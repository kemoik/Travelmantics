package com.example.travelmantics;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

   private static final int PICTURE_RESULT = 42;
   private FirebaseDatabase mfirebaseDatabase;
   private DatabaseReference mdatabaseReference;
   EditText txtTittle;
   EditText txtDescription;
   EditText txtPrice;
   Traveldeals deal;
   ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mfirebaseDatabase = FirebaseUtil.mfirebaseDatabase;
        mdatabaseReference =FirebaseUtil.mdatabaseReference;
        txtTittle = (EditText)findViewById(R.id.tittle);
        txtDescription = (EditText)findViewById(R.id.description);
        txtPrice = (EditText)findViewById(R.id.price);

        imageView = (ImageView)findViewById(R.id.image);
        Button btnImage = findViewById(R.id.btnimage);
        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
                startActivityForResult(intent.createChooser(intent,"Insert Picture"),PICTURE_RESULT);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_menu:
                saveDeal();
                Toast.makeText(this ,"Deal Saved",Toast.LENGTH_LONG).show();
                clean();
                returnToList();
                return true;
            case  R.id.delete_menu:
                 deleteDeal();
                 Toast.makeText(this,"Deal Deleted",Toast.LENGTH_LONG).show();
                 returnToList();
                 return true;
                   default:
                    return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater myinflater = getMenuInflater();
        myinflater.inflate(R.menu.save_menu, menu);
        if(FirebaseUtil.isAdmin){
            menu.findItem(R.id.delete_menu).setVisible(true);
            menu.findItem(R.id.save_menu).setVisible(true);
            enableTexts(true);
            findViewById(R.id.btnimage).setEnabled(true);
        }
        else {
            menu.findItem(R.id.delete_menu).setVisible(false);
            menu.findItem(R.id.save_menu).setVisible(false);
            enableTexts(false);
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICTURE_RESULT && resultCode == RESULT_OK){
            Uri imageUri = data.getData();
            StorageReference ref = FirebaseUtil.mStorageRef.child(imageUri.getLastPathSegment());
            ref.putFile(imageUri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //here
                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful());
                    Uri downloadUrl = urlTask.getResult();
                    final String url = String.valueOf(downloadUrl);
                    String pictureName = taskSnapshot.getStorage().getPath();
                    deal.setImageUrl(url);
                    deal.setImageName(pictureName);
                    showImage(url);
                }
            });
        }
    }

    private void  saveDeal()
    {
       deal.setTittle(txtTittle.getText().toString());
       deal.setDescription( txtDescription.getText().toString());
       deal.setPrice(txtPrice.getText().toString());
        if (deal.getId()==null){
            mdatabaseReference.push().setValue(deal);
        }
        else
            {
                mdatabaseReference.child(deal.getId()).setValue(deal);
            }
    }
    private void deleteDeal()
    {
   if(deal == null)
       {
         Toast.makeText(this, "Please save the deal before deleting ", Toast.LENGTH_SHORT).show();
         return;
       }
        mdatabaseReference.child(deal.getId()).removeValue();
   if(deal.getImageName() != null && deal.getImageName().isEmpty() == false){
       StorageReference picRef = FirebaseUtil.mStorage.getReference().child(deal.getImageName());
       picRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
           @Override
           public void onSuccess(Void aVoid) {

           }
       }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {

           }
       });
   }
    }
    private  void returnToList(){
        Intent intent = new Intent(this ,ListActivity.class );
        startActivity(intent);

    }

    private void clean()
     {
       txtTittle.setText("");
       txtDescription.setText("");
       txtPrice.setText("");
       txtTittle.requestFocus();
     }
    private void enableTexts(boolean isEnabled){
        txtTittle.setEnabled(isEnabled);
        txtDescription.setEnabled(isEnabled);
        txtPrice.setEnabled(isEnabled);
    }

    private void showImage(String url){
     if(url !=null && url.isEmpty() == false){
         int width = Resources.getSystem().getDisplayMetrics().widthPixels;
         Picasso.get()
                 .load(url)
                 .resize(width,width*2/3)
                 .centerCrop()
                 .into(imageView);
     }
    }
}

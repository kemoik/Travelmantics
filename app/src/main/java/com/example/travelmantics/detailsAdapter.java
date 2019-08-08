package com.example.travelmantics;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelmantics.R;
import com.example.travelmantics.Traveldeals;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class detailsAdapter extends RecyclerView.Adapter<detailsAdapter.detailsViewHolder>
{
    ArrayList<Traveldeals> traveldeals;
    private FirebaseDatabase mfirebaseDatabase;
    private DatabaseReference mdatabaseReference;
    private ChildEventListener childEventListener;
    private ImageView imageDeal;

    public detailsAdapter(){

        mfirebaseDatabase = FirebaseUtil.mfirebaseDatabase;
        mdatabaseReference =FirebaseUtil.mdatabaseReference;
        traveldeals = FirebaseUtil.traveldeals;
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Traveldeals td = dataSnapshot.getValue(Traveldeals.class);
                td.setId(dataSnapshot.getKey());
                traveldeals.add(td);
                notifyItemInserted(traveldeals.size()-1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mdatabaseReference.addChildEventListener(childEventListener);
    }
    @NonNull
    @Override
    public detailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.recyclerview,parent,false);
          return new detailsViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull detailsViewHolder holder, int position) {
      Traveldeals deal = traveldeals.get(position);
      holder.bind(deal);
    }

    @Override
    public int getItemCount() {
       return traveldeals.size();
    }

    public class detailsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

       TextView tvTittle;
       TextView tvDescription;
       TextView tvPrice;
      public detailsViewHolder(@NonNull View itemView) {
          super(itemView);
          tvTittle = (TextView) itemView.findViewById(R.id.tvTittle);
          tvDescription = (TextView) itemView.findViewById(R.id.tvdescription);
          tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
          imageDeal = (ImageView) itemView.findViewById(R.id.imagedeal);
          itemView.setOnClickListener(this);
      }

      public void bind(Traveldeals traveldeals){
          tvTittle.setText(traveldeals.getTittle());
          tvDescription.setText(traveldeals.getDescription());
          tvPrice.setText(traveldeals.getPrice());
          showImage(traveldeals.getImageUrl());
      }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Traveldeals selecteddeal = traveldeals.get(position);
            Intent intent = new Intent(v.getContext(),deals_activity.class);
            intent.putExtra("deals", selecteddeal);
            v.getContext().startActivity(intent);
        }

        public void showImage(String url){
          if(url != null && url.isEmpty() == false){
              Picasso.get()
                      .load(url)
                      .resize(80, 80)
                      .centerCrop()
                      .into(imageDeal);
          }
        }
    }


}

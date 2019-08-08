package com.example.travelmantics;

import android.app.Activity;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FirebaseUtil {
    public static FirebaseDatabase mfirebaseDatabase;
    public static DatabaseReference mdatabaseReference;
    private static FirebaseUtil firebaseUtil;
    public static FirebaseAuth mfirebaseAuth;
    public static ListActivity caller;
    public static FirebaseStorage mStorage;
    public static StorageReference mStorageRef;
    private static final int RC_SIGN_IN = 123;
    public static FirebaseAuth.AuthStateListener authStateListener;
    public static ArrayList<Traveldeals> traveldeals;
    public static  boolean isAdmin;

    private FirebaseUtil(){}

    public static void openFbReference(String ref, final ListActivity callerActivity){
        if(firebaseUtil == null){
            firebaseUtil = new FirebaseUtil();
            mfirebaseDatabase = FirebaseDatabase.getInstance();
            mfirebaseAuth = FirebaseAuth.getInstance();
            caller = callerActivity;
            authStateListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    if (firebaseAuth.getCurrentUser() == null) {
                        FirebaseUtil.signin();
                    } else {
                        String userId = firebaseAuth.getUid();
                        checkAdmin(userId);
                    }
                    Toast.makeText(callerActivity.getBaseContext(), "Welcome Back", Toast.LENGTH_LONG);
                }
            };
            connectStorage();
        }
        traveldeals = new ArrayList<Traveldeals>();
        mdatabaseReference = mfirebaseDatabase.getReference().child(ref);
    }
    public static void signin(){
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());


// Create and launch sign-in intent
        caller.startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.CustomTheme)
                        .setAvailableProviders(providers)
                        .setLogo(R.drawable.travels)
                        .build(),
                RC_SIGN_IN);
    }

    private static void checkAdmin(String uid){
    FirebaseUtil.isAdmin = false;
    DatabaseReference ref = mfirebaseDatabase.getReference().child("administrators").child(uid);
        ChildEventListener listener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
               FirebaseUtil.isAdmin = true;
               caller.showMenu();
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
        ref.addChildEventListener(listener);
    }

    public static void attachListener(){
        mfirebaseAuth.addAuthStateListener(authStateListener);
    }
    public static void detachListener(){
       mfirebaseAuth.removeAuthStateListener(authStateListener);
    }
    public static void connectStorage(){
        mStorage = FirebaseStorage.getInstance();
        mStorageRef = mStorage.getReference().child("deals_pictures");
    }
}

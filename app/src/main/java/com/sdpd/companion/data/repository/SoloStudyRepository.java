package com.sdpd.companion.data.repository;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.text.SimpleDateFormat;
import java.util.Date;

public class SoloStudyRepository {

    private DatabaseReference mDatabase;
    private GoogleSignInAccount account;
    private Context context;

    public SoloStudyRepository(Context context) {
        this.context = context; 
    }
    
    public void updateTimeSolo(int timeToAdd){

        account = GoogleSignIn.getLastSignedInAccount(context);
        Log.i("ACCOUNT", account.getEmail());

        mDatabase = FirebaseDatabase.getInstance().getReference();
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyy");
        String str = formatter.format(date);


        if(mDatabase.child("soloStudyStats")==null || mDatabase.child("soloStudyStats").child(account.getId())==null || mDatabase.child("soloStudyStats").child(account.getId()).child(str)==null){
            mDatabase.child("soloStudyStats").child(account.getId()).child(str).setValue(timeToAdd);
            return;
        }

        mDatabase.child("soloStudyStats").child(account.getId()).child(str).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Log.d("message",task.getResult().toString());
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    if(task.getResult()!=null){
                        if(task.getResult().getValue()==null){
                            mDatabase.child("soloStudyStats").child(account.getId()).child(str).setValue(timeToAdd);
                        }
                        else{
                            int updatedTime=Integer.parseInt(task.getResult().getValue().toString())+timeToAdd;
                            mDatabase.child("soloStudyStats").child(account.getId()).child(str).setValue(updatedTime);
                        }
                    }
                    else{
                        mDatabase.child("soloStudyStats").child(account.getId()).child(str).setValue(timeToAdd);
                    }

                }
            }
        });
    }
}

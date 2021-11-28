package com.sdpd.companion.data.remote;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sdpd.companion.data.model.Group;
import com.sdpd.companion.data.model.Message;
import com.sdpd.companion.data.model.User;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public class FirebaseMessageSource {

    private static final String TAG = "FirebaseMessageSource";

    public FirebaseDatabase firebaseDatabase;
    private final DatabaseReference mDatabase;
    public FirebaseAuth firebaseAuth;
    public FirebaseUserSource firebaseUserSource;

    @Inject
    public FirebaseMessageSource(FirebaseDatabase firebaseDatabase, FirebaseAuth firebaseAuth, FirebaseUserSource firebaseUserSource){
        this.firebaseDatabase = firebaseDatabase;
        this.firebaseAuth = firebaseAuth;
        this.mDatabase = firebaseDatabase.getReference();
        this.firebaseUserSource = firebaseUserSource;
    }

    public void sendMessage(String groupId, String message) {
//        return Completable.create(emitter -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            Log.d(TAG, user.getDisplayName());
            String key = mDatabase.child("messages/" + groupId + "/").push().getKey();
            Message messageObject = new Message(key, user.getUid(), user.getDisplayName(), message, -1);

            Map<String, Object> postValues = messageObject.toMap();
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("messages/" + groupId + "/" + key, postValues);
            childUpdates.put("groups/" + groupId + "/lastMessage", message);
            childUpdates.put("groups/" + groupId + "/lastMessageSenderName", user.getDisplayName());

            mDatabase.updateChildren(childUpdates);

//        });
    }

    public Flowable<DataSnapshot> getMessages(String groupId, int limit){
        return Flowable.create(emitter -> {
            mDatabase.child("messages/" + groupId).orderByChild("timestamp").limitToLast(limit).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    emitter.onNext(snapshot);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }, BackpressureStrategy.LATEST);
    }

    public Single<DataSnapshot> getMessageById(String groupId, String messageId) {
        return Single.create(emitter -> {
            mDatabase.child("messsages/" + groupId + "/" + messageId + "/").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    emitter.onSuccess(task.getResult());
                }
            });
        });
    }
}

package com.sdpd.companion.data.remote;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.internal.IdTokenListener;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableEmitter;
import io.reactivex.rxjava3.core.CompletableOnSubscribe;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.FlowableEmitter;
import io.reactivex.rxjava3.core.FlowableOnSubscribe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;

public class FirebaseAuthSource {
    private static final String TAG = "FirebaseAuthSource";
    FirebaseAuth firebaseAuth;
    FirebaseUserSource firebaseUserSource;

    @Inject
    public FirebaseAuthSource(FirebaseAuth firebaseAuth, FirebaseUserSource firebaseUserSource) {
        this.firebaseAuth = firebaseAuth;
        this.firebaseUserSource = firebaseUserSource;
    }


    //get current user
    public Flowable<FirebaseUser> getCurrentUser() {
        return Flowable.create(emitter -> {
            firebaseAuth.addAuthStateListener(firebaseAuth -> {
                Log.d(TAG, "auth listener");
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null){
                    Log.d(TAG, firebaseUser.getDisplayName());
                }
                emitter.onNext(firebaseUser);
            });
        }, BackpressureStrategy.LATEST);
    }

    public Completable signIn(String idToken) {
        return Completable.create(emitter -> {
            AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
            firebaseAuth.signInWithCredential(credential)
                    .addOnFailureListener(e -> emitter.onError(e))
                    .addOnSuccessListener(authResult -> {
                        firebaseUserSource.saveUserData().subscribe(emitter::onComplete);
                    });
        });
    }


}

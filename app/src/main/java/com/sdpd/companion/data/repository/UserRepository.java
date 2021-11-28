package com.sdpd.companion.data.repository;

import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.sdpd.companion.data.model.User;
import com.sdpd.companion.data.remote.FirebaseAuthSource;
import com.sdpd.companion.data.remote.FirebaseUserSource;

import java.util.Map;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public class UserRepository {

    private static final String TAG = "UserRepository";
    FirebaseAuthSource firebaseAuthSource;
    FirebaseUserSource firebaseUserSource;

    @Inject
    public UserRepository(FirebaseAuthSource firebaseAuthSource, FirebaseUserSource firebaseUserSource) {
        this.firebaseAuthSource = firebaseAuthSource;
        this.firebaseUserSource = firebaseUserSource;
    }

    private User getUserFromSnapshot(DataSnapshot snapshot) {
        Map<String, Object> values = (Map<String, Object>) snapshot.getValue();
        return new User(
                snapshot.getKey(),
                (String) values.get("displayName"),
                (values.get("photoUri").toString() != "false") ? (String) values.get("photoUri") : null
        );
    }

    public Completable signIn(String idToken) {
        return firebaseAuthSource.signIn(idToken);
    }

    public Single<User> getUserByUid (String uid) {
        return firebaseUserSource.getUserByUid(uid).map(this::getUserFromSnapshot);
    }

    public Flowable<User> getCurrentUser() {
        return firebaseAuthSource
                .getCurrentUser()
                .map(firebaseUser -> {
                    Log.d(TAG, "user reached repo");
                    return new User(firebaseUser.getUid(), firebaseUser.getDisplayName(), firebaseUser.getPhotoUrl().toString());
                });
    }
}

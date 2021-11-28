package com.sdpd.companion.di;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sdpd.companion.data.remote.FirebaseAuthSource;
import com.sdpd.companion.data.remote.FirebaseGroupSource;
import com.sdpd.companion.data.remote.FirebaseMessageSource;
import com.sdpd.companion.data.remote.FirebaseUserSource;
import com.sdpd.companion.data.repository.UserRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    @Singleton
    @Provides
    static FirebaseAuth getAuthInstance() {
        return FirebaseAuth.getInstance();
    }

    @Singleton
    @Provides
    static FirebaseAuthSource getAuthSource(FirebaseAuth firebaseAuth, FirebaseUserSource firebaseUserSource) {
        return new FirebaseAuthSource(firebaseAuth, firebaseUserSource);
    }


    @Singleton
    @Provides
    static FirebaseUserSource getUserSource(FirebaseDatabase firebaseDatabase, FirebaseAuth firebaseAuth) {
        return new FirebaseUserSource(firebaseDatabase, firebaseAuth);
    }

    @Singleton
    @Provides
    static FirebaseMessageSource getMessageSource(FirebaseDatabase firebaseDatabase, FirebaseAuth firebaseAuth, FirebaseUserSource firebaseUserSource) {
        return new FirebaseMessageSource(firebaseDatabase, firebaseAuth, firebaseUserSource);
    }


    @Singleton
    @Provides
    static FirebaseGroupSource getGroupSource(FirebaseDatabase firebaseDatabase, FirebaseAuth firebaseAuth, FirebaseUserSource firebaseUserSource) {
        return new FirebaseGroupSource(firebaseDatabase, firebaseAuth, firebaseUserSource);
    }


    @Singleton
    @Provides
    static UserRepository provideUserRepository(FirebaseAuthSource firebaseAuthSource, FirebaseUserSource firebaseUserSource) {
        return new UserRepository(firebaseAuthSource, firebaseUserSource);
    }

    @Singleton
    @Provides
    static FirebaseDatabase provideFirebaseDatabaseInstance() {
        return FirebaseDatabase.getInstance();
    }

    @Singleton
    @Provides
    static StorageReference provideStorageReference() {
        return FirebaseStorage.getInstance().getReference();
    }

}

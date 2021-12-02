package com.sdpd.companion.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sdpd.companion.data.model.User;
import com.sdpd.companion.data.repository.UserRepository;

import org.reactivestreams.Subscription;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.FlowableSubscriber;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class UserViewModel extends ViewModel {
    private static final String TAG = "UserViewModel";
    private MutableLiveData<User> user = new MutableLiveData<>();
    private  MutableLiveData<Boolean> isLoggedIn = new MutableLiveData<>();
    public UserRepository userRepository;

    @Inject
    public UserViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;

        observeCurrentUser();
    }

    private void observeCurrentUser(){
        userRepository.getCurrentUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(newUser -> {
                    Log.d(TAG, "loggedIn");
                    user.setValue(newUser);
                    isLoggedIn.setValue(true);
                }, error -> {
                    user.setValue(null);
                    isLoggedIn.setValue(false);
                    error.printStackTrace();
                });
    }

    public void signIn(String idToken) {
        Log.d(TAG, "Login Started");
        userRepository.signIn(idToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {

                });
    }

    public LiveData<User> getUser(){
        return user;
    }

    public MutableLiveData<Boolean> getIsLoggedIn() {
        return isLoggedIn;
    }
}

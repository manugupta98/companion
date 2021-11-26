package com.sdpd.companion.viewModel;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sdpd.companion.R;
import com.sdpd.companion.data.model.User;

public class LoginActivityViewModel extends ViewModel {

    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInAccount account;
    private Context context;
    private DatabaseReference mDatabase;

    public void init(Context context) {
        this.context = context;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
    }

    public GoogleSignInAccount getAccount() {
        account = GoogleSignIn.getLastSignedInAccount(context);
        return account;
    }

    public Intent getSignInIntent() {
        return mGoogleSignInClient.getSignInIntent();
    }

    public Boolean handleSignIn(Intent data) {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        return handleSignInResult(task);
    }

    private Boolean handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            mDatabase = FirebaseDatabase.getInstance().getReference();
            writeNewUser(account.getId(), account.getDisplayName(), account.getEmail());
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("FAILED", "signInResult:failed code=" + e.getStatusCode());
            return false;
        }
        return true;
    }

    private void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email);

        mDatabase.child("Users").child(userId).setValue(user);
    }
}

package com.sdpd.companion.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.database.DatabaseReference;
import com.sdpd.companion.R;
import com.sdpd.companion.viewModel.LoginActivityViewModel;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private GoogleSignInClient mGoogleSignInClient;
    private DatabaseReference mDatabase;
    private LoginActivityViewModel mLoginActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginActivityViewModel = new ViewModelProvider(this).get(LoginActivityViewModel.class);

        mLoginActivityViewModel.init(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        if(mLoginActivityViewModel.getAccount() == null) {

        }
        //check if null and send intents accordingly
    }

    public void signIn(View view) {
        Intent signInIntent = mLoginActivityViewModel.getSignInIntent();
        startActivityForResult(signInIntent, 200);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == 200) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Boolean successfulLogin = mLoginActivityViewModel.handleSignIn(data);
            if(successfulLogin)
                Toast.makeText(this, "Login succeeded!", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Please sign in with your institute email", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "An unexpected error occured", Toast.LENGTH_SHORT).show();
        }
    }
}


package com.sdpd.companion.ui.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.sdpd.companion.R;
import com.sdpd.companion.viewModel.LoginActivityViewModel;

public class LoginFragment extends Fragment implements View.OnClickListener {

    private LoginActivityViewModel mLoginActivityViewModel;
    private Button loginButton;


    public LoginFragment() {
        super(R.layout.fragment_login);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLoginActivityViewModel = new ViewModelProvider(this).get(LoginActivityViewModel.class);

        mLoginActivityViewModel.init(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        loginButton = view.findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        if(mLoginActivityViewModel.getAccount() != null) {
            changeUI();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == 200) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Boolean successfulLogin = mLoginActivityViewModel.handleSignIn(data);
            if(successfulLogin) {
                changeUI();
            }
            else
                Toast.makeText(getActivity(), "Please sign in with your institute email", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "An unexpected error occured", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        Intent signInIntent = mLoginActivityViewModel.getSignInIntent();
        startActivityForResult(signInIntent, 200);
    }

    private void changeUI() {
        final FragmentTransaction transaction = getFragmentManager().beginTransaction().setReorderingAllowed(true);
        transaction.replace(R.id.fragment_container_view, MenuFragment.class, null);
        transaction.commit();
        Toast.makeText(getActivity(), "Login succeeded!", Toast.LENGTH_SHORT).show();
    }
}
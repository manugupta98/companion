package com.sdpd.companion;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sdpd.companion.ui.home.HomeFragmentDirections;
import com.sdpd.companion.ui.login.LoginFragmentDirections;
import com.sdpd.companion.viewmodels.UserGroupViewModel;
import com.sdpd.companion.viewmodels.UserViewModel;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navView;
    NavController navController;

    UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        navView = findViewById(R.id.nav_view);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph())
                        .setDrawerLayout(drawerLayout)
                        .build();

        NavigationUI.setupWithNavController(navView, navController);
        setSupportActionBar(toolbar);
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);


        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller,
                                             @NonNull NavDestination destination, @Nullable Bundle arguments) {

                if (destination.getId() == R.id.loginFragment) {
                    toolbar.setVisibility(View.GONE);
                } else {
                    toolbar.setVisibility(View.VISIBLE);
                }
            }
        });

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

//        observeLogin();
    }

//    private void observeLogin() {
//        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//        firebaseAuth.addAuthStateListener(auth -> {
//            FirebaseUser user = auth.getCurrentUser();
//            if (user != null) {
//                navController.popBackStack(R.layout.fragment_login, true);
//                navController.navigate(R.id.homeFragment);
//            } else {
//                Log.d(TAG, "Login page opened");
//                LoginFragmentDirections.ActionLoginFragmentToHomeFragment action = LoginFragmentDirections.actionLoginFragmentToHomeFragment();
//                navController.navigate(action);
//            }
//        });
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}

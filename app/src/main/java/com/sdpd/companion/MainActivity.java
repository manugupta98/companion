package com.sdpd.companion;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.core.app.TaskStackBuilder;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sdpd.companion.data.model.User;
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

    ImageView userIcon;
    TextView userNameTextView;

    UserViewModel userViewModel;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d(TAG, "menu");
        if (item.getItemId() == R.id.logout_button) {
            Log.d(TAG, "logout");
            userViewModel.logout();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        navView = findViewById(R.id.nav_view);

        userIcon = navView.getHeaderView(0).findViewById(R.id.drawer_user_icon);
        userNameTextView = navView.getHeaderView(0).findViewById(R.id.drawer_user_name);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph())
                        .setDrawerLayout(drawerLayout)
                        .build();

        NavigationUI.setupWithNavController(navView, navController);
        setSupportActionBar(toolbar);
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        NavDirections action = HomeFragmentDirections.actionHomeFragmentToLoginFragment();
        navController.navigate(action);

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

        Menu menuNav = navView.getMenu();

        menuNav.findItem(R.id.logout_button).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.d(TAG, "logout");
                userViewModel.logout();
                GoogleSignInOptions gso = new GoogleSignInOptions.
                        Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                        build();

                GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(getApplicationContext(), gso);
                googleSignInClient.signOut();
                drawerLayout.closeDrawer(Gravity.LEFT);
                return true;
            }
        });


        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        initDrawer();
        observeLogin();
    }

    private void initDrawer() {
        userViewModel.getUser().observeForever(user -> {
            if (user != null) {
                Log.d(TAG, "new user recieved");
                Log.d(TAG, user.getDisplayName());
                Glide.with(getBaseContext())
                        .load(user.getPhotoUri())
                        .placeholder(R.drawable.default_user_icon)
                        .circleCrop()
                        .into(userIcon);
                userNameTextView.setText(user.getDisplayName());
                Log.d(TAG, userNameTextView.getText().toString());
            }
        });
    }

    private void observeLogin() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.addAuthStateListener(auth -> {
            FirebaseUser firebaseUser = auth.getCurrentUser();
            User user = null;
            if (firebaseUser == null) {
                userViewModel.setUser(null);
            } else {
                user = new User(firebaseUser.getUid(), firebaseUser.getDisplayName(), firebaseUser.getPhotoUrl().toString());
                userViewModel.setUser(user);
            }
            if (user != null) {
                if (navController.getCurrentDestination().getId() == R.id.loginFragment) {
                    NavDirections action = LoginFragmentDirections.actionLoginFragmentToHomeFragment();
                    navController.navigate(action);
                }
            } else {
                if (navController.getCurrentDestination().getId() != R.id.loginFragment) {

                    navController.navigate(R.id.loginFragment);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}

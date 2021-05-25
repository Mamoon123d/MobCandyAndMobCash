package com.cashhub.android.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import com.cashhub.android.Fragment.MoreTasksFragment;
import com.cashhub.android.Fragment.PayoutFragment;
import com.cashhub.android.Fragment.SettingFragment;
import com.cashhub.android.Fragment.TasksFragment;
import com.cashhub.android.R;
import com.cashhub.android.Services.sharedConstant;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    FirebaseAuth mAuth;
    private String TAG = "testing";
    private GoogleSignInClient mGoogleSignInClient;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_main);
            Toolbar toolbar = findViewById(R.id.custom_toolbar);
            setSupportActionBar(toolbar);
            mAuth = FirebaseAuth.getInstance();
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken("810835044995-mmor84k6ci6af18jravdamaqn9hi66a1.apps.googleusercontent.com")
                    .requestEmail()
                    .build();
            // Build a GoogleSignInClient with the options specified by gso.
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


            Log.e(TAG, "onCreate:utm" + sharedConstant.getSharedPreferenceString(MainActivity.this, "adverId", ""));




            bottomNavigationView = findViewById(R.id.bottom_nav_view);
            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    int id = menuItem.getItemId();
                    if (id == R.id.navigation_Tasks) {

                        TasksFragment tasksFragment = new TasksFragment();
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.nav_host_fragment, tasksFragment);
                        fragmentTransaction.commit();


                    }

                    if (id == R.id.navigation_moreTasks) {

                        MoreTasksFragment moreTasksFragment = new MoreTasksFragment();
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.nav_host_fragment, moreTasksFragment);
                        fragmentTransaction.commit();


                    }
                    if (id == R.id.navigation_Payout) {

                        PayoutFragment payoutFragment = new PayoutFragment();
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.nav_host_fragment, payoutFragment);
                        fragmentTransaction.commit();


                    }
                    if (id == R.id.navigation_Setting) {

                        SettingFragment settingFragment = new SettingFragment();
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.nav_host_fragment, settingFragment);
                        fragmentTransaction.commit();


                    }


                    return true;

                }
            });
            bottomNavigationView.setSelectedItemId(R.id.navigation_Tasks);

        } catch (Exception e) {
            e.printStackTrace();
            Log.w(TAG, "onCreate: MainActivity"+e );
        }
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.navigation_Tasks) {

            bottomNavigationView.getMenu().findItem(id).setChecked(true);
            bottomNavigationView.getMenu().performIdentifierAction(id, 0);



        }

        if (id == R.id.navigation_moreTasks) {
            bottomNavigationView.getMenu().findItem(id).setChecked(true);
            bottomNavigationView.getMenu().performIdentifierAction(id, 0);



        }
        if (id == R.id.navigation_Payout) {

            bottomNavigationView.getMenu().findItem(id).setChecked(true);
            bottomNavigationView.getMenu().performIdentifierAction(id, 0);




        }
        if (id == R.id.navigation_Setting) {

            bottomNavigationView.getMenu().findItem(id).setChecked(true);
            bottomNavigationView.getMenu().performIdentifierAction(id, 0);

            Log.e(TAG, "onOptionsItemSelected: "+sharedConstant.getSharedPreferenceInt(MainActivity.this, "userId", 0));
            Log.e(TAG, "onOptionsItemSelected: "+sharedConstant.getSharedPreferenceString(MainActivity.this, "securitytoken", ""));




        }

        return true;
    }

    private Boolean exit = false;

    @Override
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);
        }
    }





    @Override
    protected void onPause() {
        super.onPause();
        dismissProgressDialog();

    }

    @Override
    protected void onStop() {
        super.onStop();
        dismissProgressDialog();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

}
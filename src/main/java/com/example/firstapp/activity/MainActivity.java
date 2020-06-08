
package com.example.firstapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.firstapp.R;
import com.example.firstapp.bottom_fragments.FragmentOne;
import com.example.firstapp.bottom_fragments.FragmentThree;
import com.example.firstapp.bottom_fragments.FragmentTwo;
import com.example.firstapp.drawer_fragments.ProfileFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
//    private Button btnLogout;
    private FirebaseAuth auth;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    BottomNavigationView bottomNavigationView;
    GoogleSignInClient googleSignInClient;
    Fragment fragment;
    LinearLayout maincontent;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.navigation_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Search_bar:
                Toast.makeText(MainActivity.this, "Search", Toast.LENGTH_SHORT).show();
                break;
            case R.id.Help:
                Toast.makeText(MainActivity.this, "Help", Toast.LENGTH_SHORT).show();
                break;
            case R.id.Logout:
                auth.signOut();
                googleSignInClient.signOut().addOnCompleteListener(this,
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        });
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.nav_view);
        navigationView.bringToFront();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        maincontent = findViewById(R.id.maincontent);

        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_profile:
                        FragmentManager manager = getSupportFragmentManager();
                        manager.beginTransaction().replace(R.id.maincontent, new ProfileFragment()).commit();
                        break;

                    case R.id.navigation_notifications:
                        FragmentManager manager1 = getSupportFragmentManager();
                        manager1.beginTransaction().replace(R.id.maincontent, new FragmentThree()).commit();
                        break;


                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });


        bottomNavigationView = findViewById(R.id.bottomnav);
        bottomNavigationView.setOnNavigationItemSelectedListener(itemSelectedListener);


//        btnLogout = (Button) findViewById(R.id.logout1);
        auth = FirebaseAuth.getInstance();

//        btnLogout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                signOut();
//
//            }
//        });

    }


    private BottomNavigationView.OnNavigationItemSelectedListener itemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.maincontent, new FragmentOne()).commit();
                    //Toast.makeText(MainActivity.this, "one", Toast.LENGTH_SHORT).show();
                    // fragment = new FragmentOne();
                    ////Intent i=new Intent(getApplicationContext(),SignupActivity.class);
                    // startActivity(i);
                    break;
                case R.id.navigation_chat:
                    FragmentManager manager1 = getSupportFragmentManager();
                    manager1.beginTransaction().replace(R.id.maincontent, new FragmentTwo()).commit();
                    //Intent i1=new Intent(getApplicationContext(),MainActivity.class);
                    //startActivity(i1);
                    // fragment = new FragmentTwo();
                    break;

                case R.id.navigation_notifications:
                    // Intent i3=new Intent(getApplicationContext(),MainActivity.class);
                    // startActivity(i3);

                    FragmentManager manager2 = getSupportFragmentManager();
                    manager2.beginTransaction().replace(R.id.maincontent, new FragmentThree()).commit();

                    // fragment = new FragmentThree();
                    break;

            }

            return true;


        }


    };

    private void signOut() {
        auth.signOut();
        googleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
    }

}

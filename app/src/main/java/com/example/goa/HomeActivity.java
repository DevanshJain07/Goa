package com.example.goa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import org.tensorflow.lite.Interpreter;
import android.content.res.AssetFileDescriptor;
import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.goa.fragments.HomeFragment;
import com.example.goa.fragments.MyPicFragment;
import com.example.goa.fragments.MonumentFragment;
import com.example.goa.fragments.ProfileFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseUser firebaseUser;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public HomeFragment homeFragment;
    public MyPicFragment myPicFragment;
    public MonumentFragment monumentFragment;
    public ProfileFragment profileFragment;
    protected Interpreter tflite;

    protected void onStart() {
        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            startActivity(new Intent(HomeActivity.this, StartActivity.class));
            finish();
        }
    }

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        homeFragment = new HomeFragment();
        myPicFragment = new MyPicFragment();
        monumentFragment = new MonumentFragment();
        profileFragment = new ProfileFragment();

        try {
            tflite = new Interpreter(loadModelFile(this));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();

            navigationView.setCheckedItem(R.id.nav_home);
        }

    }

    private MappedByteBuffer loadModelFile(Activity activity) throws IOException {
//        AssetManager am = getAssets();
//        InputStream inputStream = am.open("myfoldername/myfilename");
//        File file = createFileFromInputStream(inputStream);

        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd("converted_model.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        homeFragment).commit();
                break;
            case R.id.nav_mypics:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        myPicFragment).commit();
                break;
            case R.id.nav_monument:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        monumentFragment).commit();
                break;
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        profileFragment).commit();
                break;

            case R.id.nav_logout:
                mAuth.signOut();
                firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
                if (firebaseUser==null) {
                    Toast.makeText(this,"Successfully Logged Out",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(HomeActivity.this, StartActivity.class));
                    finish();
                }else {
                    Toast.makeText(this,"Something Went Wrong",Toast.LENGTH_SHORT).show();
                }
                break;
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            super.onBackPressed();
        }
    }
}
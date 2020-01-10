package com.example.goa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.goa.LoginActivity;
import com.example.goa.MainActivity;
import com.example.goa.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartActivity extends AppCompatActivity {
    Button login,register;
    FirebaseUser firebaseUser;

    protected void onStart() {
        super.onStart();
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        //redirect if user is not null
        if (firebaseUser!=null){
            startActivity(new Intent(StartActivity.this, HomeActivity.class));
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        login=findViewById(R.id.Login);
        register=findViewById(R.id.Register);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StartActivity.this, LoginActivity.class));
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StartActivity.this,RegisterActivity.class));
            }
        });
    }
}

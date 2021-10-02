package com.example.marketapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private Button btnSalir;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSalir = findViewById(R.id.btnSalir);
        mAuth = FirebaseAuth.getInstance();

        btnSalir.setOnClickListener(view->{
            mAuth.signOut();
            Toast.makeText(this, "Cerrando Sesión", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,LoginActivity.class));
        });
    }
}
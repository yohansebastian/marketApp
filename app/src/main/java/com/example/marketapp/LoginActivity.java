package com.example.marketapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class LoginActivity extends AppCompatActivity  {
    // Definicion de atributos
    EditText etEmail,etPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Para cambiar el color del icono de la barra de estado
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_login);
       etEmail = findViewById(R.id.etEmail);
       etPassword = findViewById(R.id.etPassword);
       mAuth = FirebaseAuth.getInstance();


    }
    public void onLoginClick(View view){
        startActivity(new Intent(this,RegisterActivity.class));
        overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
    }
    public void LoginClick(View view){
        usuarioLogin();
    }
    public void usuarioLogin(){
        String correo = etEmail.getText().toString();
        String contraseña = etPassword.getText().toString();

        if (TextUtils.isEmpty(correo)){
            etEmail.setError("Ingrese un correo");
        }else if (TextUtils.isEmpty(contraseña)){
            etPassword.setError("Ingrese la contraseña");
        }else{
            mAuth.signInWithEmailAndPassword(correo,contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                 if(task.isSuccessful()){
                     startActivity(new Intent(LoginActivity.this,MainActivity.class));
                 }else{
                     Log.w("TAG","Error:",task.getException());
                 }
                }
            });
        }
    }
}
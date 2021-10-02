package com.example.marketapp;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.transition.Visibility;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class RegisterActivity extends AppCompatActivity {
    EditText etNombre,etPais,etCiudad,etEmail,etClave,etRol,etNombreTienda;
    CircularProgressButton cirRegisterButton;
    private ArrayList<Usuario>usuarioDB;
    private String usuarioID;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etNombre = findViewById(R.id.etNombre);
        etPais = findViewById(R.id.etPais);
        etCiudad = findViewById(R.id.etCiudad);
        etEmail = findViewById(R.id.etEmail);
        etClave = findViewById(R.id.etClave);
        etRol = findViewById(R.id.etRol);
        etNombreTienda = findViewById(R.id.etNombreTienda);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        changeStatusBarColor();
    }
    public void changeStatusBarColor(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.register_bk_color));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }
    public void onLoginClick(View view){
        startActivity(new Intent(this,LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    public void registerClick(View view){
        usuarioDB = new ArrayList<Usuario>();
        Usuario usuario = new Usuario();
        String nombre = etNombre.getText().toString();
        String ciudad = etCiudad.getText().toString();
        String pais   = etPais.getText().toString();
        String correo = etEmail.getText().toString();
        String rol    = etRol.getText().toString();
        String contraseña =  etClave.getText().toString();
        String nombreTienda = etNombreTienda.getText().toString();
        if(nombre.isEmpty()){
            etNombre.setError("Ingresar un nombre");
            etNombre.requestFocus();
        } else{
            usuario.setNombre(nombre);
        }
        if(ciudad.isEmpty()){
            etCiudad.setError("Ingrese una ciudad");
            etCiudad.requestFocus();
        }else{
            usuario.setCiudad(ciudad);
        }
        if(pais.isEmpty()){
            etPais.setError("Ingrese una pais");
            etPais.requestFocus();
        }else{
            usuario.setPais(pais);
        }
        if(!validarCorreo(correo)){
            etEmail.setError("Ingrese un correo valido");
            etEmail.requestFocus();
        }else{
            usuario.setCorreo(correo);
        }
        if(rol.isEmpty()){
            etRol.setError("Ingrese un rol");
            etRol.requestFocus();
        }else{
            usuario.setRol(rol);
        }
        if(validarContraseña(contraseña.trim())){
            usuario.setContraseña(contraseña);
        }else{
            etClave.setError("Ingrese una clave valida");
            etClave.requestFocus();
        }
        usuario.setNombreTienda(nombreTienda);
        mAuth.createUserWithEmailAndPassword(usuario.getCorreo(),usuario.getContraseña()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    usuarioID = mAuth.getCurrentUser().getUid();
                    DocumentReference documentReference = db.collection("usuarios").document(usuarioID);
                    Map<String,Object> usuarioObj = new HashMap<>();
                    usuarioObj.put("nombre",usuario.getNombre());
                    usuarioObj.put("correo",usuario.getCorreo());
                    usuarioObj.put("pais",usuario.getPais());
                    usuarioObj.put("ciudad",usuario.getCiudad());
                    usuarioObj.put("contraseña",usuario.getContraseña());
                    usuarioObj.put("rol",usuario.getRol());
                    usuarioObj.put("nombreTienda",usuario.getNombreTienda());

                    documentReference.set(usuario).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("TAG","onSuccess: Usuario Registrado exitosamente"+usuarioID);
                            startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                        }
                    });
                    Toast.makeText(RegisterActivity.this, "Usuario registrado con exito", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(RegisterActivity.this, "Usuario no registrado"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean validarCorreo(String correo){
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(correo).matches();
    }
    public  boolean validarContraseña(final String contraseña){
        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(contraseña);

        return matcher.matches();
    }
}
package com.example.invernadero;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Registro extends AppCompatActivity {

    Button btnRegister;
    TextView txtLogin;
    EditText etClave, etUsuario;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_registro);
        /*FIREBASE AUTH*/
        mAuth = FirebaseAuth.getInstance();

        etClave = (EditText) findViewById(R.id.passwordRegister);
        etUsuario = (EditText) findViewById(R.id.usernameRegister);
        btnRegister = findViewById(R.id.registerButton);
        txtLogin = findViewById(R.id.loginTextBack);
        btnRegister.setOnClickListener(v->{
            crearUsuario();
        });
        txtLogin.setOnClickListener(v->{
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }


    /*METODO CREAR USUARIO*/
    protected void crearUsuario(){
        String email = etUsuario.getText().toString();
        String clave = etClave.getText().toString();

        if (TextUtils.isEmpty(email)) {
            etUsuario.setError("Este campo no debe estar vacio");
            etUsuario.requestFocus();
        }else if (TextUtils.isEmpty(clave)){
            etClave.setError("Este campo no debe estar vacio");
            etClave.setError("Este campo no debe estar vacio");
        }
        else if (clave.length() <= 8){
            etClave.setError("La contraseña no debe ser menor a 8 digitos");
            etClave.setText("");
            etClave.requestFocus();
        }else{
            mAuth.createUserWithEmailAndPassword(email, clave).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(Registro.this, "USUARIO CREADO CON EXITO", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Registro.this, MainActivity.class));
                        finish();
                    }else{
                        System.out.println(task.getException().getMessage());
                        if (task.getException().getMessage().equalsIgnoreCase("The email address is already in use by another account.")){
                            Toast.makeText(Registro.this, "Este usuario ya esta registrado!", Toast.LENGTH_SHORT).show();
                            etUsuario.setText("");
                            etClave.setText("");
                        }
                    }
                }
            });
        }
    }

    /*se controla la pulsacion del boton atras y cierra la aplicacion*/
    @Override
    public void onBackPressed() {
        AlertDialog.Builder myBulid = new AlertDialog.Builder(this);
        myBulid.setMessage("¿Deseas salir de la aplicacion?");
        myBulid.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        myBulid.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = myBulid.create();
        dialog.show();
    }
}
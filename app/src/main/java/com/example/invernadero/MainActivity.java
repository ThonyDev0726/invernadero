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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    Button btnLogin;
    TextView txtRegister;
    EditText etClave, etUsuario;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        /*FIREBASE AUTH*/
        mAuth = FirebaseAuth.getInstance();
        etClave = (EditText) findViewById(R.id.passwordLogin);
        etUsuario = (EditText) findViewById(R.id.usernameLogin);
        btnLogin = (Button) findViewById(R.id.loginButton);
        txtRegister = (TextView) findViewById(R.id.signupTextBack);
        btnLogin.setOnClickListener(v -> {
            loginUser();
        });
        txtRegister.setOnClickListener(v -> {
            Intent intent = new Intent(this, Registro.class);
            startActivity(intent);
            finish();
        });
    }

    /*VERIFCAMOS SI EL USUARIO ESTA EN NUESTRA BASE DE DATOS*/
    private void loginUser() {
        String email = etUsuario.getText().toString();
        String clave = etClave.getText().toString();

        if (TextUtils.isEmpty(email)) {
            etUsuario.setError("Este campo no debe estar vacio");
            etUsuario.requestFocus();
        } else if (TextUtils.isEmpty(clave)) {
            etClave.setError("Este campo no debe estar vacio");
            etClave.requestFocus();
        } else if (clave.length() <= 8) {
            etClave.setError("La contraseña no debe ser menor a 8 digitos");
            etClave.setText("");
            etClave.requestFocus();
        } else if (email.equalsIgnoreCase("admin@gmail.com") && clave.equalsIgnoreCase("admin1234567890")) {
            Toast.makeText(MainActivity.this, "Acceso correcto", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, administrador_dashboard.class).putExtra("Usuario", etUsuario.getText().toString()));
            finish();
        } else {
            mAuth.signInWithEmailAndPassword(email, clave).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Acceso correcto", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, usuario_dashboard.class)
                                .putExtra("Usuario", etUsuario.getText().toString())
                                .putExtra("Cargo", "usuario"));
                        finish();
                    } else {
                        System.out.println(task.getException().getMessage());
                        if (task.getException().getMessage().equalsIgnoreCase("There is no user record corresponding to this identifier. The user may have been deleted.")) {
                            Toast.makeText(MainActivity.this, "Usuario no registrado!", Toast.LENGTH_SHORT).show();
                            etUsuario.setText("");
                            etClave.setText("");
                        } else if (task.getException().getMessage().equalsIgnoreCase("The password is invalid or the user does not have a password.")) {
                            Toast.makeText(MainActivity.this, "Clave incorrecta", Toast.LENGTH_SHORT).show();
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
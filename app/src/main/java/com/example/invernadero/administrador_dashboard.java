package com.example.invernadero;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class administrador_dashboard extends AppCompatActivity {
    ImageView imgNuevo, imgRegistros;
    TextView etUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_administrador_dashboard);
        imgNuevo = (ImageView) findViewById(R.id.imgNuevoReg);
        etUsuario = findViewById(R.id.txtUsuarioLogin);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            etUsuario.setText(bundle.getString("Usuario"));
        }
        imgRegistros = (ImageView) findViewById(R.id.imgRegistros);
        imgNuevo.setOnClickListener(v -> {
            startActivity(new Intent(administrador_dashboard.this, ActivityCrearRegistro.class));
        });
        imgRegistros.setOnClickListener(v -> {
            startActivity(new Intent(administrador_dashboard.this, MostrarRegistros.class));
        });
    }


    /*se controla la pulsacion del boton atras y cierra la aplicacion*/
    @Override
    public void onBackPressed() {
        AlertDialog.Builder myBulid = new AlertDialog.Builder(this);
        myBulid.setMessage("¿Deseas cerrar sesion?");
        myBulid.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(administrador_dashboard.this, MainActivity.class));
                Toast.makeText(administrador_dashboard.this, "Cerraste sesion", Toast.LENGTH_SHORT).show();
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

//https://youtu.be/DWIGAkYkpg8
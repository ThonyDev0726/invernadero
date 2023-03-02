package com.example.invernadero;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class usuario_dashboard extends AppCompatActivity {
    TextView etUsuario;
    ImageView imgNuevo, imgRegistros;
    ConstraintLayout lay_usu, lay_reg, lay_form, lay_control;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_usuario_dashboard);
        etUsuario = findViewById(R.id.txtUsuarioLogin_u);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            etUsuario.setText(bundle.getString("Usuario"));
        }
        lay_form = findViewById(R.id.card_nuevo_reg_usu);
        lay_reg = findViewById(R.id.card_ver_reg_usu);
        lay_control = findViewById(R.id.card_control_usu);
        lay_form.setOnClickListener(v -> {
            startActivity(new Intent(usuario_dashboard.this, nuevo_registro_usu.class).putExtra("Cargo", "usuario"));
        });
        lay_reg.setOnClickListener(v -> {
            startActivity(new Intent(usuario_dashboard.this, MostrarRegistros.class).putExtra("Cargo", "usuario"));
        });
        lay_control.setOnClickListener(v -> {
            startActivity(new Intent(usuario_dashboard.this, Simulacion.class));
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
                startActivity(new Intent(usuario_dashboard.this, MainActivity.class));
                Toast.makeText(usuario_dashboard.this, "Cerraste sesion", Toast.LENGTH_SHORT).show();
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
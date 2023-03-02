package com.example.invernadero;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class Simulacion extends AppCompatActivity {

    EditText etHumedad;
    Switch aSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_simulacion);
        etHumedad = findViewById(R.id.etHumedadControl);
        aSwitch = findViewById(R.id.switchBomba);
        aSwitch.setOnClickListener(v -> {
            if (etHumedad.getText().toString().isEmpty()) {
                Toast.makeText(Simulacion.this, "Se debe saber la humedad", Toast.LENGTH_SHORT).show();
                aSwitch.setChecked(false);
            }
        });
        etHumedad.setOnClickListener(v -> {
            Integer humedad = Integer.parseInt(etHumedad.getText().toString());
            if (humedad >= 25) {
                aSwitch.setChecked(true);
                Toast.makeText(Simulacion.this, "Estado de bomba cambiado", Toast.LENGTH_SHORT).show();
            } else {
                aSwitch.setChecked(false);
                Toast.makeText(Simulacion.this, "No se puede cambiar el estado de la bomba", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
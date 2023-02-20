package com.example.invernadero;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.service.controls.actions.FloatAction;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DetalleRegistro extends AppCompatActivity {

    TextView txtObservacionDetalle, txtTemperaturaDetalle, txtHumedadDetalle,
            txtLuminosidadDetalle, txtVentilacionDetalle, txtRiegoDetalle,
            txtFechaDetalle, txtHoraDetalle;
    ImageView imgRegistro;
    FloatingActionButton btnEliminar, btnEditar;
    String key, imgUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_detalle_registro);

        txtObservacionDetalle = findViewById(R.id.txtObservacionDetalle);
        txtTemperaturaDetalle = findViewById(R.id.txtTemperaturaDetalle);
        txtHumedadDetalle = findViewById(R.id.txtHumedadDetalle);
        txtLuminosidadDetalle = findViewById(R.id.txtLuminosidadDetalle);
        txtVentilacionDetalle = findViewById(R.id.txtVentilacionDetalle);
        txtRiegoDetalle = findViewById(R.id.txtRiegoDetalle);
        txtFechaDetalle = findViewById(R.id.txtFechaDetalle);
        txtHoraDetalle = findViewById(R.id.txtHoraDetalle);
        imgRegistro = findViewById(R.id.imgRegistro);
        btnEditar = findViewById(R.id.editButton);
        btnEliminar = findViewById(R.id.deleteButton);

        //Obtener los datos de la card seleccionada
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            txtObservacionDetalle.setText(bundle.getString("InvObservacion"));
            txtTemperaturaDetalle.setText(bundle.getString("InvTemperatura"));
            txtHumedadDetalle.setText(bundle.getString("InvHumedad"));
            txtLuminosidadDetalle.setText(bundle.getString("InvLuz"));
            txtVentilacionDetalle.setText(bundle.getString("InvVentilacion"));
            txtRiegoDetalle.setText(bundle.getString("InvRiego"));
            txtFechaDetalle.setText(bundle.getString("InvFecha"));
            txtHoraDetalle.setText(bundle.getString("InvHora"));
            imgUrl = bundle.getString("InvImagen");
            key = bundle.getString("Key");
            Glide.with(this).load(bundle.getString("InvImagen")).into(imgRegistro);
        }
        btnEliminar.setOnClickListener(v -> {
            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Invernadero");
            FirebaseStorage storage = FirebaseStorage.getInstance();

            StorageReference storageReference = storage.getReferenceFromUrl(imgUrl);
            storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(DetalleRegistro.this, "Registro eliminado con exito", Toast.LENGTH_LONG).show();
                    reference.child(key).removeValue();
                    startActivity(new Intent(getApplicationContext(), MostrarRegistros.class));
                    finish();
                }
            });
        });
        btnEditar.setOnClickListener(v -> {
            Intent intent = new Intent(DetalleRegistro.this, ActualizarDetalle.class)
                    .putExtra("InvImagen", imgUrl)
                    .putExtra("InvFecha", txtFechaDetalle.getText().toString())
                    .putExtra("InvTemperatura", txtTemperaturaDetalle.getText().toString())
                    .putExtra("InvObservacion", txtObservacionDetalle.getText().toString())
                    .putExtra("InvHumedad", txtHumedadDetalle.getText().toString())
                    .putExtra("InvLuz", txtLuminosidadDetalle.getText().toString())
                    .putExtra("InvVentilacion", txtVentilacionDetalle.getText().toString())
                    .putExtra("InvRiego", txtRiegoDetalle.getText().toString())
                    .putExtra("InvHora", txtHoraDetalle.getText().toString())
                    .putExtra("Key", key);
            startActivity(intent);
            finish();
        });

    }

    /*se controla la pulsacion del boton atras y cierra la aplicacion*/
    @Override
    public void onBackPressed() {
        startActivity(new Intent(DetalleRegistro.this, MostrarRegistros.class));
        finish();
    }
}
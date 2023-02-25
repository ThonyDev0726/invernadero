package com.example.invernadero;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class nuevo_registro_usu extends AppCompatActivity {
    ImageView uploadImage;
    Button btnGuardar, btnCancelar;
    EditText etTemperatura, etHumedad, etLuz, etVentilacion, etRiego, etFecha, etHora, etObservacion;
    String invTemperatura, invHumedad, invLuz, invVentilacion, invRiego, invFecha, invHora, invObservacion, invImagen;
    Uri uri;
    Invernadero invernadero;
    TextView arduino;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_nuevo_registro_usu);

        //Concatenamos la interfaz con las variables de esta clase
        etTemperatura = findViewById(R.id.txtTemperatura);
        etHumedad = findViewById(R.id.txtHumedad);
        etLuz = findViewById(R.id.txtLuz);
        etVentilacion = findViewById(R.id.txtVentilacion);
        arduino = findViewById(R.id.txt_arduino_usu);
        etRiego = findViewById(R.id.txtRiego);
        etFecha = findViewById(R.id.txtFecha);
        etHora = findViewById(R.id.txtHora);
        etObservacion = findViewById(R.id.txtObserbacion);
        uploadImage = findViewById(R.id.imgRegistro);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnCancelar = findViewById(R.id.btnCancelar);

        //
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            uri = data.getData();
                            uploadImage.setImageURI(uri);
                        } else {
                            Toast.makeText(nuevo_registro_usu.this, "No hay imagen seleccionada", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );

        //Evento para poder acceder a imagenes
        uploadImage.setOnClickListener(v -> {
            Intent photoPicker = new Intent(Intent.ACTION_PICK);
            photoPicker.setType("image/*");
            activityResultLauncher.launch(photoPicker);
        });


        //Evento guardar
        btnGuardar.setOnClickListener(v -> {
            guardarDatos();
        });
        arduino.setOnClickListener(v -> {
            Toast.makeText(nuevo_registro_usu.this, "Arduino funcionando", Toast.LENGTH_LONG).show();
            etTemperatura.setText(numeroTemperatura() + "");
            etHumedad.setText(numeroHumedad() + "");
        });
    }


    //metodo guardar con realTime y fireStorage
    public void guardarDatos() {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Imagenes Invernadero")
                .child(uri.getLastPathSegment());

        AlertDialog.Builder builder = new AlertDialog.Builder(nuevo_registro_usu.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        storageReference.putFile(uri).addOnSuccessListener(v -> {
            Task<Uri> uriTask = v.getStorage().getDownloadUrl();
            while (!uriTask.isComplete()) ;
            Uri urlImage = uriTask.getResult();
            invImagen = urlImage.toString();
            cargarDatos();
            dialog.dismiss();

        }).addOnFailureListener(v -> {
            dialog.dismiss();
        });
    }

    public void cargarDatos() {
        invTemperatura = etTemperatura.getText().toString();
        invHumedad = etHumedad.getText().toString();
        invLuz = etLuz.getText().toString();
        invVentilacion = etVentilacion.getText().toString();
        invRiego = etRiego.getText().toString();
        invFecha = etFecha.getText().toString();
        invHora = etHora.getText().toString();
        invObservacion = etObservacion.getText().toString();
        invernadero = new Invernadero(invTemperatura, invHumedad, invLuz, invVentilacion, invRiego, invFecha, invHora, invObservacion, invImagen);
        Date date = new Date();
        FirebaseDatabase.getInstance().getReference("Invernadero").child(cadenaAleatoria(10) + date.toString()).setValue(invernadero).addOnCompleteListener(v -> {
            if (v.isSuccessful()) {
                Toast.makeText(nuevo_registro_usu.this, "Los datos fueron guardados", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(v -> {
            Toast.makeText(nuevo_registro_usu.this, "No se pudo guardar los datos", Toast.LENGTH_LONG).show();
        });
    }

    public static String cadenaAleatoria(int longitud) {
        // El banco de caracteres
        String banco = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        // La cadena en donde iremos agregando un carácter aleatorio
        String cadena = "";
        for (int x = 0; x < longitud; x++) {
            int indiceAleatorio = numeroAleatorioEnRango(0, banco.length() - 1);
            char caracterAleatorio = banco.charAt(indiceAleatorio);
            cadena += caracterAleatorio;
        }
        return cadena;
    }

    public static int numeroAleatorioEnRango(int minimo, int maximo) {
        // nextInt regresa en rango pero con límite superior exclusivo, por eso sumamos 1
        return ThreadLocalRandom.current().nextInt(minimo, maximo + 1);
    }

    /*se controla la pulsacion del boton atras y cierra la aplicacion*/
    @Override
    public void onBackPressed() {
        AlertDialog.Builder myBulid = new AlertDialog.Builder(this);
        myBulid.setMessage("¿Deseas cancelar el registro?");
        myBulid.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(nuevo_registro_usu.this, usuario_dashboard.class));
                Toast.makeText(nuevo_registro_usu.this, "Registro cancelado!!", Toast.LENGTH_SHORT).show();
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
    public double numeroTemperatura() {
        int min_val = 10;
        int max_val = 50;
        double randomNum = Math.random() * (max_val - min_val);
        return randomNum;
    }

    public double numeroHumedad() {
        int min_val = 10;
        int max_val = 50;
        double randomNum = Math.random() * (max_val - min_val);
        return randomNum;
    }
}
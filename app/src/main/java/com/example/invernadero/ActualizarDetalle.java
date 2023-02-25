package com.example.invernadero;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ActualizarDetalle extends AppCompatActivity {

    ImageView uploadImage;
    Button btnActualizar, btnCancelar;
    EditText etTemperatura, etHumedad, etLuz, etVentilacion, etRiego, etFecha, etHora, etObservacion;
    String invTemperatura, invHumedad, invLuz, invVentilacion, invRiego, invFecha, invHora, invObservacion, invImagen, oldImageUrl, key;
    Uri uri;
    DatabaseReference databaseReference;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_actualizar_detalle);

        //Concatenamos la interfaz con las variables de esta clase
        etTemperatura = findViewById(R.id.txtTemperaturaActualizar);
        etHumedad = findViewById(R.id.txtHumedadActualizar);
        etLuz = findViewById(R.id.txtLuzActualizar);
        etVentilacion = findViewById(R.id.txtVentilacionActualizar);
        etRiego = findViewById(R.id.txtRiegoActualizar);
        etFecha = findViewById(R.id.txtFechaActualizar);
        etHora = findViewById(R.id.txtHoraActualizar);
        etObservacion = findViewById(R.id.txtObserbacionActualizar);
        uploadImage = findViewById(R.id.imgRegistroActualizar);
        btnActualizar = findViewById(R.id.btnActualizar);
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
                            Toast.makeText(ActualizarDetalle.this, "No se seleciono ninguna imagen", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Glide.with(ActualizarDetalle.this).load(bundle.getString("InvImagen")).into(uploadImage);
            etObservacion.setText(bundle.getString("InvObservacion"));
            etTemperatura.setText(bundle.getString("InvTemperatura"));
            etHumedad.setText(bundle.getString("InvHumedad"));
            etLuz.setText(bundle.getString("InvLuz"));
            etVentilacion.setText(bundle.getString("InvVentilacion"));
            etRiego.setText(bundle.getString("InvRiego"));
            etFecha.setText(bundle.getString("InvFecha"));
            etHora.setText(bundle.getString("InvHora"));
            oldImageUrl = bundle.getString("InvImagen");
            key = bundle.getString("Key");
        }
        databaseReference = FirebaseDatabase.getInstance().getReference("Invernadero").child(key);
        //Evento para poder acceder a imagenes
        uploadImage.setOnClickListener(v -> {
            Intent photoPicker = new Intent(Intent.ACTION_PICK);
            photoPicker.setType("image/*");
            activityResultLauncher.launch(photoPicker);
        });

        //Evento actualizar
        btnActualizar.setOnClickListener(v -> {
            saveData();
        });
    }

    public void saveData() {
        storageReference = FirebaseStorage.getInstance().getReference().child("Imagenes Invernadero").child(uri.getLastPathSegment());
        AlertDialog.Builder builder = new AlertDialog.Builder(ActualizarDetalle.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();


        storageReference.putFile(uri).addOnSuccessListener(v -> {
            Task<Uri> uriTask = v.getStorage().getDownloadUrl();
            while (!uriTask.isComplete()) ;
            Uri urlImage = uriTask.getResult();
            invImagen = urlImage.toString();
            //Se actualiza los datos
            actualizarDatos();
            dialog.dismiss();

        }).addOnFailureListener(v -> {
            dialog.dismiss();
        });
    }

    public void actualizarDatos() {
        invTemperatura = etTemperatura.getText().toString();
        invHumedad = etHumedad.getText().toString();
        invLuz = etLuz.getText().toString();
        invVentilacion = etVentilacion.getText().toString();
        invRiego = etRiego.getText().toString();
        invFecha = etFecha.getText().toString();
        invHora = etHora.getText().toString();
        invObservacion = etObservacion.getText().toString();

        Invernadero invernadero = new Invernadero(invTemperatura,
                invHumedad,
                invLuz,
                invVentilacion,
                invRiego,
                invFecha,
                invHora,
                invObservacion,
                invImagen);
        databaseReference.setValue(invernadero).addOnCompleteListener(v -> {
            if (v.isSuccessful()) {
                Toast.makeText(ActualizarDetalle.this, "El registro fue actualizado", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ActualizarDetalle.this, MostrarRegistros.class);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(v -> {
            Toast.makeText(ActualizarDetalle.this, "No se pudo actualizar el registro", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(ActualizarDetalle.this, MostrarRegistros.class);
            startActivity(intent);
            finish();
        });
    }
    /*se controla la pulsacion del boton atras y cierra la aplicacion*/
    @Override
    public void onBackPressed() {
        Bundle parametros_usu = getIntent().getExtras();
        String cargo = parametros_usu.getString("Cargo");
        if (cargo.equalsIgnoreCase("usuario")) {
            startActivity(new Intent(ActualizarDetalle.this, usuario_dashboard.class));
        } else {
            startActivity(new Intent(ActualizarDetalle.this, ActivityCrearRegistro.class));
        }
        finish();
    }
}
package com.example.invernadero;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MostrarRegistros extends AppCompatActivity {

    ImageButton btnFlotante;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    RecyclerView recyclerView;
    List<Invernadero> datalist;
    MyAdapter adapter;
    EditText searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_mostrar_registros);

        recyclerView = findViewById(R.id.recyclerView);
        btnFlotante = findViewById(R.id.btnCrearIntent);
        searchView = findViewById(R.id.search);
        searchView.clearFocus();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(MostrarRegistros.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        AlertDialog.Builder builder = new AlertDialog.Builder(MostrarRegistros.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        datalist = new ArrayList<>();

        adapter = new MyAdapter(MostrarRegistros.this, datalist);
        recyclerView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Invernadero");
        dialog.show();
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                datalist.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    Invernadero invernadero = itemSnapshot.getValue(Invernadero.class);
                    invernadero.setKey(itemSnapshot.getKey());
                    datalist.add(invernadero);
                }
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                searchList(searchView.getText().toString().toLowerCase());
                if(recyclerView.getAdapter().getItemCount() == 0){
                    Toast.makeText(MostrarRegistros.this, "No existen registros", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btnFlotante.setOnClickListener(v -> {
            startActivity(new Intent(MostrarRegistros.this,ActivityCrearRegistro.class));
        });
    }

    public void searchList(String text) {
        ArrayList<Invernadero> searchList = new ArrayList<>();
        for (Invernadero dataClass : datalist) {
            if (dataClass.getInvObservacion().toLowerCase().contains(text.toLowerCase())) {
                searchList.add(dataClass);
            }else if (dataClass.getInvFecha().toLowerCase().contains(text.toLowerCase())){
                searchList.add(dataClass);
            }
            else{
                System.out.println("No existen registros");
            }
        }
        adapter.searchDataList(searchList);
    }
    /*se controla la pulsacion del boton atras y cierra la aplicacion*/
    @Override
    public void onBackPressed() {
        startActivity(new Intent(MostrarRegistros.this, administrador_dashboard.class));
        finish();
    }
}
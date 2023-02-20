package com.example.invernadero;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private Context context;
    private List<Invernadero> dataList;

    public MyAdapter(Context context, List<Invernadero> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context).load(dataList.get(position).getInvImagen()).into(holder.imgVista);
        holder.txtFechaVista.setText(dataList.get(position).getInvFecha());
        holder.txtObservacionVista.setText(dataList.get(position).getInvObservacion());
        holder.txtTemperaturaVista.setText(dataList.get(position).getInvTemperatura());
        holder.txtHumedadVista.setText(dataList.get(position).getInvHumedad());
        holder.txtLuminosidadVista.setText(dataList.get(position).getInvLuz());
        holder.txtVentilacionVista.setText(dataList.get(position).getInvVentilacion());
        holder.txtRiegoVista.setText(dataList.get(position).getInvRiego());
        holder.txtHoraVista.setText(dataList.get(position).getInvHora());

        //Evento click para llenar los datos en la interfaz detalle
        holder.recCard.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetalleRegistro.class);
            intent.putExtra("InvImagen", dataList.get(holder.getAdapterPosition()).getInvImagen());
            intent.putExtra("InvFecha", dataList.get(holder.getAdapterPosition()).getInvFecha());
            intent.putExtra("InvTemperatura", dataList.get(holder.getAdapterPosition()).getInvTemperatura());
            intent.putExtra("InvObservacion", dataList.get(holder.getAdapterPosition()).getInvObservacion());
            intent.putExtra("InvHumedad", dataList.get(holder.getAdapterPosition()).getInvHumedad());
            intent.putExtra("InvLuz", dataList.get(holder.getAdapterPosition()).getInvLuz());
            intent.putExtra("InvVentilacion", dataList.get(holder.getAdapterPosition()).getInvVentilacion());
            intent.putExtra("InvRiego", dataList.get(holder.getAdapterPosition()).getInvRiego());
            intent.putExtra("InvHora", dataList.get(holder.getAdapterPosition()).getInvHora());
            intent.putExtra("Key", dataList.get(holder.getAdapterPosition()).getKey());
            context.startActivity(intent);
            ((Activity) context).finish();
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void searchDataList(ArrayList<Invernadero> searchList) {
        dataList = searchList;
        notifyDataSetChanged();
    }

}

class MyViewHolder extends RecyclerView.ViewHolder {

    //Uso de la card view item
    ImageView imgVista;
    TextView txtFechaVista, txtObservacionVista, txtTemperaturaVista, txtHumedadVista, txtLuminosidadVista,
            txtVentilacionVista, txtRiegoVista, txtHoraVista;
    CardView recCard;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        imgVista = itemView.findViewById(R.id.imgVista);
        txtFechaVista = itemView.findViewById(R.id.txtFechaVista);
        txtObservacionVista = itemView.findViewById(R.id.txtObservacionVista);
        txtTemperaturaVista = itemView.findViewById(R.id.txtTemperaturaVista);
        txtHumedadVista = itemView.findViewById(R.id.txtHumedadVista);
        txtLuminosidadVista = itemView.findViewById(R.id.txtLuminosidadVista);
        txtVentilacionVista = itemView.findViewById(R.id.txtVentilacionVista);
        txtRiegoVista = itemView.findViewById(R.id.txtRiegoVista);
        txtHoraVista = itemView.findViewById(R.id.txtHoraVista);
        recCard = itemView.findViewById(R.id.recCard);
    }
}
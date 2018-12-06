package com.sismossv.sismossv.fragmentos.sismos_helper;


import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sismossv.sismossv.R;
import com.sismossv.sismossv.mapa.Mapa;

import java.util.List;



public class SismoAdapter extends RecyclerView.Adapter<SismoAdapter.SismoViewHolder> {

    private List<Sismo> items;

    public static class SismoViewHolder extends RecyclerView.ViewHolder {
        public CardView card;
        public ImageView imagen;
        public TextView fecha;
        public TextView magnitud;
        public TextView ubicacion;

        public SismoViewHolder(View v) {
            super(v);
            imagen = (ImageView) v.findViewById(R.id.imgItem);
            fecha = (TextView) v.findViewById(R.id.txtTitulo);
            magnitud = (TextView) v.findViewById(R.id.txtMagnitud);
            ubicacion = (TextView) v.findViewById(R.id.txtUbicacion);
            card = (CardView) v.findViewById(R.id.sismo_cardview);
        }
    }

    public SismoAdapter(List<Sismo> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public SismoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cardview_sismo, viewGroup, false);
        return new SismoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final SismoViewHolder viewHolder, final int i) {
        viewHolder.imagen.setImageResource(R.drawable.warning);
        viewHolder.fecha.setText("Fecha: "+FechaUtil.getTextoDesdeFechaHora(items.get(i).getFecha_hora()));
        viewHolder.magnitud.setText("Magnitud: "+items.get(i).getMagnitud()+" Richter");
        viewHolder.ubicacion.setText("Ubicación: " + items.get(i).getUbicacion());
        viewHolder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(viewHolder.itemView.getContext(), Mapa.class);
                myIntent.putExtra("lat", items.get(i).getLatitud());
                myIntent.putExtra("lon", items.get(i).getLongitud());
                myIntent.putExtra("ubicacion", items.get(i).getUbicacion());
                viewHolder.itemView.getContext().startActivity(myIntent);
            }
        });
    }
}
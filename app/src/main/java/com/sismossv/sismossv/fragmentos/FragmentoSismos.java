package com.sismossv.sismossv.fragmentos;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.sismossv.sismossv.R;
import com.sismossv.sismossv.fragmentos.sismos_helper.FechaUtil;
import com.sismossv.sismossv.fragmentos.sismos_helper.Sismo;
import com.sismossv.sismossv.fragmentos.sismos_helper.SismoAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FragmentoSismos extends Fragment {

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;

    private List<Sismo> listado = new ArrayList<>();

    public static FragmentoSismos newInstance() {
        FragmentoSismos fragment = new FragmentoSismos();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void realizarPeticion(final Context context) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String mJSONURLString = "http://sismossv.atspace.cc/";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                mJSONURLString,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            if (response.length() > 0) {
                                int tam = response.length();
                                for (int i = 0; i < tam; i++) {

                                    Sismo sismo = new Sismo();
                                    JSONObject respuesta = response.getJSONObject(i);
                                    Date fecha_hora = FechaUtil.getFechaHoraDesdeTexto(respuesta.getString("fecha_hora"));
                                    double latitud = respuesta.getDouble("latitud");
                                    double longitud = respuesta.getDouble("longitud");
                                    String ubicacion = respuesta.getString("ubicacion");
                                    double profundidad = respuesta.getDouble("profundidad");
                                    double magnitud = respuesta.getDouble("magnitud");

                                    sismo.setFecha_hora(fecha_hora);
                                    sismo.setLatitud(latitud);
                                    sismo.setLongitud(longitud);
                                    sismo.setUbicacion(ubicacion);
                                    sismo.setMagnitud(magnitud);
                                    sismo.setProfundidad(profundidad);
                                    sismo.setImg(R.drawable.warning);

                                    listado.add(sismo);
                                }
                            }
                            adapter = new SismoAdapter(listado);
                            recycler.setAdapter(adapter);
                        } catch(Exception ex) {
                           // Toast.makeText(context, "ERROR procesando la respuesta.", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                       // Toast.makeText(context, "ERROR en la petición.", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_sismos, container, false);

        recycler = (RecyclerView) root.findViewById(R.id.reciclador);
        recycler.setHasFixedSize(true);

        lManager = new LinearLayoutManager(root.getContext());
        recycler.setLayoutManager(lManager);

        adapter = new SismoAdapter(listado);
        recycler.setAdapter(adapter);

        realizarPeticion(root.getContext());

        return root;
    }
}
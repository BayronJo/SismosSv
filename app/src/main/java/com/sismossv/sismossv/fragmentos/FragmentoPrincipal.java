package com.sismossv.sismossv.fragmentos;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FragmentoPrincipal extends Fragment {

    private TextView txtPrinFecha, txtPrinMagnitud, txtPrinUbicacion, txtPrinProfundidad, txtCoordenadas;
    private List<Sismo> listado = new ArrayList<>();

    public static FragmentoPrincipal newInstance() {
        FragmentoPrincipal fragment = new FragmentoPrincipal();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_principal, container, false);

        this.txtPrinFecha = root.findViewById(R.id.txtPrinFecha);
        this.txtPrinMagnitud = root.findViewById(R.id.txtPrinMagnitud);
        this.txtPrinProfundidad = root.findViewById(R.id.txtPrinProfundidad);
        this.txtPrinUbicacion = root.findViewById(R.id.txtPrinUbicacion);
        this.txtCoordenadas = root.findViewById(R.id.txtPrinCoordenadas);

        realizarPeticion(root.getContext());

        return root;
    }

    private void realizarPeticion(final Context context) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);//Crea una instancia de volley al servidor

        String mJSONURLString = "http://sismossv.atspace.cc/servicio.php";

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
                            if(listado.size()>0) {
                                txtPrinFecha.setText("Fecha: " + FechaUtil.getTextoDesdeFechaHora(listado.get(0).getFecha_hora()));
                                txtPrinUbicacion.setText("Ubicación: " + listado.get(0).getUbicacion());
                                txtPrinProfundidad.setText("Profundidad: " + listado.get(0).getProfundidad() + " Km");
                                txtPrinMagnitud.setText("Magnitud: " + listado.get(0).getMagnitud() + " Richter");
                                txtCoordenadas.setText("Coordenadas: " + listado.get(0).getLatitud() + " y " + listado.get(0).getLongitud());
                            }
                        } catch(Exception ex) {
                            //Toast.makeText(context, "ERROR procesando la respuesta.", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                      //  Toast.makeText(context, "ERROR en la petición.", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        requestQueue.add(jsonArrayRequest);
    }
}
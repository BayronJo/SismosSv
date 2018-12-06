package com.sismossv.sismossv.alertas;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
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
import com.sismossv.sismossv.mapa.Mapa;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MyService extends Service {

    private Timer timer;
    private TimerTask timerTask;
    final Handler handler = new Handler();
    int vecesDia = 0;
    int vecesAviso = 0;

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();

    }

    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, after the first 5000ms the TimerTask will run every 10000
        timer.schedule(timerTask, 1000, 3000); //
    }

    private void realizarPeticion(final Context context) {
        final List<Sismo> listado = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String mJSONURLString = "http://sismossv.atspace.cc/servicio.php";

        Calendar cal = Calendar.getInstance();
        final int hora = cal.get(Calendar. HOUR_OF_DAY);
        final int minutos = cal.get(Calendar.MINUTE);
        final int segundos = cal.get(Calendar.SECOND);
        final int anio = cal.get(Calendar.YEAR);
        final int mes = cal.get(Calendar.MONTH);
        final int dia = cal.get(Calendar.DAY_OF_MONTH);

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
                            //Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                            for(int i = 0; i<listado.size(); i++) {
                                Calendar cal_aux = Calendar.getInstance();
                                cal_aux.setTime(listado.get(i).getFecha_hora());
                                int hora_aux = cal_aux.get(Calendar. HOUR_OF_DAY);
                                int minutos_aux = cal_aux.get(Calendar.MINUTE);
                                int segundos_aux = cal_aux.get(Calendar.SECOND);
                                int anio_aux = cal_aux.get(Calendar.YEAR);
                                int mes_aux = cal_aux.get(Calendar.MONTH);
                                int dia_aux = cal_aux.get(Calendar.DAY_OF_MONTH);
                                if(dia == dia_aux && mes == mes_aux && anio == anio_aux && hora == hora_aux && minutos == minutos_aux ) {
                                    NotificationCompat.Builder mBuilder;
                                    NotificationManager mNotifyMgr = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
                                    int icono = R.drawable.alerta;
                                    Intent intent = new Intent(getApplicationContext(), Mapa.class);
                                    intent.putExtra("lat", listado.get(i).getLatitud());
                                    intent.putExtra("lon", listado.get(i).getLongitud());
                                    intent.putExtra("ubicacion", listado.get(i).getUbicacion());
                                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

                                    mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
                                            .setContentIntent(pendingIntent)
                                            .setSmallIcon(icono)
                                            .setContentTitle("ALERTA! Sismo de " + listado.get(i).getMagnitud() + " Richter.")
                                            .setContentText("En " + listado.get(i).getUbicacion() + "!")
                                           // .setVibrate(new long[]{100, 250, 100, 500})
                                            .setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+ "://" +getApplicationContext().getPackageName()+"/"+R.raw.sonidoalerta))
                                           // .setSound(soundUri)
                                            .setAutoCancel(true);
                                    mNotifyMgr.notify(1, mBuilder.build());
                                }
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

    public void initializeTimerTask() {

        timerTask = new TimerTask() {

            public void run() {

                handler.post(new Runnable() {

                    public void run() {
                        realizarPeticion(getApplicationContext());
                    }
                });

            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Toast.makeText(this, "Start!", Toast.LENGTH_LONG).show();

        startTimer();

        return START_STICKY;
    }


    public void onDestroy() {
        super.onDestroy();
    }

}

package com.sismossv.sismossv.fragmentos.sismos_helper;

import java.util.Calendar;
import java.util.Date;

public class FechaUtil {

    public static Date getFechaHoraDesdeTexto(String texto) {
        Date fecha_hora = null;

        Calendar calendar = Calendar.getInstance();

        String fecha_aux = texto.split(" ")[0];
        String hora_aux = texto.split(" ")[1];

        int dia = Integer.parseInt(fecha_aux.split("-")[2]);
        int mes = Integer.parseInt(fecha_aux.split("-")[1]);
        int anio = Integer.parseInt(fecha_aux.split("-")[0]);

        int hora = Integer.parseInt(hora_aux.split(":")[0]);
        int minuto = Integer.parseInt(hora_aux.split(":")[1]);
        int segundo = Integer.parseInt(hora_aux.split(":")[2]);

        calendar.set(Calendar.YEAR, anio);
        calendar.set(Calendar.MONTH, mes-1);
        calendar.set(Calendar.DAY_OF_MONTH, dia);
        calendar.set(Calendar.HOUR_OF_DAY, hora);
        calendar.set(Calendar.MINUTE, minuto);
        calendar.set(Calendar.SECOND, segundo);
        calendar.set(Calendar.MILLISECOND, 0);

        fecha_hora = calendar.getTime();

        return fecha_hora;
    }

    public static String getTextoDesdeFechaHora(Date fechahora) {
        String fecha_hora = null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(fechahora);
        fecha_hora = cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.YEAR)
                + " - " + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE)+":"+cal.get(Calendar.SECOND);
        return fecha_hora;
    }

}

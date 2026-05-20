package org.fernandoblanco.inglesbasico.data

import java.util.Calendar
import java.util.Locale

fun diaClaveDesde(cal: Calendar): Int =
    cal.get(Calendar.DAY_OF_YEAR) + cal.get(Calendar.YEAR) * 1000

fun diaClaveActual(): Int = diaClaveDesde(Calendar.getInstance())

/** Redondeo al minuto más cercano para mostrar en UI. */
fun segundosAMinutosDisplay(segundos: Long): Long = (segundos + 30) / 60

fun formatearDuracionDesdeSegundos(segundos: Long): String {
    val minutos = segundosAMinutosDisplay(segundos)
    val h = minutos / 60
    val m = minutos % 60
    return if (h > 0) "${h}h ${m}min" else "${m}min"
}

fun etiquetaDiaCorta(diaClave: Int): String {
    val cal = Calendar.getInstance()
    val dia = diaClave % 1000
    val anio = diaClave / 1000
    cal.set(Calendar.YEAR, anio)
    cal.set(Calendar.DAY_OF_YEAR, dia)
    return java.text.SimpleDateFormat("EEE", Locale("es", "MX")).format(cal.time)
}

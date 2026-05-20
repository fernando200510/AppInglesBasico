package org.fernandoblanco.inglesbasico.data

import org.fernandoblanco.inglesbasico.db.entity.HistorialActividadEntity
import org.fernandoblanco.inglesbasico.db.entity.NinoEntity
import org.fernandoblanco.inglesbasico.db.entity.UsoDiarioEntity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/** Las 5 actividades del menú de la app. */
enum class TipoActividadReporte(val emoji: String, val tituloParental: String) {
    VOCABULARIO("📖", "Aprende palabras"),
    IMAGEN("🖼️", "Elegir imagen correcta"),
    AUDIO("👂", "Escuchar audio y responder"),
    PALABRAS("✏️", "Completar palabras"),
    CHAT("🤖", "Conversar con la IA");

    companion object {
        fun fromDb(tipo: String): TipoActividadReporte? =
            entries.find { it.name == tipo }
    }
}

data class ParentActividadCheckUi(
    val tipo: TipoActividadReporte,
    val habilitada: Boolean,
    val conProgreso: Boolean,
    val partidas: Int,
    val aciertos: Int
)

data class ParentProgresoUi(
    val nivel: Int,
    val estrellas: Int,
    val progresoNivelPct: Int,
    val completadas: Int,
    val totalHabilitadas: Int,
    val porcentaje: Int,
    val checklist: List<ParentActividadCheckUi>
)

data class ParentDiaTiempoUi(
    val etiqueta: String,
    val minutos: Long
)

data class ParentTiempoUi(
    val minutosHoy: Long,
    val limiteDiario: Int,
    val minutosSesion: Long,
    val minutosTotal: Long,
    val porcentajeLimite: Float,
    val minutosPorDia: List<ParentDiaTiempoUi> = emptyList()
)

data class ParentChartBar(
    val tipo: TipoActividadReporte,
    val valor: Float,
    val maximo: Float,
    val etiqueta: String
)

data class ParentChartSlice(
    val tipo: TipoActividadReporte,
    val valor: Float
)

enum class ParentHistorialEstado { EXITO, PARCIAL, INTENTO }

data class ParentHistorialItem(
    val tipo: TipoActividadReporte,
    val fechaHoraMillis: Long,
    val estado: ParentHistorialEstado,
    val detalle: String
)

data class ParentReportDashboard(
    val progreso: ParentProgresoUi,
    val puntaje: Int,
    val tiempo: ParentTiempoUi,
    val barrasAcierto: List<ParentChartBar>,
    val distribucion: List<ParentChartSlice>,
    val historial: List<ParentHistorialItem>
)

fun construirDashboard(
    n: NinoEntity,
    eventos: List<HistorialActividadEntity>,
    diasUso: List<UsoDiarioEntity> = emptyList()
): ParentReportDashboard {
    val progreso = progresoActividadesParental(n, eventos)
    val tiempo = tiempoUsoParental(n, diasUso)
    return ParentReportDashboard(
        progreso = progreso,
        puntaje = n.puntajeTotal,
        tiempo = tiempo,
        barrasAcierto = barrasAciertoPorActividad(n, eventos),
        distribucion = distribucionPartidas(n),
        historial = historialParental(n, eventos)
    )
}

fun progresoActividadesParental(
    n: NinoEntity,
    eventos: List<HistorialActividadEntity> = emptyList()
): ParentProgresoUi {
    val checks = listOf(
        checkVocabulario(n),
        checkConHistorial(TipoActividadReporte.IMAGEN, n.actividadImagenHabilitada, n.partidasImagen > 0, n, eventos),
        checkConHistorial(TipoActividadReporte.AUDIO, n.actividadAudioHabilitada, n.partidasAudio > 0, n, eventos),
        checkConHistorial(TipoActividadReporte.PALABRAS, n.actividadPalabrasHabilitada, n.partidasPalabras > 0, n, eventos),
        checkConHistorial(TipoActividadReporte.CHAT, n.actividadChatHabilitada, n.partidasChat > 0, n, eventos)
    )
    val habilitadas = checks.count { it.habilitada }
    val completadas = checks.count { it.habilitada && it.conProgreso }
    val pct = if (habilitadas == 0) 0 else ((completadas * 100f) / habilitadas).toInt()
    val enNivel = (n.puntajeTotal % 50) / 50f
    return ParentProgresoUi(
        nivel = n.nivel,
        estrellas = estrellasDeNivel(n.puntajeTotal),
        progresoNivelPct = (enNivel * 100).toInt(),
        completadas = completadas,
        totalHabilitadas = habilitadas,
        porcentaje = pct,
        checklist = checks
    )
}

private fun check(
    tipo: TipoActividadReporte,
    habilitada: Boolean,
    conProgreso: Boolean,
    partidas: Int,
    aciertos: Int
) = ParentActividadCheckUi(tipo, habilitada, conProgreso, partidas, aciertos)

private fun checkVocabulario(n: NinoEntity) = ParentActividadCheckUi(
    tipo = TipoActividadReporte.VOCABULARIO,
    habilitada = n.actividadVocabularioHabilitada,
    conProgreso = n.sesionesVocabulario > 0,
    partidas = n.sesionesVocabulario,
    aciertos = n.tarjetasVocabulario
)

private fun checkConHistorial(
    tipo: TipoActividadReporte,
    habilitada: Boolean,
    conProgreso: Boolean,
    n: NinoEntity,
    eventos: List<HistorialActividadEntity>
): ParentActividadCheckUi {
    val (intentos, aciertos) = intentosYAciertos(n, eventos, tipo)
    return check(tipo, habilitada, conProgreso, intentos, aciertos)
}

fun barrasAciertoPorActividad(
    n: NinoEntity,
    eventos: List<HistorialActividadEntity>
): List<ParentChartBar> =
    TipoActividadReporte.entries.mapNotNull { tipo ->
        if (!estaHabilitada(n, tipo)) return@mapNotNull null
        val (intentos, aciertos) = intentosYAciertos(n, eventos, tipo)
        if (intentos == 0) return@mapNotNull null
        val pct = (aciertos.toFloat() / intentos) * 100f
        ParentChartBar(
            tipo = tipo,
            valor = pct.coerceIn(0f, 100f),
            maximo = 100f,
            etiqueta = "$aciertos/$intentos (${pct.toInt()}%)"
        )
    }

private fun intentosYAciertos(
    n: NinoEntity,
    eventos: List<HistorialActividadEntity>,
    tipo: TipoActividadReporte
): Pair<Int, Int> {
    val delHistorial = eventos.filter { TipoActividadReporte.fromDb(it.tipo) == tipo }
    if (delHistorial.isNotEmpty()) {
        return delHistorial.size to delHistorial.count { it.exito }
    }
    return when (tipo) {
        TipoActividadReporte.VOCABULARIO -> n.sesionesVocabulario to n.sesionesVocabulario
        else -> contadores(n, tipo)
    }
}

fun distribucionPartidas(n: NinoEntity): List<ParentChartSlice> =
    TipoActividadReporte.entries.mapNotNull { tipo ->
        if (!estaHabilitada(n, tipo)) return@mapNotNull null
        val (partidas, _) = contadores(n, tipo)
        if (partidas <= 0) return@mapNotNull null
        ParentChartSlice(tipo, partidas.toFloat())
    }

private fun contadores(n: NinoEntity, tipo: TipoActividadReporte): Pair<Int, Int> = when (tipo) {
    TipoActividadReporte.VOCABULARIO -> n.sesionesVocabulario to n.sesionesVocabulario
    TipoActividadReporte.IMAGEN -> n.partidasImagen to n.aciertosImagen
    TipoActividadReporte.AUDIO -> n.partidasAudio to n.aciertosAudio
    TipoActividadReporte.PALABRAS -> n.partidasPalabras to n.aciertosPalabras
    TipoActividadReporte.CHAT -> n.partidasChat to n.aciertosChat
}

private fun estaHabilitada(n: NinoEntity, tipo: TipoActividadReporte): Boolean = when (tipo) {
    TipoActividadReporte.VOCABULARIO -> n.actividadVocabularioHabilitada
    TipoActividadReporte.IMAGEN -> n.actividadImagenHabilitada
    TipoActividadReporte.AUDIO -> n.actividadAudioHabilitada
    TipoActividadReporte.PALABRAS -> n.actividadPalabrasHabilitada
    TipoActividadReporte.CHAT -> n.actividadChatHabilitada
}

fun tiempoUsoParental(n: NinoEntity, diasUso: List<UsoDiarioEntity> = emptyList()): ParentTiempoUi {
    val diaHoy = diaClaveActual()
    val segundosHoy = if (n.ultimoDiaUso == diaHoy) n.segundosUsoHoy else 0L
    val minutosHoy = segundosAMinutosDisplay(segundosHoy)
    val minutosSesion = segundosAMinutosDisplay(n.segundosSesionActual)
    val minutosTotal = segundosAMinutosDisplay(n.tiempoUsoTotalSegundos)
    val limite = n.limiteUsoDiarioMinutos.coerceAtLeast(1)
    val ratio = (minutosHoy.toFloat() / limite).coerceIn(0f, 1.2f)
    return ParentTiempoUi(
        minutosHoy = minutosHoy,
        limiteDiario = limite,
        minutosSesion = minutosSesion,
        minutosTotal = minutosTotal,
        porcentajeLimite = ratio,
        minutosPorDia = ultimos7DiasTiempo(n, diasUso)
    )
}

private fun ultimos7DiasTiempo(n: NinoEntity, diasUso: List<UsoDiarioEntity>): List<ParentDiaTiempoUi> {
    val mapa = diasUso.associate { it.diaClave to it.segundos }
    val diaHoy = diaClaveActual()
    val hoy = Calendar.getInstance()
    return (6 downTo 0).map { diasAtras ->
        val cal = (hoy.clone() as Calendar).apply { add(Calendar.DAY_OF_YEAR, -diasAtras) }
        val clave = diaClaveDesde(cal)
        val segundos = when (clave) {
            diaHoy -> n.segundosUsoHoy
            else -> mapa[clave] ?: 0L
        }
        ParentDiaTiempoUi(
            etiqueta = etiquetaDiaCorta(clave),
            minutos = segundosAMinutosDisplay(segundos)
        )
    }
}

fun historialParental(n: NinoEntity, eventos: List<HistorialActividadEntity>): List<ParentHistorialItem> {
    if (eventos.isNotEmpty()) {
        return eventos.mapNotNull { ev ->
            val tipo = TipoActividadReporte.fromDb(ev.tipo) ?: return@mapNotNull null
            ParentHistorialItem(
                tipo = tipo,
                fechaHoraMillis = ev.timestamp,
                estado = if (ev.exito) ParentHistorialEstado.EXITO else ParentHistorialEstado.INTENTO,
                detalle = ev.detalle.ifBlank {
                    if (ev.exito) "Completada con éxito" else "Intento registrado"
                }
            )
        }
    }
    return historialDesdeAgregados(n)
}

private fun historialDesdeAgregados(n: NinoEntity): List<ParentHistorialItem> = buildList {
    TipoActividadReporte.entries.forEach { tipo ->
        val (intentos, aciertos) = intentosYAciertos(n, emptyList(), tipo)
        if (intentos > 0) add(entradaAgregada(n, tipo, intentos, aciertos))
    }
}.sortedByDescending { it.fechaHoraMillis }

private fun entradaAgregada(
    n: NinoEntity,
    tipo: TipoActividadReporte,
    partidas: Int,
    aciertos: Int
): ParentHistorialItem {
    val ratio = if (partidas == 0) 0f else aciertos.toFloat() / partidas
    val estado = when {
        ratio >= 0.55f -> ParentHistorialEstado.EXITO
        ratio > 0f -> ParentHistorialEstado.PARCIAL
        else -> ParentHistorialEstado.INTENTO
    }
    return ParentHistorialItem(
        tipo = tipo,
        fechaHoraMillis = n.ultimaActividad.takeIf { it > 0 } ?: n.creadoEn,
        estado = estado,
        detalle = "Resumen · $aciertos/$partidas aciertos"
    )
}

fun estrellasDeNivel(puntajeTotal: Int): Int {
    val enNivel = (puntajeTotal % 50) / 50f
    return when {
        enNivel >= 0.66f -> 3
        enNivel >= 0.33f -> 2
        enNivel > 0f -> 1
        else -> 0
    }
}

fun esMismoDia(timestamp: Long): Boolean {
    if (timestamp == 0L) return false
    val cal = Calendar.getInstance()
    val hoyDia = cal.get(Calendar.DAY_OF_YEAR)
    val hoyAnio = cal.get(Calendar.YEAR)
    cal.timeInMillis = timestamp
    return cal.get(Calendar.DAY_OF_YEAR) == hoyDia && cal.get(Calendar.YEAR) == hoyAnio
}

fun formatearUltimaActividad(timestamp: Long): String {
    if (timestamp == 0L) return "Aún sin actividad"
    val fmtHora = SimpleDateFormat("HH:mm", Locale.getDefault())
    return when {
        esMismoDia(timestamp) -> "hoy ${fmtHora.format(Date(timestamp))}"
        esAyer(timestamp) -> "ayer ${fmtHora.format(Date(timestamp))}"
        else -> SimpleDateFormat("d MMM yyyy · HH:mm", Locale.getDefault()).format(Date(timestamp))
    }
}

private fun esAyer(timestamp: Long): Boolean {
    val cal = Calendar.getInstance()
    cal.add(Calendar.DAY_OF_YEAR, -1)
    val ayerDia = cal.get(Calendar.DAY_OF_YEAR)
    val ayerAnio = cal.get(Calendar.YEAR)
    cal.timeInMillis = timestamp
    return cal.get(Calendar.DAY_OF_YEAR) == ayerDia && cal.get(Calendar.YEAR) == ayerAnio
}

fun formatearFechaHistorial(timestamp: Long): String =
    SimpleDateFormat("d MMM yyyy · HH:mm", Locale.getDefault()).format(Date(timestamp))

@Deprecated("Usar formatearDuracionDesdeSegundos", ReplaceWith("formatearDuracionDesdeSegundos(segundos)"))
fun formatearDuracionTotal(minutos: Long): String {
    val h = minutos / 60
    val m = minutos % 60
    return if (h > 0) "${h}h ${m}min" else "${m}min"
}

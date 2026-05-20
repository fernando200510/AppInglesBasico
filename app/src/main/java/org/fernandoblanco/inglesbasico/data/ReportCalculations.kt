package org.fernandoblanco.inglesbasico.data

import org.fernandoblanco.inglesbasico.db.entity.HistorialActividadEntity
import org.fernandoblanco.inglesbasico.db.entity.NinoEntity
import org.fernandoblanco.inglesbasico.db.entity.UsoDiarioEntity
import java.util.Calendar
import kotlin.math.max
import kotlin.math.roundToInt

/** Estado agregado del módulo de reportes, exclusivo del niño activo. */
data class ReportesUiState(
    val ninoId: Long,
    val nombre: String,
    val avatarEmoji: String,
    val nivel: Int,
    val tituloNivel: String,
    val puntajeTotal: Int,
    val xpEnNivel: Int,
    val xpParaSiguiente: Int,
    val progresoNivel: Float,
    /** Media de la semana en curso: total minutos / días con uso > 0 (no división entera por 7). */
    val mediaDiariaTexto: String,
    val minutosSemanaTotal: Long,
    val diasActivosSemana: Int,
    val cambioSemanalPct: Int?,
    val cambioSemanalSubio: Boolean,
    val barrasSemana: List<ReporteDiaBarra>,
    val minutosSesionActual: Long,
    val historial: List<ReporteHistorialItem>
)

data class ReporteDiaBarra(
    val etiqueta: String,
    /** Minutos totales de uso ese día (misma fuente que la suma de sesiones del día). */
    val minutos: Long,
    val esHoy: Boolean,
    val diaClave: Int
)

/**
 * Sesión de actividad para reportes: duración explícita en minutos (R20 / gráfico R21).
 * Los minutos por día del gráfico = suma de [duracionMinutos] de todas las sesiones de ese día.
 */
data class ReporteHistorialItem(
    val tipo: TipoActividadReporte,
    val titulo: String,
    val duracionMinutos: Int,
    val puntos: Int,
    val timestamp: Long,
    /** Día calendario al que pertenece la sesión (misma clave que [ReporteDiaBarra.diaClave]). */
    val diaClave: Int
)

private const val XP_POR_NIVEL = 50
private const val GAP_SESION_MS = 30 * 60 * 1000L

private val ETIQUETAS_DIA_SEMANA = listOf("D", "L", "M", "M", "J", "V", "S")

private data class DiaVentana(
    val diaClave: Int,
    val etiqueta: String,
    val esHoy: Boolean,
    val minutosOficiales: Long
)

private data class SesionBruta(
    val eventos: List<HistorialActividadEntity>,
    val diaClave: Int,
    val tipo: TipoActividadReporte,
    val puntos: Int,
    val timestampFin: Long,
    val minutosBrutos: Int
)

fun construirReportesUiState(
    nino: NinoEntity,
    eventos: List<HistorialActividadEntity>,
    diasUso: List<UsoDiarioEntity>
): ReportesUiState {
    val xpEnNivel = nino.puntajeTotal % XP_POR_NIVEL
    val mapaUso = diasUso.associate { it.diaClave to it.segundos }
    val diaHoy = diaClaveActual()

    val ventana = ventanaSieteDias(nino, mapaUso, diaHoy)

    val sesionesBrutas = if (eventos.isNotEmpty()) {
        construirSesionesBrutas(eventos)
    } else {
        sesionesBrutasDesdeAgregados(nino)
    }

    val porDia = sesionesBrutas.groupBy { it.diaClave }
    val sesionesNormalizadas = mutableListOf<ReporteHistorialItem>()

    for (dia in ventana) {
        val lista = porDia[dia.diaClave].orEmpty()
        val sumaBruta = lista.sumOf { it.minutosBrutos.toLong() }.coerceAtLeast(0L)
        val oficial = dia.minutosOficiales
        val objetivoBarra = max(oficial, sumaBruta)
        val asignados = repartirMinutosExacto(
            pesos = lista.map { it.minutosBrutos },
            objetivo = objetivoBarra.toInt().coerceAtLeast(0)
        )
        lista.forEachIndexed { idx, s ->
            val tipo = s.tipo
            val minAsig = asignados.getOrElse(idx) { 0 }
            if (minAsig <= 0) return@forEachIndexed
            sesionesNormalizadas.add(
                ReporteHistorialItem(
                    tipo = tipo,
                    titulo = tipo.tituloParental,
                    duracionMinutos = minAsig,
                    puntos = s.puntos,
                    timestamp = s.timestampFin,
                    diaClave = dia.diaClave
                )
            )
        }
    }

    val barras = ventana.map { d ->
        val sumaSesionesDia = sesionesNormalizadas
            .filter { it.diaClave == d.diaClave }
            .sumOf { it.duracionMinutos }
            .toLong()
        val minutosBarra = if (sumaSesionesDia > 0L) {
            sumaSesionesDia
        } else {
            d.minutosOficiales
        }
        ReporteDiaBarra(
            etiqueta = d.etiqueta,
            minutos = minutosBarra,
            esHoy = d.esHoy,
            diaClave = d.diaClave
        )
    }

    val totalSemana = barras.sumOf { it.minutos }
    val diasActivos = barras.count { it.minutos > 0 }
    val promedioMinutos = if (diasActivos > 0) {
        (totalSemana.toDouble() / diasActivos).roundToInt().coerceAtLeast(0)
    } else {
        0
    }
    val mediaDiariaTexto = formatearDuracionDesdeSegundos(promedioMinutos * 60L)

    val (cambioPct, subio) = cambioRespectoSemanaAnterior(nino, mapaUso, diaHoy)

    val historialOrdenado = sesionesNormalizadas
        .sortedWith(
            compareByDescending<ReporteHistorialItem> { it.timestamp }
                .thenByDescending { it.puntos }
                .thenBy { it.titulo }
        )
        .take(40)

    return ReportesUiState(
        ninoId = nino.id,
        nombre = nino.nombreMostrar,
        avatarEmoji = nino.avatarEmoji,
        nivel = nino.nivel,
        tituloNivel = tituloDeNivel(nino.nivel),
        puntajeTotal = nino.puntajeTotal,
        xpEnNivel = xpEnNivel,
        xpParaSiguiente = XP_POR_NIVEL,
        progresoNivel = xpEnNivel / XP_POR_NIVEL.toFloat(),
        mediaDiariaTexto = mediaDiariaTexto,
        minutosSemanaTotal = totalSemana,
        diasActivosSemana = diasActivos,
        cambioSemanalPct = cambioPct,
        cambioSemanalSubio = subio,
        barrasSemana = barras,
        minutosSesionActual = segundosAMinutosDisplay(nino.segundosSesionActual),
        historial = historialOrdenado
    )
}

/** Reparte [objetivo] minutos enteros en partes proporcionales a [pesos]; suma resultados = objetivo. */
private fun repartirMinutosExacto(pesos: List<Int>, objetivo: Int): List<Int> {
    if (pesos.isEmpty()) return emptyList()
    if (objetivo <= 0) return List(pesos.size) { 0 }
    val n = pesos.size
    val sumaPesos = pesos.sum().coerceAtLeast(0)
    if (sumaPesos == 0) {
        val base = objetivo / n
        val resto = objetivo % n
        return List(n) { i -> base + if (i < resto) 1 else 0 }
    }
    val primero = pesos.map { p ->
        ((p.toLong() * objetivo) / sumaPesos).toInt().coerceAtLeast(0)
    }.toMutableList()
    var diff = objetivo - primero.sum()
    var guard = 0
    while (diff > 0) {
        val idx = guard % n
        primero[idx]++
        diff--
        guard++
        if (guard > 1_000_000) break
    }
    guard = 0
    while (diff < 0) {
        val idx = primero.indexOfLast { it > 0 }
        if (idx < 0) break
        primero[idx]--
        diff++
        guard++
        if (guard > 1_000_000) break
    }
    return primero
}

private fun ventanaSieteDias(
    n: NinoEntity,
    mapa: Map<Int, Long>,
    diaHoy: Int
): List<DiaVentana> {
    val hoy = Calendar.getInstance()
    return (6 downTo 0).map { diasAtras ->
        val cal = (hoy.clone() as Calendar).apply { add(Calendar.DAY_OF_YEAR, -diasAtras) }
        val clave = diaClaveDesde(cal)
        val segundos = when (clave) {
            diaHoy -> n.segundosUsoHoy
            else -> mapa[clave] ?: 0L
        }
        DiaVentana(
            diaClave = clave,
            etiqueta = etiquetaLetraDia(cal),
            esHoy = diasAtras == 0,
            minutosOficiales = segundosAMinutosDisplay(segundos)
        )
    }
}

private fun construirSesionesBrutas(ordenadosInput: List<HistorialActividadEntity>): List<SesionBruta> {
    val ordenados = ordenadosInput.sortedBy { it.timestamp }
    if (ordenados.isEmpty()) return emptyList()
    val grupos = agruparEnSesiones(ordenados)
    return grupos.mapNotNull { sesion ->
        val primero = sesion.first()
        val ultimo = sesion.last()
        val tipo = TipoActividadReporte.fromDb(primero.tipo) ?: return@mapNotNull null
        val puntos = sesion.sumOf { extraerPuntos(it.detalle, it.exito) }
        if (puntos <= 0) return@mapNotNull null
        val cal = Calendar.getInstance().apply { timeInMillis = primero.timestamp }
        val diaClave = diaClaveDesde(cal)
        val deltaSeg = ((ultimo.timestamp - primero.timestamp) / 1000L).coerceAtLeast(0L)
        val minutosPorDuracion = segundosAMinutosDisplay(deltaSeg).toInt().coerceAtLeast(0)
        val minutosBrutos = max(1, minutosPorDuracion)
        SesionBruta(
            eventos = sesion,
            diaClave = diaClave,
            tipo = tipo,
            puntos = puntos,
            timestampFin = ultimo.timestamp,
            minutosBrutos = minutosBrutos
        )
    }
}

private fun sesionesBrutasDesdeAgregados(n: NinoEntity): List<SesionBruta> {
    val lista = mutableListOf<SesionBruta>()
    val ts = n.ultimaActividad.takeIf { it > 0 } ?: n.creadoEn
    val cal = Calendar.getInstance().apply { timeInMillis = ts }
    val diaClave = diaClaveDesde(cal)
    TipoActividadReporte.entries.forEach { tipo ->
        val intentos = when (tipo) {
            TipoActividadReporte.VOCABULARIO -> n.sesionesVocabulario
            TipoActividadReporte.IMAGEN -> n.partidasImagen
            TipoActividadReporte.AUDIO -> n.partidasAudio
            TipoActividadReporte.PALABRAS -> n.partidasPalabras
            TipoActividadReporte.CHAT -> n.partidasChat
        }
        if (intentos <= 0) return@forEach
        val puntos = intentos * 10
        lista.add(
            SesionBruta(
                eventos = emptyList(),
                diaClave = diaClave,
                tipo = tipo,
                puntos = puntos,
                timestampFin = ts,
                minutosBrutos = max(1, intentos * 2)
            )
        )
    }
    return lista
}

fun tituloDeNivel(nivel: Int): String = when (nivel) {
    1 -> "Principiante"
    2 -> "Explorador"
    3 -> "Aprendiz"
    4 -> "Aprendiz Avanzado"
    5 -> "Estudiante"
    6 -> "Experto"
    else -> "Maestro"
}

fun barrasTiempoSemana(n: NinoEntity, diasUso: List<UsoDiarioEntity>): List<ReporteDiaBarra> {
    val mapa = diasUso.associate { it.diaClave to it.segundos }
    val diaHoy = diaClaveActual()
    return ventanaSieteDias(n, mapa, diaHoy).map {
        ReporteDiaBarra(
            etiqueta = it.etiqueta,
            minutos = it.minutosOficiales,
            esHoy = it.esHoy,
            diaClave = it.diaClave
        )
    }
}

private fun etiquetaLetraDia(cal: Calendar): String {
    val indice = cal.get(Calendar.DAY_OF_WEEK) - 1
    return ETIQUETAS_DIA_SEMANA.getOrElse(indice) { "?" }
}

private fun cambioRespectoSemanaAnterior(
    n: NinoEntity,
    mapa: Map<Int, Long>,
    diaHoy: Int
): Pair<Int?, Boolean> {
    val hoy = Calendar.getInstance()

    fun minutosOficialEn(diasAtras: Int): Long {
        val cal = (hoy.clone() as Calendar).apply { add(Calendar.DAY_OF_YEAR, -diasAtras) }
        val clave = diaClaveDesde(cal)
        val segundos = when (clave) {
            diaHoy -> n.segundosUsoHoy
            else -> mapa[clave] ?: 0L
        }
        return segundosAMinutosDisplay(segundos)
    }

    val estaSemana = (0..6).sumOf { minutosOficialEn(it) }
    val semanaAnterior = (7..13).sumOf { minutosOficialEn(it) }
    if (semanaAnterior == 0L) return null to (estaSemana > 0)
    val cambio = (((estaSemana - semanaAnterior).toFloat() / semanaAnterior) * 100f).roundToInt()
    return cambio to (cambio >= 0)
}

private fun agruparEnSesiones(
    ordenados: List<HistorialActividadEntity>
): List<List<HistorialActividadEntity>> {
    if (ordenados.isEmpty()) return emptyList()
    val sesiones = mutableListOf<MutableList<HistorialActividadEntity>>()
    var actual = mutableListOf(ordenados.first())
    for (i in 1 until ordenados.size) {
        val ev = ordenados[i]
        val prev = ordenados[i - 1]
        val mismaSesion = ev.tipo == prev.tipo &&
            (ev.timestamp - prev.timestamp) <= GAP_SESION_MS
        if (mismaSesion) {
            actual.add(ev)
        } else {
            sesiones.add(actual)
            actual = mutableListOf(ev)
        }
    }
    sesiones.add(actual)
    return sesiones
}

private fun extraerPuntos(detalle: String, exito: Boolean): Int {
    val regex = Regex("""\+(\d+)\s*pts?""", RegexOption.IGNORE_CASE)
    regex.find(detalle)?.groupValues?.getOrNull(1)?.toIntOrNull()?.let { return it }
    return if (exito) 10 else 0
}

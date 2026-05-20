package org.fernandoblanco.inglesbasico.ui

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import org.fernandoblanco.inglesbasico.InglesApp
import org.fernandoblanco.inglesbasico.data.CompaneroData
import org.fernandoblanco.inglesbasico.db.entity.NinoEntity
import org.fernandoblanco.inglesbasico.ui.design.PlayOutlineButton
import org.fernandoblanco.inglesbasico.ui.design.PlayScreenGradient
import org.fernandoblanco.inglesbasico.ui.design.PlaySolidButton
import org.fernandoblanco.inglesbasico.ui.design.playTextFieldColors
import org.fernandoblanco.inglesbasico.ui.kid.GameFeedbackBlock
import org.fernandoblanco.inglesbasico.ui.kid.GamePlayContentCard
import org.fernandoblanco.inglesbasico.ui.kid.KidFeedback
import org.fernandoblanco.inglesbasico.ui.kid.KidFinSesion
import org.fernandoblanco.inglesbasico.ui.kid.KidGameBackground
import org.fernandoblanco.inglesbasico.ui.kid.KidListenButton
import org.fernandoblanco.inglesbasico.ui.kid.KidOptionButton
import org.fernandoblanco.inglesbasico.ui.kid.KidSessionProgress
import org.fernandoblanco.inglesbasico.ui.theme.Amarillo
import org.fernandoblanco.inglesbasico.ui.theme.AmarilloSuave
import org.fernandoblanco.inglesbasico.ui.theme.Azul
import org.fernandoblanco.inglesbasico.ui.theme.Morado
import org.fernandoblanco.inglesbasico.ui.theme.MoradoSuave
import org.fernandoblanco.inglesbasico.ui.theme.Naranja
import org.fernandoblanco.inglesbasico.ui.theme.NaranjaOscuro
import org.fernandoblanco.inglesbasico.ui.theme.PlayError
import org.fernandoblanco.inglesbasico.ui.theme.Rosa
import org.fernandoblanco.inglesbasico.ui.theme.Verde
import org.fernandoblanco.inglesbasico.ui.theme.VerdeSuave
import org.fernandoblanco.inglesbasico.ui.viewmodel.ActividadAudioViewModel
import org.fernandoblanco.inglesbasico.ui.viewmodel.ActividadChatViewModel
import org.fernandoblanco.inglesbasico.ui.viewmodel.ActividadImagenViewModel
import org.fernandoblanco.inglesbasico.ui.viewmodel.ActividadPalabrasViewModel
import org.fernandoblanco.inglesbasico.ui.viewmodel.ActividadVocabularioViewModel
import org.fernandoblanco.inglesbasico.ui.viewmodel.AuthPadreViewModel
import org.fernandoblanco.inglesbasico.ui.viewmodel.NinoPerfilViewModel
import org.fernandoblanco.inglesbasico.ui.viewmodel.ReportesViewModel
import org.fernandoblanco.inglesbasico.ui.viewmodel.SelectorPerfilViewModel
import org.fernandoblanco.inglesbasico.ui.viewmodel.PadrePerfilViewModel
import org.fernandoblanco.inglesbasico.ui.viewmodel.EditarNinoViewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.compose.material.icons.filled.Settings

object Rutas {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val REGISTRO = "registro"
    const val SELECTOR_PERFIL = "selector_perfil"
    const val CREAR_NINO = "crear_nino"
    const val INICIO = "inicio"
    const val ACTIVIDADES = "actividades"
    const val ACT_IMAGEN = "act_imagen"
    const val ACT_AUDIO = "act_audio"
    const val ACT_PALABRAS = "act_palabras"
    const val ACT_VOCABULARIO = "act_vocabulario"
    const val ACT_CHAT = "act_chat"
    const val REPORTES = "reportes"
    const val SELECTOR_MASCOTA = "selector_mascota"
    const val SALIR_NINO = "salir_nino"
    const val PERFIL_PADRE = "perfil_padre"
    const val PANEL_REPORTES = "panel_reportes"
    const val EDITAR_NINO = "editar_nino/{ninoId}"

    fun editarNino(ninoId: Long) = "editar_nino/$ninoId"
}

@Composable
fun InglesAppRoot() {
    val app = LocalContext.current.applicationContext as InglesApp
    val factory = remember(app) { InglesViewModelFactory(app) }
    val nav = rememberNavController()
    NavHost(
        navController = nav,
        startDestination = Rutas.SPLASH,
        enterTransition = { fadeIn(tween(300)) + slideInHorizontally(tween(300)) { w -> w / 14 } },
        exitTransition = { fadeOut(tween(220)) },
        popEnterTransition = { fadeIn(tween(280)) },
        popExitTransition = { fadeOut(tween(220)) + slideOutHorizontally(tween(280)) { w -> w / 14 } }
    ) {
        composable(Rutas.SPLASH) { PantallaSplash(app = app, nav = nav) }
        composable(Rutas.LOGIN) { PantallaLogin(factory = factory, nav = nav) }
        composable(Rutas.REGISTRO) { PantallaRegistro(factory = factory, nav = nav) }
        composable(Rutas.SELECTOR_PERFIL) { PantallaSelectorPerfil(factory = factory, nav = nav) }
        composable(Rutas.CREAR_NINO) { PantallaCrearNino(factory = factory, nav = nav) }
        composable(Rutas.INICIO) { PantallaInicio(nav = nav, factory = factory) }
        composable(Rutas.ACTIVIDADES) { PantallaListaActividades(nav = nav) }
        composable(Rutas.ACT_IMAGEN) { PantallaActividadImagen(factory = factory, nav = nav) }
        composable(Rutas.ACT_AUDIO) { PantallaActividadAudio(factory = factory, nav = nav) }
        composable(Rutas.ACT_PALABRAS) { PantallaActividadPalabras(factory = factory, nav = nav) }
        composable(Rutas.ACT_VOCABULARIO) { PantallaActividadVocabulario(factory = factory, nav = nav) }
        composable(Rutas.ACT_CHAT) { PantallaActividadChat(factory = factory, nav = nav) }
        composable(Rutas.REPORTES) { PantallaReportes(factory = factory, nav = nav) }
        composable(Rutas.SELECTOR_MASCOTA) { PantallaSelectorMascota(factory = factory, nav = nav) }
        composable(Rutas.SALIR_NINO) { PantallaSalirNino(factory = factory, nav = nav) }
        composable(Rutas.PERFIL_PADRE) {
            PantallaPerfilPadre(factory = factory, nav = nav)
        }
        composable(Rutas.PANEL_REPORTES) {
            org.fernandoblanco.inglesbasico.ui.parent.PantallaPanelReportesParental(factory = factory, nav = nav)
        }
        composable(
            route = Rutas.EDITAR_NINO,
            arguments = listOf(navArgument("ninoId") { type = NavType.LongType })
        ) { back ->
            val ninoId = back.arguments?.getLong("ninoId") ?: return@composable
            PantallaEditarNino(factory = factory, nav = nav, ninoId = ninoId)
        }
    }
}

@Composable
private fun PantallaSplash(app: InglesApp, nav: NavHostController) {
    LaunchedEffect(Unit) {
        val sesion = app.sesion
        val repo = app.repositorioNino
        val padreId = sesion.padreIdActivo
        if (padreId != null && app.repositorioPadre.obtenerPorId(padreId) == null) {
            sesion.padreIdActivo = null
            sesion.ninoIdActivo = null
        }
        val ninoId = sesion.ninoIdActivo
        if (ninoId != null && repo.obtenerPorId(ninoId) == null) {
            sesion.ninoIdActivo = null
        } else if (ninoId != null) {
            repo.reanudarMonitoreoTiempo(ninoId)
        }
        when {
            sesion.padreIdActivo == null ->
                nav.navigate(Rutas.LOGIN) { popUpTo(Rutas.SPLASH) { inclusive = true } }
            sesion.ninoIdActivo == null ->
                nav.navigate(Rutas.SELECTOR_PERFIL) { popUpTo(Rutas.SPLASH) { inclusive = true } }
            else ->
                nav.navigate(Rutas.INICIO) { popUpTo(Rutas.SPLASH) { inclusive = true } }
        }
    }
    KidGameBackground {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Text("📚", style = MaterialTheme.typography.displayLarge)
            Spacer(Modifier.height(12.dp))
            Text("Inglés Divertido", style = MaterialTheme.typography.headlineMedium, color = Naranja, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(20.dp))
            CircularProgressIndicator(color = Naranja, strokeWidth = 3.dp)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PantallaLogin(factory: InglesViewModelFactory, nav: NavHostController) {
    val vm: AuthPadreViewModel = viewModel(factory = factory)
    val mensaje by vm.mensaje.collectAsState()
    val cargando by vm.cargando.collectAsState()
    val snack = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var usuario by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }

    LaunchedEffect(mensaje) { mensaje?.let { scope.launch { snack.showSnackbar(it) }; vm.limpiarMensaje() } }

    Scaffold(snackbarHost = { SnackbarHost(snack) }, containerColor = Color.Transparent) { pad ->
        PlayScreenGradient(Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(pad)
                    .padding(horizontal = 28.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Spacer(Modifier.height(24.dp))
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .shadow(20.dp, CircleShape, spotColor = Naranja.copy(0.4f))
                        .clip(CircleShape)
                        .background(Brush.radialGradient(listOf(Amarillo, Naranja))),
                    contentAlignment = Alignment.Center
                ) { Text("📚", style = MaterialTheme.typography.displayMedium) }
                Text("¡Bienvenido!", style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                Text("Ingresa con tu cuenta de padre/tutor", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = usuario, onValueChange = { usuario = it }, label = { Text("Usuario") }, modifier = Modifier.fillMaxWidth(), singleLine = true, shape = RoundedCornerShape(16.dp), colors = playTextFieldColors())
                OutlinedTextField(value = contrasena, onValueChange = { contrasena = it }, label = { Text("Contraseña") }, modifier = Modifier.fillMaxWidth(), singleLine = true, visualTransformation = PasswordVisualTransformation(), shape = RoundedCornerShape(16.dp), colors = playTextFieldColors())
                PlaySolidButton(
                    text = "Entrar",
                    onClick = { vm.iniciarSesion(usuario, contrasena) { nav.navigate(Rutas.SELECTOR_PERFIL) { popUpTo(Rutas.LOGIN) { inclusive = true } } } },
                    enabled = !cargando,
                    loading = cargando,
                    containerColor = Naranja
                )
                PlayOutlineButton(text = "Crear cuenta nueva", onClick = { nav.navigate(Rutas.REGISTRO) }, borderColor = Morado)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PantallaRegistro(factory: InglesViewModelFactory, nav: NavHostController) {
    val vm: AuthPadreViewModel = viewModel(factory = factory)
    val mensaje by vm.mensaje.collectAsState()
    val cargando by vm.cargando.collectAsState()
    val snack = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var usuario by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }

    LaunchedEffect(mensaje) { mensaje?.let { scope.launch { snack.showSnackbar(it) }; vm.limpiarMensaje() } }

    Scaffold(
        snackbarHost = { SnackbarHost(snack) },
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text("Nueva cuenta", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = { nav.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { pad ->
        PlayScreenGradient(Modifier.fillMaxSize()) {
            Column(modifier = Modifier
                .padding(pad)
                .padding(24.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Crea tu cuenta de padre/tutor", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                OutlinedTextField(value = usuario, onValueChange = { usuario = it }, label = { Text("Usuario (único)") }, modifier = Modifier.fillMaxWidth(), singleLine = true, shape = RoundedCornerShape(16.dp), colors = playTextFieldColors())
                OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Tu nombre") }, modifier = Modifier.fillMaxWidth(), singleLine = true, shape = RoundedCornerShape(16.dp), colors = playTextFieldColors())
                OutlinedTextField(value = contrasena, onValueChange = { contrasena = it }, label = { Text("Contraseña (mín. 4)") }, modifier = Modifier.fillMaxWidth(), singleLine = true, visualTransformation = PasswordVisualTransformation(), shape = RoundedCornerShape(16.dp), colors = playTextFieldColors())
                Spacer(Modifier.height(8.dp))
                PlaySolidButton(
                    text = "Crear cuenta",
                    onClick = { vm.registrar(usuario, nombre, contrasena) { nav.navigate(Rutas.SELECTOR_PERFIL) { popUpTo(Rutas.LOGIN) { inclusive = true } } } },
                    enabled = !cargando,
                    loading = cargando,
                    containerColor = Morado
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PantallaSelectorPerfil(factory: InglesViewModelFactory, nav: NavHostController) {
    val vm: SelectorPerfilViewModel = viewModel(factory = factory)
    val padre by vm.padre.collectAsState()
    val ninos by vm.ninos.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    val snack = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var mostrarEliminarId by remember { mutableStateOf<Long?>(null) }

    LaunchedEffect(mensaje) { mensaje?.let { scope.launch { snack.showSnackbar(it) }; vm.limpiarMensaje() } }

    mostrarEliminarId?.let { id ->
        AlertDialog(
            onDismissRequest = { mostrarEliminarId = null },
            title = { Text("Eliminar perfil") },
            text = { Text("¿Eliminar este perfil? Se perderá todo su progreso.") },
            confirmButton = { TextButton(onClick = { vm.eliminarNino(id); mostrarEliminarId = null }) { Text("Eliminar", color = PlayError) } },
            dismissButton = { TextButton(onClick = { mostrarEliminarId = null }) { Text("Cancelar") } }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snack) },
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text("¡Hola${padre?.nombreMostrar?.let { ", $it" } ?: ""}!", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = { nav.navigate(Rutas.PERFIL_PADRE) }) { Icon(Icons.Default.Settings, null) }
                    TextButton(onClick = { vm.cerrarSesionCompleta { nav.navigate(Rutas.LOGIN) { popUpTo(0) { inclusive = true } } } }) {
                        Text("Salir", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f))
            )
        }
    ) { pad ->
        PlayScreenGradient(Modifier.fillMaxSize()) {
            Column(modifier = Modifier
                .padding(pad)
                .fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(Modifier.height(16.dp))
                Text("¿Quién va a jugar hoy?", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground, textAlign = TextAlign.Center)
                Spacer(Modifier.height(8.dp))
                Text("Toca tu nombre para entrar", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
                Spacer(Modifier.height(16.dp))
                PlayOutlineButton(
                    text = "Panel parental · Reportes",
                    onClick = { nav.navigate(Rutas.PANEL_REPORTES) },
                    borderColor = Morado,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
                Spacer(Modifier.height(16.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(ninos) { nino ->
                        TarjetaNino(
                            nino = nino,
                            onClick = { vm.seleccionarNino(nino.id) { nav.navigate(Rutas.INICIO) { popUpTo(Rutas.SELECTOR_PERFIL) { inclusive = true } } } },
                            onEditar = { nav.navigate(Rutas.editarNino(nino.id)) },
                            onEliminar = { mostrarEliminarId = nino.id }
                        )
                    }
                    if (ninos.size < 6) {
                        item { TarjetaAgregarNino(onClick = { nav.navigate(Rutas.CREAR_NINO) }) }
                    }
                }
            }
        }
    }
}

@Composable
private fun TarjetaNino(nino: NinoEntity, onClick: () -> Unit, onEditar: () -> Unit, onEliminar: () -> Unit) {
    val shape = RoundedCornerShape(24.dp)
    val colores = listOf(Naranja to AmarilloSuave, Morado to MoradoSuave, Azul to Color(0xFFE0F4FF), Verde to VerdeSuave, Rosa to Color(0xFFFFE8F3))
    val (acento, fondo) = colores[nino.id.toInt() % colores.size]
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .shadow(12.dp, shape, spotColor = acento.copy(0.3f))
            .clip(shape)
            .background(fondo),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable { onClick() }
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .shadow(8.dp, CircleShape, spotColor = acento.copy(0.4f))
                    .clip(CircleShape)
                    .background(acento.copy(0.15f))
                    .border(2.dp, acento.copy(0.4f), CircleShape),
                contentAlignment = Alignment.Center
            ) { Text(nino.avatarEmoji, style = MaterialTheme.typography.headlineMedium) }
            Spacer(Modifier.height(6.dp))
            Text(nino.nombreMostrar, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = acento, textAlign = TextAlign.Center)
            Text("Nivel ${nino.nivel} · ⭐ ${nino.puntajeTotal}pts", style = MaterialTheme.typography.labelMedium, color = acento.copy(0.7f))
        }
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(acento.copy(0.2f))
                    .clickable { onEditar() },
                contentAlignment = Alignment.Center
            ) { Text("✏️", style = MaterialTheme.typography.labelSmall) }
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(PlayError.copy(0.15f))
                    .clickable { onEliminar() },
                contentAlignment = Alignment.Center
            ) { Text("🗑️", style = MaterialTheme.typography.labelSmall) }
        }
    }
}

@Composable
private fun TarjetaAgregarNino(onClick: () -> Unit) {
    val shape = RoundedCornerShape(24.dp)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .shadow(8.dp, shape)
            .clip(shape)
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(0.6f))
            .border(2.dp, MaterialTheme.colorScheme.outline.copy(0.3f), shape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(0.15f)), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Add, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(32.dp))
            }
            Text("Agregar niño", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PantallaCrearNino(factory: InglesViewModelFactory, nav: NavHostController) {
    val vm: SelectorPerfilViewModel = viewModel(factory = factory)
    val mensaje by vm.mensaje.collectAsState()
    val cargando by vm.cargando.collectAsState()
    val snack = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var nombre by remember { mutableStateOf("") }
    var emojiSeleccionado by remember { mutableStateOf("🐱") }
    val emojis = listOf("🐱", "🐶", "🐰", "🦊", "🐸", "🐯", "🐼", "🐨", "🦁", "🐻", "🐲", "🦄", "🐧", "🦋", "🐬")

    LaunchedEffect(mensaje) { mensaje?.let { scope.launch { snack.showSnackbar(it) }; vm.limpiarMensaje() } }

    Scaffold(
        snackbarHost = { SnackbarHost(snack) },
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text("Nuevo perfil", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = { nav.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { pad ->
        PlayScreenGradient(Modifier.fillMaxSize()) {
            Column(modifier = Modifier
                .padding(pad)
                .padding(24.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Elige un avatar", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Box(modifier = Modifier
                    .size(96.dp)
                    .shadow(16.dp, CircleShape, spotColor = Naranja.copy(0.4f))
                    .clip(CircleShape)
                    .background(AmarilloSuave), contentAlignment = Alignment.Center) {
                    Text(emojiSeleccionado, style = MaterialTheme.typography.displayMedium)
                }
                LazyVerticalGrid(
                    columns = GridCells.Fixed(5),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(emojis) { emoji ->
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(CircleShape)
                                .background(if (emoji == emojiSeleccionado) Naranja.copy(0.2f) else MaterialTheme.colorScheme.surfaceVariant)
                                .border(
                                    if (emoji == emojiSeleccionado) 2.dp else 0.dp,
                                    Naranja,
                                    CircleShape
                                )
                                .clickable { emojiSeleccionado = emoji },
                            contentAlignment = Alignment.Center
                        ) { Text(emoji, style = MaterialTheme.typography.titleLarge) }
                    }
                }
                OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre del niño") }, modifier = Modifier.fillMaxWidth(), singleLine = true, shape = RoundedCornerShape(16.dp), colors = playTextFieldColors())
                PlaySolidButton(text = "Crear perfil", onClick = { vm.crearNino(nombre, emojiSeleccionado) { nav.popBackStack() } }, enabled = !cargando && nombre.isNotBlank(), loading = cargando, containerColor = Verde)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PantallaInicio(nav: NavHostController, factory: InglesViewModelFactory) {
    val vm: NinoPerfilViewModel = viewModel(factory = factory)
    val perfil by vm.perfil.collectAsState()

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("¡Hola${perfil?.nombreMostrar?.let { ", $it" } ?: ""}!", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Text("¿Qué hacemos hoy?", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                actions = {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .padding(end = 8.dp)
                            .clip(CircleShape)
                            .background(Naranja.copy(0.15f))
                            .clickable { nav.navigate(Rutas.SALIR_NINO) },
                        contentAlignment = Alignment.Center
                    ) { Text(perfil?.avatarEmoji ?: "🐱", style = MaterialTheme.typography.titleLarge) }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f))
            )
        }
    ) { pad ->
        PlayScreenGradient(Modifier.fillMaxSize()) {
            Column(modifier = Modifier
                .padding(pad)
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(20.dp)) {
                perfil?.let { p ->
                    if (p.rachaActual > 0) {
                        val shape = RoundedCornerShape(20.dp)
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .shadow(10.dp, shape, spotColor = Amarillo.copy(0.4f))
                            .clip(shape)
                            .background(AmarilloSuave)
                            .padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Text("🔥", style = MaterialTheme.typography.headlineMedium)
                                Column {
                                    Text("¡${p.rachaActual} días seguidos!", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = NaranjaOscuro)
                                    Text("Récord: ${p.rachaMaxima} días · ¡No pierdas tu racha!", style = MaterialTheme.typography.bodySmall, color = NaranjaOscuro.copy(0.8f))
                                }
                            }
                        }
                    }
                    val companero = CompaneroData.obtenerPorId(p.mascotaId)
                    val shape = RoundedCornerShape(20.dp)
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .shadow(10.dp, shape, spotColor = Morado.copy(0.2f))
                        .clip(shape)
                        .background(MoradoSuave)
                        .padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text(companero.emoji, style = MaterialTheme.typography.headlineLarge)
                            Column {
                                Text(companero.nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Morado)
                                Text(companero.frasesInicio.random(), style = MaterialTheme.typography.bodySmall, color = Morado.copy(0.8f))
                            }
                        }
                    }
                }
                PlayNavCard(emoji = "🎮", titulo = "Actividades", subtitulo = "Imagen, audio y palabras", accentStart = Naranja, accentEnd = Rosa, onClick = { nav.navigate(Rutas.ACTIVIDADES) })
                PlayNavCard(emoji = "📊", titulo = "Reportes", subtitulo = "Tu progreso y logros", accentStart = Azul, accentEnd = Verde, onClick = { nav.navigate(Rutas.REPORTES) })
                PlayOutlineButton(text = "Cambiar compañero de aventura", onClick = { nav.navigate(Rutas.SELECTOR_MASCOTA) }, borderColor = Morado)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PantallaSalirNino(factory: InglesViewModelFactory, nav: NavHostController) {
    val vm: SelectorPerfilViewModel = viewModel(factory = factory)
    val mensaje by vm.mensaje.collectAsState()
    val verificando by vm.verificandoPin.collectAsState()
    val snack = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var contrasena by remember { mutableStateOf("") }

    LaunchedEffect(mensaje) { mensaje?.let { scope.launch { snack.showSnackbar(it) }; vm.limpiarMensaje() } }

    Scaffold(
        snackbarHost = { SnackbarHost(snack) },
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text("Control parental", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = { nav.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { pad ->
        PlayScreenGradient(Modifier.fillMaxSize()) {
            Column(modifier = Modifier
                .padding(pad)
                .fillMaxSize()
                .padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Box(modifier = Modifier
                    .size(96.dp)
                    .shadow(16.dp, CircleShape, spotColor = Morado.copy(0.4f))
                    .clip(CircleShape)
                    .background(MoradoSuave), contentAlignment = Alignment.Center) {
                    Text("🔒", style = MaterialTheme.typography.displaySmall)
                }
                Spacer(Modifier.height(24.dp))
                Text("¿Salir del perfil?", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground, textAlign = TextAlign.Center)
                Spacer(Modifier.height(8.dp))
                Text("Pide a tu papá o mamá que ingrese la contraseña para continuar", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
                Spacer(Modifier.height(28.dp))
                OutlinedTextField(value = contrasena, onValueChange = { contrasena = it }, label = { Text("Contraseña del padre/tutor") }, modifier = Modifier.fillMaxWidth(), singleLine = true, visualTransformation = PasswordVisualTransformation(), shape = RoundedCornerShape(16.dp), colors = playTextFieldColors())
                Spacer(Modifier.height(16.dp))
                PlaySolidButton(
                    text = "Confirmar y salir",
                    onClick = { vm.verificarContrasenaParaSalir(contrasena) { nav.navigate(Rutas.SELECTOR_PERFIL) { popUpTo(0) { inclusive = true } } } },
                    enabled = !verificando && contrasena.isNotBlank(),
                    loading = verificando,
                    containerColor = Morado
                )
            }
        }
    }
}

@Composable
private fun PlayNavCard(emoji: String, titulo: String, subtitulo: String, accentStart: Color, accentEnd: Color, nivel: Int? = null, onClick: () -> Unit) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(targetValue = if (pressed) 0.97f else 1f, animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium), label = "navCard")
    val shape = RoundedCornerShape(28.dp)
    Box(modifier = Modifier
        .fillMaxWidth()
        .scale(scale)
        .shadow(16.dp, shape, spotColor = accentStart.copy(0.3f))
        .clip(shape)
        .background(MaterialTheme.colorScheme.surface)
        .clickable(interactionSource = interaction, indication = null) { onClick() }) {
        Row(Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)) {
            Box(Modifier
                .width(10.dp)
                .fillMaxHeight()
                .background(Brush.verticalGradient(colors = listOf(accentStart, accentEnd))))
            Column(Modifier.padding(horizontal = 18.dp, vertical = 18.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                if (nivel != null) {
                    Box(Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(accentStart.copy(0.15f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)) {
                        Text("Nivel $nivel", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = accentStart)
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(emoji, style = MaterialTheme.typography.displaySmall)
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(titulo, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        Text(subtitulo, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PantallaListaActividades(nav: NavHostController) {
    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text("Actividades", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = { nav.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f))
            )
        }
    ) { pad ->
        PlayScreenGradient(Modifier.fillMaxSize()) {
            Column(modifier = Modifier
                .padding(pad)
                .padding(20.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Estudia primero, luego juega 🎓", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                PlayNavCard(emoji = "📖", titulo = "Aprende palabras", subtitulo = "Estudia con tarjetas antes de jugar", accentStart = Amarillo, accentEnd = Verde, onClick = { nav.navigate(Rutas.ACT_VOCABULARIO) })
                PlayNavCard(emoji = "🖼️", titulo = "Juego de imágenes", subtitulo = "Mira el emoji y elige en inglés", accentStart = Naranja, accentEnd = NaranjaOscuro, nivel = 1, onClick = { nav.navigate(Rutas.ACT_IMAGEN) })
                PlayNavCard(emoji = "👂", titulo = "Juego de audio", subtitulo = "Escucha y elige la respuesta", accentStart = Morado, accentEnd = Rosa, nivel = 2, onClick = { nav.navigate(Rutas.ACT_AUDIO) })
                PlayNavCard(emoji = "✏️", titulo = "Completar palabras", subtitulo = "Completa la palabra en inglés", accentStart = Verde, accentEnd = Azul, nivel = 3, onClick = { nav.navigate(Rutas.ACT_PALABRAS) })
                PlayNavCard(emoji = "🤖", titulo = "Chat con el Tutor", subtitulo = "Conversa y aprende con tu compañero", accentStart = Azul, accentEnd = Verde, nivel = 4, onClick = { nav.navigate(Rutas.ACT_CHAT) })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PantallaActividadImagen(factory: InglesViewModelFactory, nav: NavHostController) {
    val vm: ActividadImagenViewModel = viewModel(factory = factory)
    val feedback by vm.feedback.collectAsState()
    val feedbackOk by vm.feedbackOk.collectAsState()
    val solucion by vm.solucionCorrecta.collectAsState()
    val fin by vm.finSesion.collectAsState()
    val indice by vm.indice.collectAsState()
    val procesando by vm.procesando.collectAsState()
    val pregunta by vm.preguntaVisible.collectAsState()
    val ctx = LocalContext.current

    LaunchedEffect(Unit) { vm.sonido.collect { ok -> if (ok) KidFeedback.playCorrect(ctx) else KidFeedback.playIncorrect(ctx) } }

    Scaffold(containerColor = Color.Transparent,
        topBar = {
            TopAppBar(title = { Text("🖼️ Imágenes", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = { nav.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) } },
                actions = { TextButton(onClick = { vm.reiniciar() }) { Text("Nueva sesión", color = Naranja, fontWeight = FontWeight.SemiBold) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface))
        }
    ) { pad ->
        KidGameBackground {
            Box(Modifier
                .padding(pad)
                .fillMaxSize()) {
                if (fin != null) {
                    Column(Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                        KidFinSesion(aciertos = fin!!.first, total = fin!!.second, onJugarOtra = { vm.reiniciar() }, onSalir = { nav.popBackStack() })
                    }
                } else if (pregunta != null) {
                    key(indice, pregunta!!.correctaEn) {
                        Column(Modifier
                            .padding(16.dp)
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            GamePlayContentCard {
                                KidSessionProgress(indice + 1, ActividadImagenViewModel.PREGUNTAS_POR_SESION)
                                Spacer(Modifier.height(8.dp))
                                Text("¿Cuál es la palabra en inglés?", style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                                Box(Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp), contentAlignment = Alignment.Center) {
                                    Text(pregunta!!.emoji, style = MaterialTheme.typography.displayLarge.copy(fontSize = MaterialTheme.typography.displayLarge.fontSize * 1.55f))
                                }
                            }
                            GameFeedbackBlock(texto = feedback, esCorrecto = feedbackOk, solucionCorrecta = solucion)
                            pregunta!!.opciones.chunked(2).forEach { fila ->
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                    fila.forEach { op -> KidOptionButton(label = op, enabled = !procesando, onClick = { vm.responder(op) }, modifier = Modifier.weight(1f)) }
                                    if (fila.size == 1) Spacer(Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PantallaActividadAudio(factory: InglesViewModelFactory, nav: NavHostController) {
    val vm: ActividadAudioViewModel = viewModel(factory = factory)
    val feedback by vm.feedback.collectAsState()
    val feedbackOk by vm.feedbackOk.collectAsState()
    val solucion by vm.solucionCorrecta.collectAsState()
    val fin by vm.finSesion.collectAsState()
    val indice by vm.indice.collectAsState()
    val listo by vm.ttsListo.collectAsState()
    val procesando by vm.procesando.collectAsState()
    val pregunta by vm.preguntaVisible.collectAsState()
    val ctx = LocalContext.current

    LaunchedEffect(Unit) { vm.sonido.collect { ok -> if (ok) KidFeedback.playCorrect(ctx) else KidFeedback.playIncorrect(ctx) } }

    Scaffold(containerColor = Color.Transparent,
        topBar = {
            TopAppBar(title = { Text("👂 Audio", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = { nav.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) } },
                actions = { TextButton(onClick = { vm.reiniciar() }) { Text("Nueva sesión", color = Morado, fontWeight = FontWeight.SemiBold) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface))
        }
    ) { pad ->
        KidGameBackground {
            Box(Modifier
                .padding(pad)
                .fillMaxSize()) {
                if (fin != null) {
                    Column(Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                        KidFinSesion(aciertos = fin!!.first, total = fin!!.second, onJugarOtra = { vm.reiniciar() }, onSalir = { nav.popBackStack() })
                    }
                } else if (pregunta != null) {
                    key(indice, pregunta!!.palabraIngles) {
                        Column(Modifier
                            .padding(16.dp)
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            GamePlayContentCard {
                                KidSessionProgress(indice + 1, ActividadAudioViewModel.PREGUNTAS_POR_SESION)
                                Spacer(Modifier.height(8.dp))
                                Text("Escucha la palabra y elige la correcta", style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                                Text("(Toca el botón para escuchar)", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                                KidListenButton(enabled = listo, onClick = { vm.reproducir() })
                            }
                            GameFeedbackBlock(texto = feedback, esCorrecto = feedbackOk, solucionCorrecta = solucion)
                            pregunta!!.opcionesIngles.forEach { op -> KidOptionButton(label = op, enabled = !procesando, onClick = { vm.responder(op) }, modifier = Modifier.fillMaxWidth()) }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PantallaActividadPalabras(factory: InglesViewModelFactory, nav: NavHostController) {
    val vm: ActividadPalabrasViewModel = viewModel(factory = factory)
    val feedback by vm.feedback.collectAsState()
    val feedbackOk by vm.feedbackOk.collectAsState()
    val solucion by vm.solucionCorrecta.collectAsState()
    val fin by vm.finSesion.collectAsState()
    val indice by vm.indice.collectAsState()
    val procesando by vm.procesando.collectAsState()
    val pregunta by vm.preguntaVisible.collectAsState()
    val ctx = LocalContext.current

    LaunchedEffect(Unit) { vm.sonido.collect { ok -> if (ok) KidFeedback.playCorrect(ctx) else KidFeedback.playIncorrect(ctx) } }

    Scaffold(containerColor = Color.Transparent,
        topBar = {
            TopAppBar(title = { Text("✏️ Palabras", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = { nav.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) } },
                actions = { TextButton(onClick = { vm.reiniciar() }) { Text("Nueva sesión", color = Verde, fontWeight = FontWeight.SemiBold) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface))
        }
    ) { pad ->
        KidGameBackground {
            Box(Modifier
                .padding(pad)
                .fillMaxSize()) {
                if (fin != null) {
                    Column(Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                        KidFinSesion(aciertos = fin!!.first, total = fin!!.second, onJugarOtra = { vm.reiniciar() }, onSalir = { nav.popBackStack() })
                    }
                } else if (pregunta != null) {
                    key(indice, pregunta!!.correcta) {
                        Column(Modifier
                            .padding(16.dp)
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            GamePlayContentCard {
                                KidSessionProgress(indice + 1, ActividadPalabrasViewModel.PREGUNTAS_POR_SESION)
                                Spacer(Modifier.height(8.dp))
                                Text("Completa la palabra en inglés", style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                                Text(pregunta!!.incompleta, style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, color = Naranja, modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp))
                                Text(pregunta!!.pista, style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.fillMaxWidth())
                            }
                            GameFeedbackBlock(texto = feedback, esCorrecto = feedbackOk, solucionCorrecta = solucion)
                            pregunta!!.opciones.forEach { w -> KidOptionButton(label = w, enabled = !procesando, onClick = { vm.responder(w) }, modifier = Modifier.fillMaxWidth()) }
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PantallaActividadVocabulario(factory: InglesViewModelFactory, nav: NavHostController) {
    val vm: ActividadVocabularioViewModel = viewModel(factory = factory)
    val indice by vm.indice.collectAsState()
    val fin by vm.finSesion.collectAsState()
    val mostrarTrad by vm.mostrarTrad.collectAsState()
    val tarjetaActualState by vm.tarjetaActual.collectAsState()

    Scaffold(containerColor = Color.Transparent,
        topBar = {
            TopAppBar(title = { Text("📖 Aprende palabras", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = { nav.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) } },
                actions = { TextButton(onClick = { vm.reiniciar() }) { Text("Reiniciar", color = Amarillo, fontWeight = FontWeight.SemiBold) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface))
        }
    ) { pad ->
        KidGameBackground {
            Column(modifier = Modifier
                .padding(pad)
                .fillMaxSize()
                .padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                KidSessionProgress(indice + 1, ActividadVocabularioViewModel.TARJETAS_POR_SESION)

                val tarjetaActual = tarjetaActualState // Variable local para permitir Smart Cast

                if (fin) {
                    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                        Text("🎉", style = MaterialTheme.typography.displayLarge)
                        Spacer(Modifier.height(12.dp))
                        Text("¡Estudiaste 10 palabras!", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                        Spacer(Modifier.height(8.dp))
                        Text("Ahora ve a los juegos y demuestra lo que aprendiste", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
                        Spacer(Modifier.height(24.dp))
                        PlaySolidButton(text = "Estudiar más palabras", onClick = { vm.reiniciar() }, containerColor = Amarillo)
                        Spacer(Modifier.height(12.dp))
                        PlayOutlineButton(text = "Ir a jugar", onClick = { nav.popBackStack() }, borderColor = Verde)
                    }
                } else if (tarjetaActual != null) {
                    val shape = RoundedCornerShape(28.dp)
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .shadow(20.dp, shape, spotColor = Amarillo.copy(0.3f))
                        .clip(shape)
                        .background(MaterialTheme.colorScheme.surface)
                        .clickable { if (!mostrarTrad) vm.revelarTraduccion() }, contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.padding(32.dp)) {
                            Text(tarjetaActual.emoji, style = MaterialTheme.typography.displayLarge.copy(fontSize = MaterialTheme.typography.displayLarge.fontSize * 2f))
                            Text(tarjetaActual.en, style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold, color = Naranja, textAlign = TextAlign.Center)
                            if (mostrarTrad) {
                                Text(tarjetaActual.es, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Verde, textAlign = TextAlign.Center)
                            } else {
                                Text("Toca para ver la traducción en español", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
                            }
                        }
                    }
                    if (mostrarTrad) {
                        PlaySolidButton(text = if (indice < ActividadVocabularioViewModel.TARJETAS_POR_SESION - 1) "Siguiente palabra ➡️" else "¡Terminar! 🎉", onClick = { vm.siguiente() }, containerColor = Verde)
                    } else {
                        PlayOutlineButton(text = "Toca la tarjeta para ver", onClick = { vm.revelarTraduccion() }, borderColor = Amarillo)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PantallaActividadChat(factory: InglesViewModelFactory, nav: NavHostController) {
    val vm: ActividadChatViewModel = viewModel(factory = factory)
    val mensajes by vm.mensajes.collectAsState()
    val cargando by vm.cargando.collectAsState()
    val fin by vm.finSesion.collectAsState()
    val emocion by vm.emocionCompanero.collectAsState()
    val aciertos by vm.aciertos.collectAsState()
    val turno by vm.turnoActual.collectAsState()
    val companero by vm.companero.collectAsState()
    val error by vm.error.collectAsState()
    val snack = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val ultimoMensaje = mensajes.lastOrNull()
    val scale by animateFloatAsState(targetValue = if (emocion.animacion == "happy" || emocion.animacion == "celebrate") 1.18f else 1f, animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy), label = "mascota")

    LaunchedEffect(error) { error?.let { scope.launch { snack.showSnackbar(it) }; vm.limpiarError() } }

    Scaffold(snackbarHost = { SnackbarHost(snack) }, containerColor = Color.Transparent,
        topBar = {
            TopAppBar(title = { Text("Chat con ${companero.nombre}", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = { nav.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) } },
                actions = { TextButton(onClick = { vm.reiniciar() }) { Text("Nueva sesión", color = Morado, fontWeight = FontWeight.SemiBold) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface))
        }
    ) { pad ->
        KidGameBackground {
            Column(modifier = Modifier
                .padding(pad)
                .fillMaxSize()
                .padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                KidSessionProgress(turno + 1, ActividadChatViewModel.TURNOS_POR_SESION)
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .shadow(12.dp, RoundedCornerShape(24.dp), spotColor = Morado.copy(0.2f))
                    .clip(RoundedCornerShape(24.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.Top) {
                        Text(emocion.emoji, style = MaterialTheme.typography.displaySmall, modifier = Modifier.scale(scale))
                        if (cargando) {
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text("${companero.nombre} está pensando...", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Morado, strokeWidth = 2.dp)
                            }
                        } else if (ultimoMensaje != null) {
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text(ultimoMensaje.textoIngles, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                                Text(ultimoMensaje.textoEspanol, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
                if (fin) {
                    Column(Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .verticalScroll(rememberScrollState()), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                        KidFinSesion(aciertos = aciertos, total = ActividadChatViewModel.TURNOS_POR_SESION, onJugarOtra = { vm.reiniciar() }, onSalir = { nav.popBackStack() })
                    }
                } else {
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        ultimoMensaje?.opciones?.forEach { opcion -> KidOptionButton(label = opcion, enabled = !cargando, onClick = { vm.responderOpcion(opcion) }, modifier = Modifier.fillMaxWidth()) }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PantallaReportes(factory: InglesViewModelFactory, nav: NavHostController) {
    val vm: ReportesViewModel = viewModel(factory = factory)
    val nino by vm.nino.collectAsState()

    Scaffold(containerColor = Color.Transparent,
        topBar = {
            TopAppBar(title = { Text("Tus logros", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = { nav.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)))
        }
    ) { pad ->
        PlayScreenGradient(Modifier.fillMaxSize()) {
            Column(modifier = Modifier
                .padding(pad)
                .padding(20.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                if (nino == null) {
                    Text(
                        "No hay datos de tu perfil.\nVuelve al inicio o pide a tu papá que elija tu perfil.",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(12.dp))
                    PlaySolidButton(text = "Volver al inicio", onClick = { nav.popBackStack() }, containerColor = Azul)
                } else {
                    val n = nino!!
                    val mascota = CompaneroData.obtenerPorId(n.mascotaId).emoji
                    org.fernandoblanco.inglesbasico.ui.kid.KidReportesHeader(n.nombreMostrar, n.avatarEmoji, mascota)
                    org.fernandoblanco.inglesbasico.ui.kid.KidNivelEstrellas(n.nivel, org.fernandoblanco.inglesbasico.ui.kid.estrellasDeNivel(n.puntajeTotal))
                    org.fernandoblanco.inglesbasico.ui.kid.KidProgresoBarra(
                        org.fernandoblanco.inglesbasico.ui.kid.progresoNivel(n.puntajeTotal),
                        "Tu avance en este nivel"
                    )
                    org.fernandoblanco.inglesbasico.ui.kid.KidPuntajeBurbuja(n.puntajeTotal)
                    val tiempos = org.fernandoblanco.inglesbasico.ui.kid.tiemposVisualizacion(n)
                    org.fernandoblanco.inglesbasico.ui.kid.KidTiempoUso(tiempos.first, tiempos.second, tiempos.third)
                    Text("Todas tus actividades", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    BarraJuegoKid("📖 Aprende palabras", n.tarjetasVocabulario, n.sesionesVocabulario.coerceAtLeast(1), Amarillo)
                    BarraJuegoKid("🖼️ Elegir imagen", n.aciertosImagen, n.partidasImagen, Naranja)
                    BarraJuegoKid("👂 Audio", n.aciertosAudio, n.partidasAudio, Morado)
                    BarraJuegoKid("✏️ Completar palabras", n.aciertosPalabras, n.partidasPalabras, Verde)
                    BarraJuegoKid("🤖 Chat con tutor", n.aciertosChat, n.partidasChat, Azul)
                    val items = org.fernandoblanco.inglesbasico.ui.kid.historialActividades(n)
                    if (items.isEmpty()) {
                        Text(
                            "Juega las actividades para ver tu historial aquí.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        items.forEach { org.fernandoblanco.inglesbasico.ui.kid.KidHistorialTarjeta(it) }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PantallaSelectorMascota(factory: InglesViewModelFactory, nav: NavHostController) {
    val vm: NinoPerfilViewModel = viewModel(factory = factory)

    Scaffold(containerColor = Color.Transparent,
        topBar = {
            TopAppBar(title = { Text("Elige tu compañero", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = { nav.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)))
        }
    ) { pad ->
        PlayScreenGradient(Modifier.fillMaxSize()) {
            Column(modifier = Modifier
                .padding(pad)
                .padding(20.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("¿Quién será tu compañero de aventura?", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                CompaneroData.todos.forEach { companero ->
                    val shape = RoundedCornerShape(24.dp)
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .shadow(12.dp, shape, spotColor = Morado.copy(0.2f))
                        .clip(shape)
                        .background(MaterialTheme.colorScheme.surface)
                        .clickable { vm.guardarMascota(companero.id) { nav.popBackStack() } }
                        .padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Box(modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(AmarilloSuave), contentAlignment = Alignment.Center) { Text(companero.emoji, style = MaterialTheme.typography.displaySmall) }
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(companero.nombre, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                Text(companero.frasesBienvenida.first(), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BarraJuegoKid(titulo: String, aciertos: Int, intentos: Int, color: Color) {
    val ratio = if (intentos == 0) 0f else (aciertos.toFloat() / intentos).coerceIn(0f, 1f)
    val animated by animateFloatAsState(targetValue = ratio, animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMediumLow), label = "barraProgreso")
    val errores = (intentos - aciertos).coerceAtLeast(0)
    val shape = RoundedCornerShape(24.dp)
    Box(modifier = Modifier
        .fillMaxWidth()
        .shadow(12.dp, shape, spotColor = color.copy(0.2f))
        .clip(shape)
        .background(MaterialTheme.colorScheme.surface)) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(titulo, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            Box(Modifier
                .fillMaxWidth()
                .height(16.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(color.copy(0.15f))) {
                Box(Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(animated)
                    .background(Brush.horizontalGradient(listOf(color, color.copy(0.8f)))))
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("✅ $aciertos aciertos", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = Verde)
                Text("❌ $errores errores", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = PlayError)
            }
            Text("${(ratio * 100).toInt()}% de aciertos", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = color)
        }
    }
}

private fun areasMejora(n: NinoEntity): List<String> {
    data class T(val nombre: String, val a: Int, val t: Int)
    val tipos = listOf(T("Imágenes", n.aciertosImagen, n.partidasImagen), T("Audio", n.aciertosAudio, n.partidasAudio), T("Palabras", n.aciertosPalabras, n.partidasPalabras))
    return tipos.mapNotNull { x ->
        if (x.t < 3) return@mapNotNull null
        val r = x.a.toFloat() / x.t
        if (r < 0.55f) "Practica más el juego de ${x.nombre.lowercase()} (${(r * 100).toInt()}% acierto)" else null
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PantallaPerfilPadre(factory: InglesViewModelFactory, nav: NavHostController) {
    val vm: PadrePerfilViewModel = viewModel(factory = factory)
    val padre by vm.padre.collectAsState()
    val nombre by vm.nombre.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    val cargando by vm.cargando.collectAsState()
    val snack = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var nuevaContrasena by remember { mutableStateOf("") }
    var confirmarContrasena by remember { mutableStateOf("") }

    LaunchedEffect(mensaje) { mensaje?.let { scope.launch { snack.showSnackbar(it) }; vm.limpiarMensaje() } }

    Scaffold(
        snackbarHost = { SnackbarHost(snack) },
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text("Mi cuenta", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = { nav.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f))
            )
        }
    ) { pad ->
        PlayScreenGradient(Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(pad)
                    .padding(24.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .shadow(16.dp, CircleShape, spotColor = Naranja.copy(0.4f))
                        .clip(CircleShape)
                        .background(Brush.radialGradient(listOf(Amarillo, Naranja))),
                    contentAlignment = Alignment.Center
                ) { Text("👤", style = MaterialTheme.typography.displaySmall) }
                Text(padre?.usuario ?: "", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(8.dp))
                PlayOutlineButton(
                    text = "Ver reportes de los niños",
                    onClick = { nav.navigate(Rutas.PANEL_REPORTES) },
                    borderColor = Azul
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { vm.setNombre(it) },
                    label = { Text("Nombre para mostrar") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = playTextFieldColors()
                )
                OutlinedTextField(
                    value = nuevaContrasena,
                    onValueChange = { nuevaContrasena = it },
                    label = { Text("Nueva contraseña (opcional)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    shape = RoundedCornerShape(16.dp),
                    colors = playTextFieldColors()
                )
                OutlinedTextField(
                    value = confirmarContrasena,
                    onValueChange = { confirmarContrasena = it },
                    label = { Text("Confirmar contraseña") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    shape = RoundedCornerShape(16.dp),
                    colors = playTextFieldColors()
                )
                Spacer(Modifier.height(8.dp))
                PlaySolidButton(
                    text = "Guardar cambios",
                    onClick = {
                        if (nuevaContrasena.isNotBlank() && nuevaContrasena != confirmarContrasena) {
                            scope.launch { snack.showSnackbar("Las contraseñas no coinciden") }
                            return@PlaySolidButton
                        }
                        val pwd = nuevaContrasena.takeIf { it.isNotBlank() }
                        vm.guardar(pwd) {
                            scope.launch { snack.showSnackbar("¡Cuenta actualizada!") }
                            nuevaContrasena = ""
                            confirmarContrasena = ""
                        }
                    },
                    enabled = !cargando,
                    loading = cargando,
                    containerColor = Verde
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PantallaEditarNino(factory: InglesViewModelFactory, nav: NavHostController, ninoId: Long) {
    val vm: EditarNinoViewModel = viewModel(factory = factory)
    val nombre by vm.nombre.collectAsState()
    val emoji by vm.emoji.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    val cargando by vm.cargando.collectAsState()
    val snack = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var mostrarEliminar by remember { mutableStateOf(false) }
    val emojis = listOf("🐱", "🐶", "🐰", "🦊", "🐸", "🐯", "🐼", "🐨", "🦁", "🐻", "🐲", "🦄", "🐧", "🦋", "🐬")

    LaunchedEffect(Unit) { vm.cargar(ninoId) }
    LaunchedEffect(mensaje) { mensaje?.let { scope.launch { snack.showSnackbar(it) }; vm.limpiarMensaje() } }

    if (mostrarEliminar) {
        AlertDialog(
            onDismissRequest = { mostrarEliminar = false },
            title = { Text("Eliminar perfil") },
            text = { Text("¿Eliminar este perfil? Se perderá todo su progreso permanentemente.") },
            confirmButton = {
                TextButton(onClick = {
                    mostrarEliminar = false
                    vm.eliminar(ninoId) { nav.popBackStack() }
                }) { Text("Eliminar", color = PlayError) }
            },
            dismissButton = { TextButton(onClick = { mostrarEliminar = false }) { Text("Cancelar") } }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snack) },
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text("Editar perfil", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = { nav.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f))
            )
        }
    ) { pad ->
        PlayScreenGradient(Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(pad)
                    .padding(24.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Elige un avatar", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .shadow(16.dp, CircleShape, spotColor = Naranja.copy(0.4f))
                        .clip(CircleShape)
                        .background(AmarilloSuave),
                    contentAlignment = Alignment.Center
                ) { Text(emoji, style = MaterialTheme.typography.displayMedium) }
                LazyVerticalGrid(
                    columns = GridCells.Fixed(5),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(emojis) { e ->
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(CircleShape)
                                .background(if (e == emoji) Naranja.copy(0.2f) else MaterialTheme.colorScheme.surfaceVariant)
                                .border(if (e == emoji) 2.dp else 0.dp, Naranja, CircleShape)
                                .clickable { vm.setEmoji(e) },
                            contentAlignment = Alignment.Center
                        ) { Text(e, style = MaterialTheme.typography.titleLarge) }
                    }
                }
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { vm.setNombre(it) },
                    label = { Text("Nombre del niño") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = playTextFieldColors()
                )
                PlaySolidButton(
                    text = "Guardar cambios",
                    onClick = { vm.guardar(ninoId) { nav.popBackStack() } },
                    enabled = !cargando && nombre.isNotBlank(),
                    loading = cargando,
                    containerColor = Verde
                )
                PlayOutlineButton(
                    text = "Eliminar este perfil",
                    onClick = { mostrarEliminar = true },
                    borderColor = PlayError,
                    textColor = PlayError
                )
            }
        }
    }
}
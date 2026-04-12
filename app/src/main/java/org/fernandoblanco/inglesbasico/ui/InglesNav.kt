package org.fernandoblanco.inglesbasico.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.layout.ContentScale
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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.launch
import org.fernandoblanco.inglesbasico.InglesApp
import org.fernandoblanco.inglesbasico.data.CompaneroData
import org.fernandoblanco.inglesbasico.db.entity.UsuarioEntity
import org.fernandoblanco.inglesbasico.ui.design.LoginEntrance
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
import org.fernandoblanco.inglesbasico.ui.kid.avatarEmojiParaUsuario
import org.fernandoblanco.inglesbasico.ui.theme.PlayBlue
import org.fernandoblanco.inglesbasico.ui.theme.PlayBlueDark
import org.fernandoblanco.inglesbasico.ui.theme.PlayError
import org.fernandoblanco.inglesbasico.ui.theme.PlayGreen
import org.fernandoblanco.inglesbasico.ui.theme.PlayInk
import org.fernandoblanco.inglesbasico.ui.theme.PlayPurple
import org.fernandoblanco.inglesbasico.ui.theme.PlayPurpleSoft
import org.fernandoblanco.inglesbasico.ui.theme.PlaySurface
import org.fernandoblanco.inglesbasico.ui.theme.PlayYellow
import org.fernandoblanco.inglesbasico.ui.theme.PlayYellowSoft
import org.fernandoblanco.inglesbasico.ui.viewmodel.ActividadAudioViewModel
import org.fernandoblanco.inglesbasico.ui.viewmodel.ActividadChatViewModel
import org.fernandoblanco.inglesbasico.ui.viewmodel.ActividadImagenViewModel
import org.fernandoblanco.inglesbasico.ui.viewmodel.ActividadPalabrasViewModel
import org.fernandoblanco.inglesbasico.ui.viewmodel.AuthViewModel
import org.fernandoblanco.inglesbasico.ui.viewmodel.PerfilViewModel
import org.fernandoblanco.inglesbasico.ui.viewmodel.ReportesViewModel

object Rutas {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val REGISTRO = "registro"
    const val INICIO = "inicio"
    const val PERFIL = "perfil"
    const val ACTIVIDADES = "actividades"
    const val ACT_IMAGEN = "act_imagen"
    const val ACT_AUDIO = "act_audio"
    const val ACT_PALABRAS = "act_palabras"
    const val REPORTES = "reportes"
    const val ACT_CHAT = "act_chat"
    const val SELECTOR_MASCOTA = "selector_mascota"
}

@Composable
fun InglesAppRoot() {
    val app = LocalContext.current.applicationContext as InglesApp
    val factory = remember(app) { InglesViewModelFactory(app) }
    val nav = rememberNavController()
    NavHost(
        navController = nav,
        startDestination = Rutas.SPLASH,
        enterTransition = {
            fadeIn(tween(300)) + slideInHorizontally(tween(300)) { w -> w / 14 }
        },
        exitTransition = { fadeOut(tween(220)) },
        popEnterTransition = { fadeIn(tween(280)) },
        popExitTransition = {
            fadeOut(tween(220)) + slideOutHorizontally(tween(280)) { w -> w / 14 }
        }
    ) {
        composable(Rutas.SPLASH) {
            PantallaSplash(app = app, nav = nav)
        }
        composable(Rutas.LOGIN) {
            PantallaLogin(factory = factory, nav = nav)
        }
        composable(Rutas.REGISTRO) {
            PantallaRegistro(factory = factory, nav = nav)
        }
        composable(Rutas.INICIO) {
            PantallaInicio(app = app, nav = nav, factory = factory)
        }
        composable(Rutas.PERFIL) {
            PantallaPerfil(factory = factory, nav = nav)
        }
        composable(Rutas.ACTIVIDADES) {
            PantallaListaActividades(nav = nav, factory = factory)
        }
        composable(Rutas.ACT_IMAGEN) {
            PantallaActividadImagen(factory = factory, nav = nav)
        }
        composable(Rutas.ACT_AUDIO) {
            PantallaActividadAudio(factory = factory, nav = nav)
        }
        composable(Rutas.ACT_PALABRAS) {
            PantallaActividadPalabras(factory = factory, nav = nav)
        }
        composable(Rutas.REPORTES) {
            PantallaReportes(factory = factory, nav = nav)
        }
        composable(Rutas.ACT_CHAT) {
            PantallaActividadChat(factory = factory, nav = nav)
        }
        composable(Rutas.SELECTOR_MASCOTA) {
            PantallaSelectorMascota(factory = factory, nav = nav)
        }
    }
}

@Composable
private fun PantallaSplash(app: InglesApp, nav: NavHostController) {
    LaunchedEffect(Unit) {
        if (app.sesion.usuarioIdActivo != null) {
            nav.navigate(Rutas.INICIO) { popUpTo(Rutas.SPLASH) { inclusive = true } }
        } else {
            nav.navigate(Rutas.LOGIN) { popUpTo(Rutas.SPLASH) { inclusive = true } }
        }
    }
    KidGameBackground {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("📚", style = MaterialTheme.typography.displayLarge)
            Spacer(Modifier.height(12.dp))
            Text(
                "Inglés Divertido",
                style = MaterialTheme.typography.headlineMedium,
                color = PlayPurple,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(20.dp))
            CircularProgressIndicator(color = PlayBlue, strokeWidth = 3.dp)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PantallaLogin(factory: InglesViewModelFactory, nav: NavHostController) {
    val vm: AuthViewModel = viewModel(factory = factory)
    val mensaje by vm.mensaje.collectAsState()
    val cargando by vm.cargando.collectAsState()
    val snack = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var usuario by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }

    LaunchedEffect(mensaje) {
        mensaje?.let {
            scope.launch { snack.showSnackbar(it) }
            vm.limpiarMensaje()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snack) },
        containerColor = Color.Transparent
    ) { pad ->
        PlayScreenGradient(Modifier.fillMaxSize()) {
            Box(Modifier.fillMaxSize().padding(pad)) {
                LoginEntrance {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 24.dp, vertical = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(18.dp)
                    ) {
                        Spacer(Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .shadow(16.dp, CircleShape, spotColor = PlayBlue.copy(0.25f))
                                .clip(CircleShape)
                                .background(PlaySurface),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🦊", style = MaterialTheme.typography.displayLarge)
                        }
                        Text(
                            "¡Hola, pequeño explorador!",
                            style = MaterialTheme.typography.headlineMedium,
                            color = PlayInk,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Entra para seguir aprendiendo inglés",
                            style = MaterialTheme.typography.bodyLarge,
                            color = PlayInk.copy(alpha = 0.72f),
                            textAlign = TextAlign.Center
                        )
                        OutlinedTextField(
                            value = usuario,
                            onValueChange = { usuario = it },
                            label = { Text("Tu usuario") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(22.dp),
                            colors = playTextFieldColors()
                        )
                        OutlinedTextField(
                            value = contrasena,
                            onValueChange = { contrasena = it },
                            label = { Text("Contraseña") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation(),
                            shape = RoundedCornerShape(22.dp),
                            colors = playTextFieldColors()
                        )
                        Spacer(Modifier.height(4.dp))
                        PlaySolidButton(
                            text = "Entrar",
                            onClick = {
                                vm.iniciarSesion(usuario, contrasena) {
                                    nav.navigate(Rutas.INICIO) {
                                        popUpTo(Rutas.LOGIN) { inclusive = true }
                                    }
                                }
                            },
                            enabled = !cargando,
                            loading = cargando,
                            containerColor = PlayBlue
                        )
                        PlayOutlineButton(
                            text = "Crear cuenta nueva",
                            onClick = { nav.navigate(Rutas.REGISTRO) },
                            borderColor = PlayPurple
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PantallaRegistro(factory: InglesViewModelFactory, nav: NavHostController) {
    val vm: AuthViewModel = viewModel(factory = factory)
    val mensaje by vm.mensaje.collectAsState()
    val cargando by vm.cargando.collectAsState()
    val snack = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var usuario by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }

    LaunchedEffect(mensaje) {
        mensaje?.let {
            scope.launch { snack.showSnackbar(it) }
            vm.limpiarMensaje()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snack) },
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text("Nueva cuenta", fontWeight = FontWeight.Bold, color = PlayInk) },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = PlayInk)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
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
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "Crea tu usuario y empieza la aventura",
                    style = MaterialTheme.typography.titleMedium,
                    color = PlayInk.copy(alpha = 0.8f)
                )
                OutlinedTextField(
                    value = usuario,
                    onValueChange = { usuario = it },
                    label = { Text("Usuario (único)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(22.dp),
                    colors = playTextFieldColors()
                )
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Cómo te llamamos") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(22.dp),
                    colors = playTextFieldColors()
                )
                OutlinedTextField(
                    value = contrasena,
                    onValueChange = { contrasena = it },
                    label = { Text("Contraseña (mín. 4)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    shape = RoundedCornerShape(22.dp),
                    colors = playTextFieldColors()
                )
                Spacer(Modifier.height(8.dp))
                PlaySolidButton(
                    text = "¡Listo, a jugar!",
                    onClick = {
                        vm.registrar(usuario, nombre, contrasena) {
                            nav.navigate(Rutas.INICIO) {
                                popUpTo(Rutas.LOGIN) { inclusive = true }
                            }
                        }
                    },
                    enabled = !cargando,
                    loading = cargando,
                    containerColor = PlayPurple
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PantallaInicio(
    app: InglesApp,
    nav: NavHostController,
    factory: InglesViewModelFactory
) {
    val perfilVm: PerfilViewModel = viewModel(factory = factory)
    val perfil by perfilVm.perfil.collectAsState()
    val uid = app.sesion.usuarioIdActivo ?: 0L
    val emoji = avatarEmojiParaUsuario(uid)

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "¡Hola${perfil?.nombreMostrar?.let { ", $it" } ?: ""}!",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = PlayInk
                        )
                        Text(
                            "¿Qué hacemos hoy?",
                            style = MaterialTheme.typography.bodyMedium,
                            color = PlayInk.copy(alpha = 0.65f)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { nav.navigate(Rutas.PERFIL) },
                        modifier = Modifier.padding(end = 4.dp)
                    ) {
                        PerfilIconoMini(
                            uri = perfil?.avatarUri?.let { Uri.parse(it) },
                            emoji = emoji
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PlaySurface.copy(alpha = 0.92f),
                    titleContentColor = PlayInk,
                    actionIconContentColor = PlayInk
                )
            )
        }
    ) { pad ->
        PlayScreenGradient(Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(pad)
                    .padding(horizontal = 20.dp, vertical = 16.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                perfil?.let { p ->
                    if (p.rachaActual > 0) {
                        val shape = RoundedCornerShape(20.dp)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(10.dp, shape, spotColor = PlayYellow.copy(0.3f))
                                .clip(shape)
                                .background(PlayYellowSoft)
                                .padding(16.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text("🔥", style = MaterialTheme.typography.headlineMedium)
                                Column {
                                    Text(
                                        "¡${p.rachaActual} días seguidos!",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = PlayInk
                                    )
                                    Text(
                                        "Récord: ${p.rachaMaxima} días · ¡No pierdas tu racha!",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = PlayInk.copy(alpha = 0.7f)
                                    )
                                }
                            }
                        }
                    }
                }

                perfil?.let { p ->
                    val companero = CompaneroData.obtenerPorId(p.mascotaId)
                    val shape = RoundedCornerShape(20.dp)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(10.dp, shape, spotColor = PlayPurple.copy(0.2f))
                            .clip(shape)
                            .background(PlaySurface)
                            .padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(companero.emoji, style = MaterialTheme.typography.headlineLarge)
                            Column {
                                Text(
                                    companero.nombre,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = PlayInk
                                )
                                Text(
                                    companero.frasesInicio.random(),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = PlayInk.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }
                }

                PlayNavCard(
                    emoji = "🎮",
                    titulo = "Actividades",
                    subtitulo = "Imagen, audio y palabras — 10 preguntas",
                    accentStart = PlayBlue,
                    accentEnd = PlayPurple,
                    nivel = null,
                    onClick = { nav.navigate(Rutas.ACTIVIDADES) }
                )
                PlayNavCard(
                    emoji = "📊",
                    titulo = "Reportes",
                    subtitulo = "Tu progreso y logros",
                    accentStart = PlayYellow,
                    accentEnd = PlayGreen,
                    nivel = null,
                    onClick = { nav.navigate(Rutas.REPORTES) }
                )
                Spacer(Modifier.height(4.dp))
                PlayOutlineButton(
                    text = "Cambiar compañero de aventura",
                    onClick = { nav.navigate(Rutas.SELECTOR_MASCOTA) },
                    borderColor = PlayPurple
                )
                PlayOutlineButton(
                    text = "Cerrar sesión",
                    onClick = {
                        app.sesion.cerrarSesion()
                        nav.navigate(Rutas.LOGIN) { popUpTo(0) { inclusive = true } }
                    },
                    borderColor = PlayInk.copy(alpha = 0.35f)
                )
            }
        }
    }
}

@Composable
private fun PerfilIconoMini(uri: Uri?, emoji: String) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .shadow(10.dp, CircleShape, spotColor = PlayPurple.copy(0.35f))
            .clip(CircleShape)
            .background(PlayYellowSoft),
        contentAlignment = Alignment.Center
    ) {
        if (uri != null) {
            val ctx = LocalContext.current
            AsyncImage(
                model = ImageRequest.Builder(ctx).data(uri).crossfade(true).build(),
                contentDescription = "Perfil",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Text(emoji, style = MaterialTheme.typography.headlineSmall)
        }
    }
}

@Composable
private fun PlayNavCard(
    emoji: String,
    titulo: String,
    subtitulo: String,
    accentStart: Color,
    accentEnd: Color,
    nivel: Int?,
    onClick: () -> Unit
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.97f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium),
        label = "navCard"
    )
    val shape = RoundedCornerShape(28.dp)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .shadow(elevation = 20.dp, shape = shape, ambientColor = accentStart.copy(alpha = 0.22f), spotColor = accentStart.copy(alpha = 0.32f))
            .clip(shape)
            .background(PlaySurface)
            .clickable(interactionSource = interaction, indication = null) { onClick() }
    ) {
        Row(Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
            Box(
                Modifier
                    .width(12.dp)
                    .fillMaxHeight()
                    .background(Brush.verticalGradient(colors = listOf(accentStart, accentEnd)))
            )
            Column(
                Modifier.padding(horizontal = 18.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (nivel != null) {
                    Box(
                        Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(accentStart.copy(alpha = 0.16f))
                            .padding(horizontal = 12.dp, vertical = 5.dp)
                    ) {
                        Text(
                            "Nivel $nivel",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = accentStart
                        )
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(emoji, style = MaterialTheme.typography.displaySmall)
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(titulo, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = PlayInk)
                        Text(subtitulo, style = MaterialTheme.typography.bodyLarge, color = PlayInk.copy(alpha = 0.68f))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PantallaPerfil(factory: InglesViewModelFactory, nav: NavHostController) {
    val vm: PerfilViewModel = viewModel(factory = factory)
    val perfil by vm.perfil.collectAsState()
    val nombre by vm.nombreMostrar.collectAsState()
    val usuario by vm.usuario.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    val snack = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var nuevaContrasena by remember { mutableStateOf("") }
    var mostrarEliminar by remember { mutableStateOf(false) }
    val ctx = LocalContext.current
    val pick = rememberLauncherForActivityResult(PickVisualMedia()) { uri: Uri? ->
        vm.guardarAvatar(uri) { scope.launch { snack.showSnackbar("¡Foto guardada!") } }
    }

    LaunchedEffect(mensaje) {
        mensaje?.let {
            scope.launch { snack.showSnackbar(it) }
            vm.limpiarMensaje()
        }
    }

    if (mostrarEliminar) {
        AlertDialog(
            onDismissRequest = { mostrarEliminar = false },
            title = { Text("Eliminar perfil") },
            text = { Text("Se borrarán tus datos en este dispositivo. ¿Seguro?") },
            confirmButton = {
                TextButton(onClick = {
                    mostrarEliminar = false
                    vm.eliminarCuenta {
                        nav.navigate(Rutas.LOGIN) { popUpTo(0) { inclusive = true } }
                    }
                }) { Text("Eliminar") }
            },
            dismissButton = {
                TextButton(onClick = { mostrarEliminar = false }) { Text("Cancelar") }
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snack) },
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text("Mi perfil", fontWeight = FontWeight.Bold, color = PlayInk) },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = PlayInk)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PlaySurface.copy(alpha = 0.94f))
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
                val uid = perfil?.id ?: 0L
                val emoji = avatarEmojiParaUsuario(uid)
                Box(
                    modifier = Modifier
                        .size(128.dp)
                        .shadow(16.dp, CircleShape, spotColor = PlayBlue.copy(0.35f))
                        .clip(CircleShape)
                        .background(PlayPurpleSoft)
                        .clickable { pick.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly)) },
                    contentAlignment = Alignment.Center
                ) {
                    val u = perfil?.avatarUri?.let { Uri.parse(it) }
                    if (u != null) {
                        AsyncImage(
                            model = ImageRequest.Builder(ctx).data(u).crossfade(true).build(),
                            contentDescription = "Avatar",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Text(emoji, style = MaterialTheme.typography.displayLarge)
                    }
                }
                Text(
                    "Toca para cambiar tu foto",
                    style = MaterialTheme.typography.bodyLarge,
                    color = PlayInk.copy(alpha = 0.65f),
                    textAlign = TextAlign.Center
                )
                PlayOutlineButton(
                    text = "Avatar divertido al azar",
                    onClick = { vm.quitarAvatar { scope.launch { snack.showSnackbar("¡Listo!") } } },
                    borderColor = PlayPurple
                )
                OutlinedTextField(
                    value = usuario,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Usuario") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = playTextFieldColors()
                )
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { vm.setNombreMostrar(it) },
                    label = { Text("Nombre para mostrar") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(22.dp),
                    colors = playTextFieldColors()
                )
                OutlinedTextField(
                    value = nuevaContrasena,
                    onValueChange = { nuevaContrasena = it },
                    label = { Text("Nueva contraseña (opcional)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    shape = RoundedCornerShape(22.dp),
                    colors = playTextFieldColors()
                )
                PlaySolidButton(
                    text = "Guardar cambios",
                    onClick = {
                        val pwd = nuevaContrasena.takeIf { it.isNotBlank() }
                        vm.guardar(pwd) {
                            scope.launch { snack.showSnackbar("¡Perfil actualizado!") }
                            nuevaContrasena = ""
                        }
                    },
                    containerColor = PlayGreen
                )
                PlayOutlineButton(
                    text = "Eliminar perfil",
                    onClick = { mostrarEliminar = true },
                    borderColor = PlayError,
                    textColor = PlayError
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PantallaListaActividades(nav: NavHostController, factory: InglesViewModelFactory) {
    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text("Tus niveles", fontWeight = FontWeight.Bold, color = PlayInk) },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = PlayInk)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PlaySurface.copy(alpha = 0.94f))
            )
        }
    ) { pad ->
        PlayScreenGradient(Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(pad)
                    .padding(20.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                Text(
                    "10 preguntas por juego — al final verás tu puntuación",
                    style = MaterialTheme.typography.titleMedium,
                    color = PlayInk.copy(alpha = 0.72f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                PlayNavCard(
                    emoji = "🖼️",
                    titulo = "Juego de imágenes",
                    subtitulo = "¿Cuál es la palabra en inglés?",
                    accentStart = PlayBlue,
                    accentEnd = PlayBlueDark,
                    nivel = 1,
                    onClick = { nav.navigate(Rutas.ACT_IMAGEN) }
                )
                PlayNavCard(
                    emoji = "👂",
                    titulo = "Juego de audio",
                    subtitulo = "Escucha y elige la respuesta",
                    accentStart = PlayPurple,
                    accentEnd = PlayYellow,
                    nivel = 2,
                    onClick = { nav.navigate(Rutas.ACT_AUDIO) }
                )
                PlayNavCard(
                    emoji = "✏️",
                    titulo = "Completar palabras",
                    subtitulo = "Elige la palabra que falta",
                    accentStart = PlayGreen,
                    accentEnd = PlayBlue,
                    nivel = 3,
                    onClick = { nav.navigate(Rutas.ACT_PALABRAS) }
                )
                PlayNavCard(
                    emoji = "🤖",
                    titulo = "Chat con el Tutor IA",
                    subtitulo = "Conversa y aprende con tu compañero",
                    accentStart = PlayPurple,
                    accentEnd = PlayGreen,
                    nivel = 4,
                    onClick = { nav.navigate(Rutas.ACT_CHAT) }
                )
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

    LaunchedEffect(Unit) {
        vm.sonido.collect { ok ->
            if (ok) KidFeedback.playCorrect(ctx) else KidFeedback.playIncorrect(ctx)
        }
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text("Imágenes", fontWeight = FontWeight.Bold, color = PlayInk) },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = PlayInk)
                    }
                },
                actions = {
                    TextButton(onClick = { vm.reiniciar() }) {
                        Text("Nueva sesión", color = PlayBlue, fontWeight = FontWeight.SemiBold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PlaySurface, titleContentColor = PlayInk)
            )
        }
    ) { pad ->
        KidGameBackground {
            Box(Modifier.padding(pad).fillMaxSize()) {
                if (fin != null) {
                    Column(
                        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        KidFinSesion(aciertos = fin!!.first, total = fin!!.second, onJugarOtra = { vm.reiniciar() }, onSalir = { nav.popBackStack() })
                    }
                } else if (pregunta != null) {
                    key(indice, pregunta!!.correctaEn) {
                        Column(
                            Modifier.padding(16.dp).fillMaxSize().verticalScroll(rememberScrollState()),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            GamePlayContentCard {
                                KidSessionProgress(indice + 1, ActividadImagenViewModel.PREGUNTAS_POR_SESION)
                                Spacer(Modifier.height(8.dp))
                                Text("¿Cuál es en inglés?", style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                                Box(Modifier.fillMaxWidth().padding(vertical = 12.dp), contentAlignment = Alignment.Center) {
                                    Text(pregunta!!.emoji, style = MaterialTheme.typography.displayLarge.copy(fontSize = MaterialTheme.typography.displayLarge.fontSize * 1.55f))
                                }
                            }
                            GameFeedbackBlock(texto = feedback, esCorrecto = feedbackOk, solucionCorrecta = solucion)
                            pregunta!!.opciones.chunked(2).forEach { fila ->
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                    fila.forEach { op ->
                                        KidOptionButton(label = op, enabled = !procesando, onClick = { vm.responder(op) }, modifier = Modifier.weight(1f))
                                    }
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

    LaunchedEffect(Unit) {
        vm.sonido.collect { ok ->
            if (ok) KidFeedback.playCorrect(ctx) else KidFeedback.playIncorrect(ctx)
        }
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text("Audio", fontWeight = FontWeight.Bold, color = PlayInk) },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = PlayInk)
                    }
                },
                actions = {
                    TextButton(onClick = { vm.reiniciar() }) {
                        Text("Nueva sesión", color = PlayPurple, fontWeight = FontWeight.SemiBold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PlaySurface, titleContentColor = PlayInk)
            )
        }
    ) { pad ->
        KidGameBackground {
            Box(Modifier.padding(pad).fillMaxSize()) {
                if (fin != null) {
                    Column(
                        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        KidFinSesion(aciertos = fin!!.first, total = fin!!.second, onJugarOtra = { vm.reiniciar() }, onSalir = { nav.popBackStack() })
                    }
                } else if (pregunta != null) {
                    key(indice, pregunta!!.palabraIngles) {
                        Column(
                            Modifier.padding(16.dp).fillMaxSize().verticalScroll(rememberScrollState()),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            GamePlayContentCard {
                                KidSessionProgress(indice + 1, ActividadAudioViewModel.PREGUNTAS_POR_SESION)
                                Spacer(Modifier.height(8.dp))
                                Text("Escucha y elige en inglés", style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                                KidListenButton(enabled = listo, onClick = { vm.reproducir() })
                            }
                            GameFeedbackBlock(texto = feedback, esCorrecto = feedbackOk, solucionCorrecta = solucion)
                            pregunta!!.opcionesIngles.forEach { op ->
                                KidOptionButton(label = op, enabled = !procesando, onClick = { vm.responder(op) }, modifier = Modifier.fillMaxWidth())
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

    LaunchedEffect(Unit) {
        vm.sonido.collect { ok ->
            if (ok) KidFeedback.playCorrect(ctx) else KidFeedback.playIncorrect(ctx)
        }
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text("Palabras", fontWeight = FontWeight.Bold, color = PlayInk) },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = PlayInk)
                    }
                },
                actions = {
                    TextButton(onClick = { vm.reiniciar() }) {
                        Text("Nueva sesión", color = PlayGreen, fontWeight = FontWeight.SemiBold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PlaySurface, titleContentColor = PlayInk)
            )
        }
    ) { pad ->
        KidGameBackground {
            Box(Modifier.padding(pad).fillMaxSize()) {
                if (fin != null) {
                    Column(
                        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        KidFinSesion(aciertos = fin!!.first, total = fin!!.second, onJugarOtra = { vm.reiniciar() }, onSalir = { nav.popBackStack() })
                    }
                } else if (pregunta != null) {
                    key(indice, pregunta!!.correcta) {
                        Column(
                            Modifier.padding(16.dp).fillMaxSize().verticalScroll(rememberScrollState()),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            GamePlayContentCard {
                                KidSessionProgress(indice + 1, ActividadPalabrasViewModel.PREGUNTAS_POR_SESION)
                                Spacer(Modifier.height(8.dp))
                                Text("Completa la palabra en inglés", style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                                Text(
                                    pregunta!!.incompleta,
                                    style = MaterialTheme.typography.displayMedium,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    color = PlayBlue,
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                                )
                            }
                            GameFeedbackBlock(texto = feedback, esCorrecto = feedbackOk, solucionCorrecta = solucion)
                            pregunta!!.opciones.forEach { w ->
                                KidOptionButton(label = w, enabled = !procesando, onClick = { vm.responder(w) }, modifier = Modifier.fillMaxWidth())
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
private fun PantallaReportes(factory: InglesViewModelFactory, nav: NavHostController) {
    val vm: ReportesViewModel = viewModel(factory = factory)
    val u by vm.usuario.collectAsState()

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text("Tus logros", fontWeight = FontWeight.Bold, color = PlayInk) },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = PlayInk)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PlaySurface.copy(alpha = 0.94f))
            )
        }
    ) { pad ->
        PlayScreenGradient(Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(pad)
                    .padding(20.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                if (u == null) {
                    Text("No hay datos de usuario.", style = MaterialTheme.typography.titleMedium, color = PlayInk.copy(alpha = 0.65f), textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                } else {
                    val user = u!!
                    val heroShape = RoundedCornerShape(28.dp)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(22.dp, heroShape, spotColor = PlayPurple.copy(alpha = 0.3f))
                            .clip(heroShape)
                            .background(Brush.horizontalGradient(colors = listOf(PlayBlue, PlayPurple)))
                            .padding(24.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("⭐ Nivel ${user.nivel}", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("Puntos: ${user.puntajeTotal}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold, color = PlayYellow)
                            if (user.rachaActual > 0) {
                                Text("🔥 Racha: ${user.rachaActual} días (Récord: ${user.rachaMaxima})", style = MaterialTheme.typography.bodyLarge, color = Color.White.copy(alpha = 0.9f))
                            }
                        }
                    }
                    Text("Rendimiento por juego", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = PlayInk)
                    BarraJuegoKid("🖼️ Imágenes", user.aciertosImagen, user.partidasImagen, PlayBlue)
                    BarraJuegoKid("👂 Audio", user.aciertosAudio, user.partidasAudio, PlayPurple)
                    BarraJuegoKid("✏️ Palabras", user.aciertosPalabras, user.partidasPalabras, PlayGreen)
                    val mejoras = areasMejora(user)
                    val tipShape = RoundedCornerShape(26.dp)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(16.dp, tipShape, spotColor = PlayYellow.copy(alpha = 0.35f))
                            .clip(tipShape)
                            .background(PlayYellowSoft)
                            .padding(20.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text("Consejos para mejorar", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = PlayInk)
                            if (mejoras.isEmpty()) {
                                Text("¡Genial! Sigue jugando los tres modos para subir de nivel.", style = MaterialTheme.typography.bodyLarge, color = PlayInk.copy(alpha = 0.72f))
                            } else {
                                mejoras.forEach { linea ->
                                    Text("• $linea", style = MaterialTheme.typography.bodyLarge, color = PlayInk.copy(alpha = 0.78f))
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
private fun PantallaSelectorMascota(factory: InglesViewModelFactory, nav: NavHostController) {
    val scope = rememberCoroutineScope()
    val ctx = LocalContext.current
    val app = ctx.applicationContext as InglesApp

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text("Elige tu compañero", fontWeight = FontWeight.Bold, color = PlayInk) },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = PlayInk)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PlaySurface.copy(alpha = 0.94f))
            )
        }
    ) { pad ->
        PlayScreenGradient(Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(pad)
                    .padding(20.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "¿Quién será tu compañero de aventura?",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = PlayInk,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                CompaneroData.todos.forEach { companero ->
                    val shape = RoundedCornerShape(24.dp)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(12.dp, shape, spotColor = PlayPurple.copy(0.2f))
                            .clip(shape)
                            .background(PlaySurface)
                            .clickable {
                                scope.launch {
                                    val id = app.sesion.usuarioIdActivo ?: return@launch
                                    app.repositorioUsuario.guardarMascota(id, companero.id)
                                    nav.popBackStack()
                                }
                            }
                            .padding(20.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(72.dp)
                                    .clip(CircleShape)
                                    .background(PlayYellowSoft),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(companero.emoji, style = MaterialTheme.typography.displaySmall)
                            }
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(companero.nombre, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = PlayInk)
                                Text(companero.frasesBienvenida.first(), style = MaterialTheme.typography.bodyMedium, color = PlayInk.copy(alpha = 0.7f))
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

    val scale by animateFloatAsState(
        targetValue = if (emocion.animacion == "happy" || emocion.animacion == "celebrate") 1.18f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "mascota"
    )

    LaunchedEffect(error) {
        error?.let {
            scope.launch { snack.showSnackbar(it) }
            vm.limpiarError()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snack) },
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text("Chat con ${companero.nombre}", fontWeight = FontWeight.Bold, color = PlayInk) },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = PlayInk)
                    }
                },
                actions = {
                    TextButton(onClick = { vm.reiniciar() }) {
                        Text("Nueva sesión", color = PlayPurple, fontWeight = FontWeight.SemiBold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PlaySurface, titleContentColor = PlayInk)
            )
        }
    ) { pad ->
        KidGameBackground {
            Column(
                modifier = Modifier
                    .padding(pad)
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                KidSessionProgress(turno + 1, ActividadChatViewModel.TURNOS_POR_SESION)

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(12.dp, RoundedCornerShape(24.dp), spotColor = PlayPurple.copy(0.2f))
                        .clip(RoundedCornerShape(24.dp))
                        .background(PlaySurface)
                        .padding(16.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            emocion.emoji,
                            style = MaterialTheme.typography.displaySmall,
                            modifier = Modifier.scale(scale)
                        )
                        if (cargando) {
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text(
                                    "${companero.nombre} está pensando...",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = PlayInk.copy(alpha = 0.7f)
                                )
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = PlayPurple,
                                    strokeWidth = 2.dp
                                )
                            }
                        } else if (ultimoMensaje != null) {
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text(
                                    ultimoMensaje.textoIngles,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.SemiBold,
                                    color = PlayInk
                                )
                                Text(
                                    ultimoMensaje.textoEspanol,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = PlayInk.copy(alpha = 0.6f)
                                )
                            }
                        }
                    }
                }

                if (fin) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        KidFinSesion(
                            aciertos = aciertos,
                            total = ActividadChatViewModel.TURNOS_POR_SESION,
                            onJugarOtra = { vm.reiniciar() },
                            onSalir = { nav.popBackStack() }
                        )
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ultimoMensaje?.opciones?.forEach { opcion ->
                            KidOptionButton(
                                label = opcion,
                                enabled = !cargando,
                                onClick = { vm.responderOpcion(opcion) },
                                modifier = Modifier.fillMaxWidth()
                            )
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
    val animated by animateFloatAsState(
        targetValue = ratio,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMediumLow),
        label = "barraProgreso"
    )
    val errores = (intentos - aciertos).coerceAtLeast(0)
    val shape = RoundedCornerShape(24.dp)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 16.dp, shape = shape, ambientColor = color.copy(alpha = 0.18f), spotColor = color.copy(alpha = 0.3f))
            .clip(shape)
            .background(PlaySurface)
    ) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(titulo, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PlayInk)
            Box(Modifier.fillMaxWidth().height(18.dp).clip(RoundedCornerShape(12.dp)).background(color.copy(alpha = 0.14f))) {
                Box(Modifier.fillMaxHeight().fillMaxWidth(animated).background(Brush.horizontalGradient(colors = listOf(color, color.copy(alpha = 0.88f)))))
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("✅ $aciertos aciertos", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold, color = PlayGreen)
                Text("❌ $errores errores", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold, color = PlayError.copy(alpha = 0.92f))
            }
            Text("${(ratio * 100).toInt()} % de aciertos", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = color)
        }
    }
}

private fun areasMejora(u: UsuarioEntity): List<String> {
    data class T(val nombre: String, val a: Int, val t: Int)
    val tipos = listOf(
        T("Imágenes", u.aciertosImagen, u.partidasImagen),
        T("Audio", u.aciertosAudio, u.partidasAudio),
        T("Palabras", u.aciertosPalabras, u.partidasPalabras)
    )
    return tipos.mapNotNull { x ->
        if (x.t < 3) return@mapNotNull null
        val r = x.a.toFloat() / x.t
        if (r < 0.55f) "Practica más el juego de ${x.nombre.lowercase()} (${(r * 100).toInt()} % acierto)"
        else null
    }
}

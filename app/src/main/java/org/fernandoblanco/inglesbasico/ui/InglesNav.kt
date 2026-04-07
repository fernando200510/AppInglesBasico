package org.fernandoblanco.inglesbasico.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import org.fernandoblanco.inglesbasico.db.entity.UsuarioEntity
import org.fernandoblanco.inglesbasico.ui.kid.KidFeedback
import org.fernandoblanco.inglesbasico.ui.kid.KidFeedbackBanner
import org.fernandoblanco.inglesbasico.ui.kid.KidFinSesion
import org.fernandoblanco.inglesbasico.ui.kid.KidGameBackground
import org.fernandoblanco.inglesbasico.ui.kid.KidListenButton
import org.fernandoblanco.inglesbasico.ui.kid.KidOptionButton
import org.fernandoblanco.inglesbasico.ui.kid.KidSessionProgress
import org.fernandoblanco.inglesbasico.ui.kid.avatarEmojiParaUsuario
import org.fernandoblanco.inglesbasico.ui.theme.KidCoral
import org.fernandoblanco.inglesbasico.ui.theme.KidMint
import org.fernandoblanco.inglesbasico.ui.theme.KidPurple
import org.fernandoblanco.inglesbasico.ui.theme.KidSky
import org.fernandoblanco.inglesbasico.ui.theme.KidSun
import org.fernandoblanco.inglesbasico.ui.theme.KidTurquoise
import org.fernandoblanco.inglesbasico.ui.viewmodel.ActividadAudioViewModel
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
}

@Composable
fun InglesAppRoot() {
    val app = LocalContext.current.applicationContext as InglesApp
    val factory = remember(app) { InglesViewModelFactory(app) }
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = Rutas.SPLASH) {
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
            PantallaListaActividades(nav = nav)
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
    }
}

@Composable
private fun PantallaSplash(app: InglesApp, nav: NavHostController) {
    LaunchedEffect(Unit) {
        if (app.sesion.usuarioIdActivo != null) {
            nav.navigate(Rutas.INICIO) {
                popUpTo(Rutas.SPLASH) { inclusive = true }
            }
        } else {
            nav.navigate(Rutas.LOGIN) {
                popUpTo(Rutas.SPLASH) { inclusive = true }
            }
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
                color = KidPurple
            )
            Spacer(Modifier.height(20.dp))
            CircularProgressIndicator(color = KidTurquoise)
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
        KidGameBackground {
            Column(
                modifier = Modifier
                    .padding(pad)
                    .padding(24.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(24.dp))
                Text("🌟", style = MaterialTheme.typography.displayMedium)
                Text(
                    "¡Hola de nuevo!",
                    style = MaterialTheme.typography.headlineMedium,
                    color = KidPurple,
                    textAlign = TextAlign.Center
                )
                Text(
                    "Escribe tu usuario y contraseña para entrar al menú.",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
                OutlinedTextField(
                    value = usuario,
                    onValueChange = { usuario = it },
                    label = { Text("Usuario") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp)
                )
                OutlinedTextField(
                    value = contrasena,
                    onValueChange = { contrasena = it },
                    label = { Text("Contraseña") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    shape = RoundedCornerShape(16.dp)
                )
                Button(
                    onClick = {
                        vm.iniciarSesion(usuario, contrasena) {
                            nav.navigate(Rutas.INICIO) {
                                popUpTo(Rutas.LOGIN) { inclusive = true }
                            }
                        }
                    },
                    enabled = !cargando,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = KidTurquoise)
                ) {
                    if (cargando) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(28.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Entrar al juego", style = MaterialTheme.typography.titleMedium)
                    }
                }
                OutlinedButton(
                    onClick = { nav.navigate(Rutas.REGISTRO) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(20.dp)
                ) { Text("Crear perfil nuevo") }
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
        topBar = {
            TopAppBar(
                title = { Text("Crear perfil") },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = KidSun.copy(alpha = 0.35f)
                )
            )
        }
    ) { pad ->
        Column(
            modifier = Modifier
                .padding(pad)
                .padding(24.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                "¡Elige un usuario y contraseña para guardar tu progreso!",
                style = MaterialTheme.typography.bodyLarge
            )
            OutlinedTextField(
                value = usuario,
                onValueChange = { usuario = it },
                label = { Text("Usuario (único)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(16.dp)
            )
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Tu nombre") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(16.dp)
            )
            OutlinedTextField(
                value = contrasena,
                onValueChange = { contrasena = it },
                label = { Text("Contraseña (mín. 4)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                shape = RoundedCornerShape(16.dp)
            )
            Button(
                onClick = {
                    vm.registrar(usuario, nombre, contrasena) {
                        nav.navigate(Rutas.INICIO) {
                            popUpTo(Rutas.LOGIN) { inclusive = true }
                        }
                    }
                },
                enabled = !cargando,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = KidPurple)
            ) {
                if (cargando) CircularProgressIndicator(
                    modifier = Modifier.size(26.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
                else Text("¡Guardar y jugar!", style = MaterialTheme.typography.titleMedium)
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
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            "Elige qué quieres hacer hoy",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
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
                    containerColor = KidMint.copy(alpha = 0.6f)
                )
            )
        }
    ) { pad ->
        KidGameBackground {
            Column(
                modifier = Modifier
                    .padding(pad)
                    .padding(horizontal = 20.dp, vertical = 16.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                TarjetaMenuKid(
                    emoji = "🎮",
                    titulo = "Actividades",
                    subtitulo = "Imagen, audio y palabras",
                    fondo = KidSun.copy(alpha = 0.45f),
                    onClick = { nav.navigate(Rutas.ACTIVIDADES) }
                )
                TarjetaMenuKid(
                    emoji = "📊",
                    titulo = "Reportes",
                    subtitulo = "Tu progreso y retos",
                    fondo = KidSky.copy(alpha = 0.35f),
                    onClick = { nav.navigate(Rutas.REPORTES) }
                )
                Spacer(Modifier.height(8.dp))
                OutlinedButton(
                    onClick = {
                        app.sesion.cerrarSesion()
                        nav.navigate(Rutas.LOGIN) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp)
                ) { Text("Salir") }
            }
        }
    }
}

@Composable
private fun PerfilIconoMini(uri: Uri?, emoji: String) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(KidPurple.copy(alpha = 0.2f)),
        contentAlignment = Alignment.Center
    ) {
        if (uri != null) {
            val ctx = LocalContext.current
            AsyncImage(
                model = ImageRequest.Builder(ctx)
                    .data(uri)
                    .crossfade(true)
                    .build(),
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
private fun TarjetaMenuKid(
    emoji: String,
    titulo: String,
    subtitulo: String,
    fondo: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = fondo),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            Modifier.padding(22.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(emoji, style = MaterialTheme.typography.displaySmall)
            Column {
                Text(titulo, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(subtitulo, style = MaterialTheme.typography.bodyLarge)
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
        vm.guardarAvatar(uri) {
            scope.launch { snack.showSnackbar("¡Foto guardada!") }
        }
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
                        nav.navigate(Rutas.LOGIN) {
                            popUpTo(0) { inclusive = true }
                        }
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
        topBar = {
            TopAppBar(
                title = { Text("Mi perfil") },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = KidLavenderCompat()
                )
            )
        }
    ) { pad ->
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
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(KidMint)
                    .clickable {
                        pick.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
                    },
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
                    Text(emoji, style = MaterialTheme.typography.displayMedium)
                }
            }
            Text(
                "Toca el círculo para elegir foto",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
            OutlinedButton(
                onClick = {
                    vm.quitarAvatar {
                        scope.launch { snack.showSnackbar("Avatar aleatorio activado") }
                    }
                },
                shape = RoundedCornerShape(16.dp)
            ) { Text("Usar avatar divertido") }

            OutlinedTextField(
                value = usuario,
                onValueChange = {},
                readOnly = true,
                label = { Text("Usuario") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            )
            OutlinedTextField(
                value = nombre,
                onValueChange = { vm.setNombreMostrar(it) },
                label = { Text("Nombre para mostrar") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(16.dp)
            )
            OutlinedTextField(
                value = nuevaContrasena,
                onValueChange = { nuevaContrasena = it },
                label = { Text("Nueva contraseña (opcional)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                shape = RoundedCornerShape(16.dp)
            )
            Button(
                onClick = {
                    val pwd = nuevaContrasena.takeIf { it.isNotBlank() }
                    vm.guardar(pwd) {
                        scope.launch { snack.showSnackbar("¡Perfil actualizado!") }
                        nuevaContrasena = ""
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = KidTurquoise)
            ) { Text("Guardar cambios") }
            OutlinedButton(
                onClick = { mostrarEliminar = true },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = KidCoral)
            ) { Text("Eliminar perfil") }
        }
    }
}

@Composable
private fun KidLavenderCompat(): Color =
    org.fernandoblanco.inglesbasico.ui.theme.KidLavender.copy(alpha = 0.85f)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PantallaListaActividades(nav: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Actividades") },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = KidSun.copy(alpha = 0.4f)
                )
            )
        }
    ) { pad ->
        KidGameBackground {
            Column(
                modifier = Modifier
                    .padding(pad)
                    .padding(20.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    "Cada juego tiene 10 preguntas y ¡premio al final!",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                TarjetaMenuKid(
                    emoji = "🖼️",
                    titulo = "Juego de imágenes",
                    subtitulo = "¿Cuál es la palabra en inglés?",
                    fondo = KidMint.copy(alpha = 0.7f),
                    onClick = { nav.navigate(Rutas.ACT_IMAGEN) }
                )
                TarjetaMenuKid(
                    emoji = "👂",
                    titulo = "Juego de audio",
                    subtitulo = "Escucha y elige en inglés",
                    fondo = KidCoral.copy(alpha = 0.25f),
                    onClick = { nav.navigate(Rutas.ACT_AUDIO) }
                )
                TarjetaMenuKid(
                    emoji = "✏️",
                    titulo = "Completar palabras",
                    subtitulo = "Llena los espacios",
                    fondo = KidPurple.copy(alpha = 0.2f),
                    onClick = { nav.navigate(Rutas.ACT_PALABRAS) }
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
    val fin by vm.finSesion.collectAsState()
    val indice by vm.indice.collectAsState()
    val procesando by vm.procesando.collectAsState()
    val ctx = LocalContext.current
    val p = vm.preguntaActual

    LaunchedEffect(Unit) {
        vm.sonido.collect { ok ->
            if (ok) KidFeedback.playCorrect(ctx) else KidFeedback.playIncorrect(ctx)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Imágenes") },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    TextButton(onClick = { vm.reiniciar() }) { Text("Nueva sesión") }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = KidMint.copy(alpha = 0.55f)
                )
            )
        }
    ) { pad ->
        KidGameBackground {
            Box(
                Modifier
                    .padding(pad)
                    .fillMaxSize()
            ) {
                if (fin != null) {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        KidFinSesion(
                            aciertos = fin!!.first,
                            total = fin!!.second,
                            onJugarOtra = { vm.reiniciar() },
                            onSalir = { nav.popBackStack() }
                        )
                    }
                } else if (p != null) {
                    Column(
                        Modifier
                            .padding(20.dp)
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        KidSessionProgress(indice + 1, ActividadImagenViewModel.PREGUNTAS_POR_SESION)
                        Text(
                            "¿Cuál es su significado en inglés?",
                            style = MaterialTheme.typography.headlineSmall,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            p.emoji,
                            style = MaterialTheme.typography.displayLarge.copy(
                                fontSize = MaterialTheme.typography.displayLarge.fontSize * 1.5f
                            )
                        )
                        KidFeedbackBanner(feedback, feedbackOk)
                        p.opciones.chunked(2).forEach { fila ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                fila.forEach { op ->
                                    KidOptionButton(
                                        label = op,
                                        enabled = !procesando,
                                        onClick = { vm.responder(op) },
                                        modifier = Modifier.weight(1f)
                                    )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PantallaActividadAudio(factory: InglesViewModelFactory, nav: NavHostController) {
    val vm: ActividadAudioViewModel = viewModel(factory = factory)
    val feedback by vm.feedback.collectAsState()
    val feedbackOk by vm.feedbackOk.collectAsState()
    val fin by vm.finSesion.collectAsState()
    val indice by vm.indice.collectAsState()
    val listo by vm.ttsListo.collectAsState()
    val procesando by vm.procesando.collectAsState()
    val ctx = LocalContext.current
    val p = vm.preguntaActual

    LaunchedEffect(Unit) {
        vm.sonido.collect { ok ->
            if (ok) KidFeedback.playCorrect(ctx) else KidFeedback.playIncorrect(ctx)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Audio") },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    TextButton(onClick = { vm.reiniciar() }) { Text("Nueva sesión") }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = KidCoral.copy(alpha = 0.22f)
                )
            )
        }
    ) { pad ->
        KidGameBackground {
            Box(
                Modifier
                    .padding(pad)
                    .fillMaxSize()
            ) {
                if (fin != null) {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        KidFinSesion(
                            aciertos = fin!!.first,
                            total = fin!!.second,
                            onJugarOtra = { vm.reiniciar() },
                            onSalir = { nav.popBackStack() }
                        )
                    }
                } else if (p != null) {
                    Column(
                        Modifier
                            .padding(20.dp)
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        KidSessionProgress(indice + 1, ActividadAudioViewModel.PREGUNTAS_POR_SESION)
                        Text(
                            "Escucha la palabra y elige la opción correcta en inglés.",
                            style = MaterialTheme.typography.headlineSmall,
                            textAlign = TextAlign.Center
                        )
                        KidListenButton(enabled = listo, onClick = { vm.reproducir() })
                        KidFeedbackBanner(feedback, feedbackOk)
                        p.opcionesIngles.forEach { op ->
                            KidOptionButton(
                                label = op,
                                enabled = !procesando,
                                onClick = { vm.responder(op) },
                                modifier = Modifier.fillMaxWidth()
                            )
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
    val fin by vm.finSesion.collectAsState()
    val indice by vm.indice.collectAsState()
    val procesando by vm.procesando.collectAsState()
    val ctx = LocalContext.current
    val p = vm.preguntaActual

    LaunchedEffect(Unit) {
        vm.sonido.collect { ok ->
            if (ok) KidFeedback.playCorrect(ctx) else KidFeedback.playIncorrect(ctx)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Palabras") },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    TextButton(onClick = { vm.reiniciar() }) { Text("Nueva sesión") }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = KidPurple.copy(alpha = 0.2f)
                )
            )
        }
    ) { pad ->
        KidGameBackground {
            Box(
                Modifier
                    .padding(pad)
                    .fillMaxSize()
            ) {
                if (fin != null) {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        KidFinSesion(
                            aciertos = fin!!.first,
                            total = fin!!.second,
                            onJugarOtra = { vm.reiniciar() },
                            onSalir = { nav.popBackStack() }
                        )
                    }
                } else if (p != null) {
                    Column(
                        Modifier
                            .padding(20.dp)
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        KidSessionProgress(indice + 1, ActividadPalabrasViewModel.PREGUNTAS_POR_SESION)
                        Text(
                            "Completa la palabra en inglés",
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            p.incompleta,
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            color = KidTurquoise
                        )
                        KidFeedbackBanner(feedback, feedbackOk)
                        p.opciones.forEach { w ->
                            KidOptionButton(
                                label = w,
                                enabled = !procesando,
                                onClick = { vm.responder(w) },
                                modifier = Modifier.fillMaxWidth()
                            )
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
        topBar = {
            TopAppBar(
                title = { Text("Tus reportes") },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = KidSky.copy(alpha = 0.35f)
                )
            )
        }
    ) { pad ->
        KidGameBackground {
            Column(
                modifier = Modifier
                    .padding(pad)
                    .padding(20.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (u == null) {
                    Text("No hay datos de usuario.", style = MaterialTheme.typography.bodyLarge)
                } else {
                    val user = u!!
                    Card(
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(6.dp)
                    ) {
                        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("⭐ Nivel ${user.nivel}", style = MaterialTheme.typography.headlineSmall)
                            Text(
                                "Puntos totales: ${user.puntajeTotal}",
                                style = MaterialTheme.typography.titleMedium,
                                color = KidTurquoise
                            )
                        }
                    }
                    Text(
                        "Aciertos vs intentos",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    BarraJuegoKid(
                        "🖼️ Imágenes",
                        user.aciertosImagen,
                        user.partidasImagen,
                        KidTurquoise
                    )
                    BarraJuegoKid(
                        "👂 Audio",
                        user.aciertosAudio,
                        user.partidasAudio,
                        KidCoral
                    )
                    BarraJuegoKid(
                        "✏️ Palabras",
                        user.aciertosPalabras,
                        user.partidasPalabras,
                        KidPurple
                    )
                    val mejoras = areasMejora(user)
                    Card(
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = KidSun.copy(alpha = 0.45f))
                    ) {
                        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                "Áreas para mejorar",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            if (mejoras.isEmpty()) {
                                Text(
                                    "¡Vas muy bien! Sigue practicando los tres juegos por igual.",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            } else {
                                mejoras.forEach { linea ->
                                    Text("• $linea", style = MaterialTheme.typography.bodyLarge)
                                }
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
    val ratio = if (intentos == 0) 0f else aciertos.toFloat() / intentos
    val errores = (intentos - aciertos).coerceAtLeast(0)
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f))
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(titulo, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            LinearProgressIndicator(
                progress = { ratio },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(14.dp),
                color = color,
                trackColor = color.copy(alpha = 0.2f),
            )
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "✅ $aciertos aciertos",
                    style = MaterialTheme.typography.bodyLarge,
                    color = KidTurquoise
                )
                Text(
                    "❌ $errores errores",
                    style = MaterialTheme.typography.bodyLarge,
                    color = KidCoral
                )
            }
            Text(
                "${(ratio * 100).toInt()} % de acierto",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )
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

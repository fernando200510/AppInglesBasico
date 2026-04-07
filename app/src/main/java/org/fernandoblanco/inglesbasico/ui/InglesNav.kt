package org.fernandoblanco.inglesbasico.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import org.fernandoblanco.inglesbasico.InglesApp
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
            PantallaInicio(app = app, nav = nav)
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
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        Spacer(Modifier.height(16.dp))
        Text("Inglés Básico", style = MaterialTheme.typography.titleLarge)
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
        topBar = {
            TopAppBar(title = { Text("Iniciar sesión") })
        }
    ) { pad ->
        Column(
            modifier = Modifier
                .padding(pad)
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                "Accede con tu usuario y contraseña. Las contraseñas se guardan de forma segura (hash).",
                style = MaterialTheme.typography.bodyMedium
            )
            OutlinedTextField(
                value = usuario,
                onValueChange = { usuario = it },
                label = { Text("Usuario") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = contrasena,
                onValueChange = { contrasena = it },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
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
                modifier = Modifier.fillMaxWidth()
            ) {
                if (cargando) CircularProgressIndicator(
                    modifier = Modifier
                        .height(24.dp)
                        .padding(4.dp),
                    strokeWidth = 2.dp
                )
                else Text("Entrar")
            }
            OutlinedButton(
                onClick = { nav.navigate(Rutas.REGISTRO) },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Crear perfil") }
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
                }
            )
        }
    ) { pad ->
        Column(
            modifier = Modifier
                .padding(pad)
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = usuario,
                onValueChange = { usuario = it },
                label = { Text("Usuario (único)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre para mostrar") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = contrasena,
                onValueChange = { contrasena = it },
                label = { Text("Contraseña (mín. 4 caracteres)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
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
                modifier = Modifier.fillMaxWidth()
            ) {
                if (cargando) CircularProgressIndicator(
                    modifier = Modifier
                        .height(24.dp)
                        .padding(4.dp),
                    strokeWidth = 2.dp
                )
                else Text("Guardar perfil")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PantallaInicio(app: InglesApp, nav: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Inicio") },
                actions = {
                    TextButton(onClick = {
                        app.sesion.cerrarSesion()
                        nav.navigate(Rutas.LOGIN) {
                            popUpTo(0) { inclusive = true }
                        }
                    }) { Text("Salir") }
                }
            )
        }
    ) { pad ->
        Column(
            modifier = Modifier
                .padding(pad)
                .padding(24.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Elige un módulo. Tu progreso se guarda en la base de datos local (SQLite vía Room).",
                style = MaterialTheme.typography.bodyLarge
            )
            BotonModulo("Actividades", "Imagen, audio y palabras") {
                nav.navigate(Rutas.ACTIVIDADES)
            }
            BotonModulo("Reportes", "Puntaje, nivel y avance") {
                nav.navigate(Rutas.REPORTES)
            }
            BotonModulo("Mi perfil", "Editar o eliminar cuenta") {
                nav.navigate(Rutas.PERFIL)
            }
        }
    }
}

@Composable
private fun BotonModulo(titulo: String, subtitulo: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(Modifier.padding(20.dp)) {
            Text(titulo, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))
            Text(subtitulo, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PantallaPerfil(factory: InglesViewModelFactory, nav: NavHostController) {
    val vm: PerfilViewModel = viewModel(factory = factory)
    val nombre by vm.nombreMostrar.collectAsState()
    val usuario by vm.usuario.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    val snack = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var nuevaContrasena by remember { mutableStateOf("") }
    var mostrarEliminar by remember { mutableStateOf(false) }

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
            text = { Text("Se borrarán tus datos locales de este dispositivo. ¿Continuar?") },
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
                }
            )
        }
    ) { pad ->
        Column(
            modifier = Modifier
                .padding(pad)
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = usuario,
                onValueChange = {},
                readOnly = true,
                label = { Text("Usuario") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = nombre,
                onValueChange = { vm.setNombreMostrar(it) },
                label = { Text("Nombre para mostrar") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = nuevaContrasena,
                onValueChange = { nuevaContrasena = it },
                label = { Text("Nueva contraseña (opcional)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Button(
                onClick = {
                    val pwd = nuevaContrasena.takeIf { it.isNotBlank() }
                    vm.guardar(pwd) {
                        scope.launch { snack.showSnackbar("Perfil actualizado") }
                        nuevaContrasena = ""
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Guardar cambios") }
            OutlinedButton(
                onClick = { mostrarEliminar = true },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Eliminar perfil") }
        }
    }
}

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
                }
            )
        }
    ) { pad ->
        Column(
            modifier = Modifier
                .padding(pad)
                .padding(24.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            BotonModulo("Imagen y consigna", "Elige la imagen correcta") {
                nav.navigate(Rutas.ACT_IMAGEN)
            }
            BotonModulo("Audio", "Escucha y elige la respuesta") {
                nav.navigate(Rutas.ACT_AUDIO)
            }
            BotonModulo("Palabras incompletas", "Completa la palabra en inglés") {
                nav.navigate(Rutas.ACT_PALABRAS)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PantallaActividadImagen(factory: InglesViewModelFactory, nav: NavHostController) {
    val vm: ActividadImagenViewModel = viewModel(factory = factory)
    val feedback by vm.feedback.collectAsState()
    val snack = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val p = vm.preguntaActual

    LaunchedEffect(feedback) {
        feedback?.let {
            scope.launch { snack.showSnackbar(it) }
            vm.limpiarFeedback()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snack) },
        topBar = {
            TopAppBar(
                title = { Text("Actividad: imágenes") },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    TextButton(onClick = { vm.reiniciar() }) { Text("Reiniciar") }
                }
            )
        }
    ) { pad ->
        Column(
            modifier = Modifier
                .padding(pad)
                .padding(24.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                p.consigna,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                p.opciones.chunked(2).forEach { fila ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        fila.forEach { emoji ->
                            Button(
                                onClick = { vm.responder(emoji) },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(72.dp)
                            ) {
                                Text(emoji, style = MaterialTheme.typography.headlineMedium)
                            }
                        }
                        if (fila.size == 1) Spacer(Modifier.weight(1f))
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
    val listo by vm.ttsListo.collectAsState()
    val snack = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val p = vm.preguntaActual

    LaunchedEffect(feedback) {
        feedback?.let {
            scope.launch { snack.showSnackbar(it) }
            vm.limpiarFeedback()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snack) },
        topBar = {
            TopAppBar(
                title = { Text("Actividad: audio") },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    TextButton(onClick = { vm.reiniciar() }) { Text("Reiniciar") }
                }
            )
        }
    ) { pad ->
        Column(
            modifier = Modifier
                .padding(pad)
                .padding(24.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Pulsa «Escuchar» y elige el significado correcto en español.",
                style = MaterialTheme.typography.bodyLarge
            )
            Button(
                onClick = { vm.reproducir() },
                enabled = listo,
                modifier = Modifier.fillMaxWidth()
            ) { Text(if (listo) "Escuchar" else "Preparando audio…") }
            p.opciones.forEach { opcion ->
                OutlinedButton(
                    onClick = { vm.responder(opcion) },
                    modifier = Modifier.fillMaxWidth()
                ) { Text(opcion) }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PantallaActividadPalabras(factory: InglesViewModelFactory, nav: NavHostController) {
    val vm: ActividadPalabrasViewModel = viewModel(factory = factory)
    val feedback by vm.feedback.collectAsState()
    val snack = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val p = vm.preguntaActual

    LaunchedEffect(feedback) {
        feedback?.let {
            scope.launch { snack.showSnackbar(it) }
            vm.limpiarFeedback()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snack) },
        topBar = {
            TopAppBar(
                title = { Text("Palabras incompletas") },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    TextButton(onClick = { vm.reiniciar() }) { Text("Reiniciar") }
                }
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
            Text(
                p.incompleta,
                style = MaterialTheme.typography.displaySmall,
                textAlign = TextAlign.Center
            )
            Text("Completa la palabra en inglés", style = MaterialTheme.typography.bodyMedium)
            p.opciones.forEach { w ->
                Button(
                    onClick = { vm.responder(w) },
                    modifier = Modifier.fillMaxWidth()
                ) { Text(w) }
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
                title = { Text("Reportes") },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { pad ->
        Column(
            modifier = Modifier
                .padding(pad)
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (u == null) {
                Text("No hay datos de usuario.")
            } else {
                TarjetaDato("Puntaje total", "${u!!.puntajeTotal} pts")
                TarjetaDato("Nivel alcanzado", "Nivel ${u!!.nivel}")
                TarjetaDato(
                    "Imágenes",
                    "${u!!.aciertosImagen} aciertos / ${u!!.partidasImagen} intentos"
                )
                TarjetaDato(
                    "Audio",
                    "${u!!.aciertosAudio} aciertos / ${u!!.partidasAudio} intentos"
                )
                TarjetaDato(
                    "Palabras",
                    "${u!!.aciertosPalabras} aciertos / ${u!!.partidasPalabras} intentos"
                )
            }
        }
    }
}

@Composable
private fun TarjetaDato(etiqueta: String, valor: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(etiqueta, style = MaterialTheme.typography.labelLarge)
            Text(valor, style = MaterialTheme.typography.titleLarge)
        }
    }
}

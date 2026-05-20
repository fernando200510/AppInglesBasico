package org.fernandoblanco.inglesbasico

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.fernandoblanco.inglesbasico.ui.InglesAppRoot
import org.fernandoblanco.inglesbasico.ui.theme.InglesBasicoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InglesBasicoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    InglesAppRoot()
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        val app = application as? InglesApp ?: return
        lifecycleScope.launch {
            app.repositorioNino.flushTiempoSesionActiva()
        }
    }
}

package org.fernandoblanco.inglesbasico

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import org.fernandoblanco.inglesbasico.ui.InglesAppRoot
import org.fernandoblanco.inglesbasico.ui.theme.InglesBasicoTheme
import org.fernandoblanco.inglesbasico.ui.theme.PlayCream

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InglesBasicoTheme(dynamicColor = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = PlayCream
                ) {
                    InglesAppRoot()
                }
            }
        }
    }
}
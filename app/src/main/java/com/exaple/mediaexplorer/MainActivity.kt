package com.exaple.mediaexplorer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.exaple.mediaexplorer.ui.theme.MediaExplorerTheme
import com.exaple.mediaexplorer.ui.viewmodels.MediaExplorerViewModel
import com.exaple.mediaexplorer.ui.views.MediaExplorer

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        MediaExplorerViewModel.init( applicationContext )
        setContent {
            MediaExplorerTheme {
                Scaffold (
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                ){ innerPaddings ->
                    MediaExplorer( innerPaddings = innerPaddings)
                }
            }
        }
    }
}
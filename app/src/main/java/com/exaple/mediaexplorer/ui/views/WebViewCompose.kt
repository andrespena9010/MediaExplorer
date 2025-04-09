package com.exaple.mediaexplorer.ui.views

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.exaple.mediaexplorer.ui.viewmodels.WebViewModel
import com.exaple.mediaexplorer.ui.viewmodels.WebViewModelClass

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewCompose(
    viewModel: WebViewModelClass = WebViewModel
) {

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    ) { innerPaddings ->
        Box (
            modifier = Modifier
                .padding( innerPaddings )
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            AndroidView(
                factory = { ctx ->
                    viewModel.go( ctx )
                }
            )
        }
    }
}
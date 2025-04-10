package com.exaple.mediaexplorer.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.exaple.mediaexplorer.ui.viewmodels.MediaExporerViewModel
import com.exaple.mediaexplorer.ui.viewmodels.MediaExporerViewModelClass

@Composable
fun MediaExplorer(
    viewModel: MediaExporerViewModelClass = MediaExporerViewModel,
    innerPaddings: PaddingValues
){

    val selectedItem by viewModel.selectedItem.collectAsStateWithLifecycle() // mediaitem
    val ready by viewModel.ready.collectAsStateWithLifecycle()
    val loadingMessages by viewModel.loadingMessages.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .padding( innerPaddings )
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ){

        if ( !ready ){
            Column (
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                CircularProgressIndicator(
                    modifier = Modifier.padding(5.dp)
                )
                LazyColumn (
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ){
                    items( loadingMessages ) { message ->
                        Text( message )
                    }
                }
            }
        }

    }

}
package com.exaple.mediaexplorer.ui.views

import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.util.UnstableApi
import com.exaple.mediaexplorer.ui.custom.SelectedContainer
import com.exaple.mediaexplorer.ui.models.Type
import com.exaple.mediaexplorer.ui.viewmodels.MediaExplorerViewModel
import com.exaple.mediaexplorer.ui.viewmodels.MediaExplorerViewModelClass


@OptIn(UnstableApi::class)
@Composable
fun MediaExplorer(
    viewModel: MediaExplorerViewModelClass = MediaExplorerViewModel,
    innerPaddings: PaddingValues
){

    val items by viewModel.items.collectAsStateWithLifecycle()
    val selectedItem by viewModel.selectedItem.collectAsStateWithLifecycle()
    val ready by viewModel.ready.collectAsStateWithLifecycle()

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
                Text("Loading media...")
            }
        } else {

            if ( selectedItem.type == Type.None){
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .zIndex(100f),
                    contentAlignment = Alignment.Center
                ){
                    Button(
                        onClick = {
                            viewModel.start()
                        }
                    ) {
                        Text("Inicio")
                    }
                }
            }

            items.items.forEachIndexed { index, item ->
                SelectedContainer(
                    item = item,
                    zIndex = index + 1
                )
            }

        }

    }

}
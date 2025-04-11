package com.exaple.mediaexplorer.ui.custom

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.exaple.mediaexplorer.ui.viewmodels.MediaExplorerViewModel
import com.exaple.mediaexplorer.ui.viewmodels.MediaExplorerViewModelClass

@Composable
fun Options(
    viewModel: MediaExplorerViewModelClass = MediaExplorerViewModel
){

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ){



    }

}

@Preview(
    device = "spec:width=960dp,height=540dp,dpi=320"
)
@Composable
private fun Preview(){
    Options()
}
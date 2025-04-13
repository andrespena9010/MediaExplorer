package com.exaple.mediaexplorer.ui.views

import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.compose.PlayerSurface
import androidx.media3.ui.compose.SURFACE_TYPE_TEXTURE_VIEW
import coil3.compose.AsyncImage
import com.exaple.mediaexplorer.ui.custom.MediaContainer
import com.exaple.mediaexplorer.ui.custom.Options
import com.exaple.mediaexplorer.ui.custom.WeatherView
import com.exaple.mediaexplorer.ui.models.AudioExplorerItem
import com.exaple.mediaexplorer.ui.models.ImageExplorerItem
import com.exaple.mediaexplorer.ui.models.PdfExplorerItem
import com.exaple.mediaexplorer.ui.models.Type
import com.exaple.mediaexplorer.ui.models.VideoExplorerItem
import com.exaple.mediaexplorer.ui.models.WeatherExplorerItem
import com.exaple.mediaexplorer.ui.models.WebExplorerItem
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
                Options()
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit){
                        detectTapGestures(
                            onPress = {
                                viewModel.restart()
                            }
                        )
                    }
                    .zIndex(101f)
            )

            BoxWithConstraints (
                modifier = Modifier
                    .fillMaxSize()
            ){
                val transTime = 2000
                items.forEachIndexed { index, item ->

                    when ( item.type ){

                        Type.Image -> {

                            MediaContainer(
                                item = item,
                                index = index,
                                transTime = transTime,
                                modifier = Modifier
                            ) { mod ->
                                Image(
                                    bitmap = ( item as ImageExplorerItem ).bitmap!!.asImageBitmap(),
                                    contentDescription = item.name,
                                    modifier = mod
                                )
                            }

                        }

                        Type.AudioMix -> {

                            val audioItem = ( item as AudioExplorerItem )

                            MediaContainer(
                                item = item,
                                index = index,
                                transTime = transTime,
                                modifier = Modifier
                            ) { mod ->
                                when ( audioItem.contentType.type ){

                                    Type.Image -> {
                                        Image(
                                            bitmap = audioItem.bitmap!!.asImageBitmap(),
                                            contentDescription = item.name,
                                            modifier = mod
                                                .zIndex(0f)
                                        )
                                    }

                                    Type.Gif -> {
                                        AsyncImage(
                                            model = audioItem.byteArray,
                                            contentDescription = audioItem.name,
                                            modifier = mod
                                                .zIndex(0f)
                                        )
                                    }

                                }
                            }
                        }

                        Type.Video -> {
                            val videoItem = ( item as VideoExplorerItem )
                            val player = remember { videoItem.viewModel.getPlayer() }
                            val size = videoItem.viewModel.getSize(0)
                            val modifier = Modifier
                                .width(size.width.toFloat().dp)
                                .height(size.height.toFloat().dp)

                            MediaContainer(
                                item = item,
                                index = index,
                                transTime = transTime,
                                modifier = modifier
                            ) { mod ->
                                PlayerSurface(
                                    player = player,
                                    surfaceType = SURFACE_TYPE_TEXTURE_VIEW,
                                    modifier = mod
                                        .zIndex(0f)
                                )
                            }
                        }

                        Type.Pdf -> {
                            val pdfItem = ( item as PdfExplorerItem )
                            val pages by pdfItem.viewModel.pdfPages.collectAsStateWithLifecycle()
                            MediaContainer(
                                item = item,
                                index = index,
                                transTime = transTime,
                                modifier = Modifier
                            ) { mod ->
                                Image(
                                    bitmap = pages[0].bitmap!!.asImageBitmap(),
                                    contentDescription = item.name,
                                    modifier = mod
                                        .background(Color.White)
                                        .zIndex(0f)
                                )
                            }
                        }

                        Type.Web -> {
                            val webView = ( item as WebExplorerItem ).viewModel.getWebView()
                            MediaContainer(
                                item = item,
                                index = index,
                                transTime = transTime,
                                modifier = Modifier
                            ) { mod ->
                                AndroidView(
                                    factory = { ctx ->
                                        webView
                                    },
                                    modifier = mod
                                )
                            }
                        }

                        Type.Weather -> {
                            val weatherItem = ( item as WeatherExplorerItem )
                            MediaContainer(
                                item = item,
                                index = index,
                                transTime = transTime,
                                modifier = Modifier
                            ) { mod ->
                                WeatherView(
                                    viewModel = weatherItem.viewModel,
                                    modifier = mod
                                        .zIndex(0f)
                                )
                            }
                        }

                    }

                }

            }

        }

    }

}
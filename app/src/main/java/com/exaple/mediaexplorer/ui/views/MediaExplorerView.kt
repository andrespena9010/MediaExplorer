package com.exaple.mediaexplorer.ui.views

import androidx.activity.compose.BackHandler
import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.compose.PlayerSurface
import androidx.media3.ui.compose.SURFACE_TYPE_TEXTURE_VIEW
import coil3.compose.AsyncImage
import com.exaple.mediaexplorer.ui.custom.Controls
import com.exaple.mediaexplorer.ui.custom.MediaContainer
import com.exaple.mediaexplorer.ui.custom.Options
import com.exaple.mediaexplorer.ui.custom.WeatherView
import com.exaple.mediaexplorer.ui.models.AudioItem
import com.exaple.mediaexplorer.ui.models.ImageItem
import com.exaple.mediaexplorer.ui.models.PdfItem
import com.exaple.mediaexplorer.ui.models.Type
import com.exaple.mediaexplorer.ui.models.VideoItem
import com.exaple.mediaexplorer.ui.models.WeatherItem
import com.exaple.mediaexplorer.ui.models.WebItem
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
    val transTime by viewModel.transitionTime.collectAsStateWithLifecycle()
    val inTransition by viewModel.inTransition.collectAsStateWithLifecycle()
    val activeScreen by viewModel.activeScreen.collectAsStateWithLifecycle()
    val context = LocalContext.current

    BackHandler {
        viewModel.restart( context )
    }

    Box(
        modifier = Modifier
            .padding( innerPaddings )
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ){

        if ( selectedItem == -1 ){
            Options()
        }

        if ( !activeScreen ){
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit){
                        detectTapGestures(
                            onPress = {}
                        )
                    }
                    .zIndex(1f)
            )
        }

        BoxWithConstraints (
            modifier = Modifier
                .fillMaxSize()
        ){

            items.forEachIndexed { ind, item ->

                val index = 0 - ind

                if ( item.load ){

                    when ( item.type ){

                        Type.Image -> {

                            MediaContainer(
                                item = item,
                                index = index,
                                transTime = transTime,
                                modifier = Modifier
                            ) { mod ->
                                Image(
                                    bitmap = ( item as ImageItem ).getSaveBitmap().asImageBitmap(),
                                    contentDescription = item.uuid,
                                    modifier = mod
                                )
                            }

                        }

                        Type.AudioMix -> {

                            val audioItem = ( item as AudioItem )

                            MediaContainer(
                                item = item,
                                index = index,
                                transTime = transTime,
                                modifier = Modifier
                            ) { mod ->

                                Controls(
                                    viewModel = audioItem.viewModel,
                                    modifier = mod
                                        .zIndex(1f)
                                )

                                when ( audioItem.contentType.type ){

                                    Type.Image -> {
                                        Image(
                                            bitmap = audioItem.getSaveBitmap().asImageBitmap(),
                                            contentDescription = item.uuid,
                                            modifier = mod
                                                .zIndex(0f)
                                        )
                                    }

                                    Type.Gif -> {
                                        AsyncImage(
                                            model = audioItem.byteArray,
                                            contentDescription = audioItem.uuid,
                                            modifier = mod
                                                .zIndex(0f)
                                        )
                                    }

                                }
                            }
                        }

                        Type.Video -> {

                            val videoItem = ( item as VideoItem )
                            val den = LocalDensity.current
                            val size = videoItem.viewModel.getSize()
                            val modifier = Modifier
                                .width( with ( den ){ size.width.toDp() } )
                                .height( with ( den ){ size.height.toDp() } )

                            MediaContainer(
                                item = item,
                                index = index,
                                transTime = transTime,
                                modifier = modifier
                            ) { mod ->
                                if ( inTransition ){
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(Color.Black)
                                    )
                                } else {
                                    PlayerSurface(
                                        player = videoItem.viewModel.getPlayer(),
                                        surfaceType = SURFACE_TYPE_TEXTURE_VIEW,
                                        modifier = mod
                                            .zIndex(0f)
                                    )
                                }
                            }
                        }

                        Type.Pdf -> {

                            val pdfItem = ( item as PdfItem )

                            MediaContainer(
                                item = item,
                                index = index,
                                transTime = transTime,
                                modifier = Modifier
                            ) { mod ->
                                Image(
                                    bitmap = pdfItem.viewModel.getPage(0).asImageBitmap(),
                                    contentDescription = item.uuid,
                                    modifier = mod
                                        .background(Color.White)
                                        .zIndex(0f)
                                )
                            }
                        }

                        Type.Web -> {

                            val webView = ( item as WebItem ).viewModel.getWebView()

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

                            val weatherItem = ( item as WeatherItem )

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
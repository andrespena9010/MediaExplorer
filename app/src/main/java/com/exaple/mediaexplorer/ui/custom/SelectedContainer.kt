package com.exaple.mediaexplorer.ui.custom

import android.annotation.SuppressLint
import androidx.annotation.OptIn
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.compose.PlayerSurface
import androidx.media3.ui.compose.SURFACE_TYPE_TEXTURE_VIEW
import coil3.compose.AsyncImage
import com.exaple.mediaexplorer.ui.models.AudioExplorerItem
import com.exaple.mediaexplorer.ui.models.ImageExplorerItem
import com.exaple.mediaexplorer.ui.models.MediaExplorerItem
import com.exaple.mediaexplorer.ui.models.PdfExplorerItem
import com.exaple.mediaexplorer.ui.models.Type
import com.exaple.mediaexplorer.ui.models.VideoExplorerItem
import com.exaple.mediaexplorer.ui.models.WeatherExplorerItem
import com.exaple.mediaexplorer.ui.models.WebExplorerItem
import com.exaple.mediaexplorer.ui.viewmodels.MediaExplorerViewModel
import com.exaple.mediaexplorer.ui.viewmodels.MediaExplorerViewModelClass

@SuppressLint("UnusedBoxWithConstraintsScope")
@OptIn(UnstableApi::class)
@Composable
fun SelectedContainer(
    item: MediaExplorerItem,
    zIndex: Int,
    viewModel: MediaExplorerViewModelClass = MediaExplorerViewModel
){

    val selectedItem by viewModel.selectedItem.collectAsStateWithLifecycle()

    when ( item.type ){

        Type.Image -> {
            BoxWithConstraints( // TODO: asignarlo al contenido!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                modifier = Modifier.fillMaxSize()
            ){
                val height by animateDpAsState(
                    targetValue = if ( item == selectedItem ) maxHeight else 0.dp,
                    label = "AnimatedHeight"
                )
                val width by animateDpAsState(
                    targetValue = if ( item == selectedItem ) maxWidth else 0.dp,
                    label = "AnimatedWidth"
                )

                DefaultContainer (
                    modifier = Modifier
                        .width(width)
                        .height(height),
                    onPress = { viewModel.restart( item ) },
                    zIndex = zIndex
                ){
                    Image(
                        bitmap = ( item as ImageExplorerItem ).bitmap!!.asImageBitmap(),
                        contentDescription = item.name,
                        modifier = Modifier
                            .zIndex(0f)
                    )
                }

            }
        }

        /*Type.AudioMix -> {
            val audioItem = ( item as AudioExplorerItem )
            DefaultContainer (
                onPress = { viewModel.restart( item ) },
                zIndex = zIndex
            ){
                when ( audioItem.contentType.type ){

                    Type.Image -> {
                        Image(
                            bitmap = audioItem.bitmap!!.asImageBitmap(),
                            contentDescription = item.name,
                            modifier = Modifier
                                .zIndex(0f)
                        )
                    }

                    Type.Gif -> {
                        AsyncImage(
                            model = audioItem.byteArray,
                            contentDescription = audioItem.name,
                            modifier = Modifier
                                .zIndex(0f)
                        )
                    }

                }
            }
        }

        Type.Video -> {
            val videoItem = ( item as VideoExplorerItem )
            DefaultContainer (
                onPress = { viewModel.restart( item ) },
                zIndex = zIndex
            ){
                val player = remember { videoItem.viewModel.getPlayer() }
                val size = videoItem.viewModel.getSize(0)

                PlayerSurface(
                    player = player,
                    surfaceType = SURFACE_TYPE_TEXTURE_VIEW,
                    modifier = Modifier
                        .width(size.width.toFloat().dp)
                        .height(size.height.toFloat().dp)
                        .zIndex(0f)
                )
            }
        }

        Type.Pdf -> {
            val pdfItem = ( item as PdfExplorerItem )
            val pages by pdfItem.viewModel.pdfPages.collectAsStateWithLifecycle()
            DefaultContainer (
                onPress = { viewModel.restart( item ) },
                zIndex = zIndex
            ){
                Image(
                    bitmap = pages[0].bitmap!!.asImageBitmap(),
                    contentDescription = item.name,
                    modifier = Modifier
                        .background(Color.White)
                        .zIndex(0f)
                )
            }
        }

        Type.Web -> {
            val webView = ( item as WebExplorerItem ).viewModel.getWebView()
            DefaultContainer (
                onPress = { viewModel.restart( item ) },
                zIndex = zIndex
            ){
                AndroidView(
                    factory = { ctx ->
                        webView
                    }
                )
            }
        }

        Type.Weather -> {
            val weatherItem = ( item as WeatherExplorerItem )
            DefaultContainer (
                onPress = { viewModel.restart( item ) },
                zIndex = zIndex
            ){
                WeatherView(
                    viewModel = weatherItem.viewModel,
                    modifier = Modifier
                        .zIndex(0f)
                )
            }
        }*/

    }
}
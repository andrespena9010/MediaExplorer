package com.exaple.mediaexplorer.ui.custom

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.exaple.mediaexplorer.ui.models.MediaExplorerItem
import com.exaple.mediaexplorer.ui.models.Transition
import com.exaple.mediaexplorer.ui.shapes.DynamicCircleShape
import com.exaple.mediaexplorer.ui.shapes.DynamicCustomShape
import com.exaple.mediaexplorer.ui.shapes.DynamicDiagonalShape
import com.exaple.mediaexplorer.ui.shapes.DynamicRectangleShape
import com.exaple.mediaexplorer.ui.viewmodels.MediaExplorerViewModel
import com.exaple.mediaexplorer.ui.viewmodels.MediaExplorerViewModelClass

@Composable
fun BoxWithConstraintsScope.MediaContainer(
    item: MediaExplorerItem,
    index: Int,
    transTime: Int,
    modifier: Modifier,
    viewModel: MediaExplorerViewModelClass = MediaExplorerViewModel,
    content: @Composable BoxScope.( modifier: Modifier ) -> Unit
){

    val transition by viewModel.transition.collectAsStateWithLifecycle()
    var modifierSize by remember { mutableStateOf(IntSize.Zero) }

    val alpha by animateFloatAsState(
        targetValue = if ( item.active ) 1f else 0f,
        animationSpec = if (item.active) snap() else tween(transTime),
    )

    val offsetX by animateDpAsState(
        targetValue = if ( item.active ) 0.dp else -maxWidth,
        animationSpec = if (item.active) snap() else tween(transTime),
    )

    val rectangleShape = remember( alpha ) {
        DynamicRectangleShape( alpha )
    }

    val circleShape = remember( alpha ) {
        DynamicCircleShape( alpha )
    }
    val diagonalShape = remember( alpha ) {
        DynamicDiagonalShape( alpha )
    }

    val customShape = remember( alpha ) {
        DynamicCustomShape( alpha )
    }

    Box (
        modifier = Modifier
            .zIndex( index.toFloat() )
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ){

        when ( transition ){

            Transition.SlideToLeft -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background( Color.Black.copy( alpha = alpha ) )
                )

                content(
                    modifier
                        .offset {
                            IntOffset(offsetX.roundToPx(), 0)
                        }
                        .onGloballyPositioned { coordinates ->
                            modifierSize = coordinates.size
                        }
                )
            }

            Transition.SizeAtCenter -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background( Color.Black.copy( alpha = alpha ) )
                )

                content(
                    modifier
                        .clip( rectangleShape )
                )
            }

            Transition.Blurred -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background( Color.Black.copy( alpha = alpha ) )
                )

                content(
                    modifier
                        .alpha( alpha )
                )
            }

            Transition.LineExpanded -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip( diagonalShape ),
                    contentAlignment = Alignment.Center
                ){
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background( Color.Black.copy( alpha = alpha ) )
                    )

                    content(
                        modifier
                    )
                }
            }

            Transition.CircleAtCenter -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background( Color.Black.copy( alpha = alpha ) )
                )

                content(
                    modifier
                        .clip( circleShape )
                )
            }

            Transition.Custom -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip( customShape ),
                    contentAlignment = Alignment.Center
                ){
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background( Color.Black.copy( alpha = alpha ) )
                    )

                    content(
                        modifier
                    )
                }
            }

        }

    }
}

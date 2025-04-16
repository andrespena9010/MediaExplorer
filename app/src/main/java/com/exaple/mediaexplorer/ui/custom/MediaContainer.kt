package com.exaple.mediaexplorer.ui.custom

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.exaple.mediaexplorer.ui.models.MediaExplorerItem
import com.exaple.mediaexplorer.ui.models.Transition
import com.exaple.mediaexplorer.ui.shapes.DynamicCircleShape
import com.exaple.mediaexplorer.ui.shapes.DynamicCustomShape
import com.exaple.mediaexplorer.ui.shapes.DynamicDiagonalShape
import com.exaple.mediaexplorer.ui.viewmodels.MediaExplorerViewModel
import com.exaple.mediaexplorer.ui.viewmodels.MediaExplorerViewModelClass

@OptIn(ExperimentalAnimationApi::class)
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

    val position by animateFloatAsState(
        targetValue = if ( item.active ) 1f else 0f,
        animationSpec = if (item.active) snap() else tween(transTime),
    )

    val circleShape = remember( position ) {
        DynamicCircleShape( position )
    }
    val diagonalShape = remember( position ) {
        DynamicDiagonalShape( position )
    }

    val customShape = remember( position ) {
        DynamicCustomShape( position )
    }

    Box (
        modifier = Modifier
            .zIndex( index.toFloat() )
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ){

        when ( transition ){

            Transition.SlideOutLeft -> {
                AnimatedVisibility(
                    visible = item.active,
                    enter = EnterTransition.None,
                    exit = slideOutHorizontally(
                        targetOffsetX = { fullWidth -> -fullWidth },
                        animationSpec = tween(durationMillis = transTime)
                    ) + fadeOut(tween(transTime))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ){
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background( Color.Black )
                        )

                        content(
                            modifier
                        )
                    }
                }
            }

            Transition.FadeScaleOut -> {
                AnimatedVisibility(
                    visible = item.active,
                    enter = EnterTransition.None,
                    exit = fadeOut(animationSpec = tween(transTime, easing = FastOutSlowInEasing)) +
                            scaleOut(animationSpec = tween(transTime)),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ){
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background( Color.Black )
                        )

                        content(
                            modifier
                        )
                    }
                }
            }

            Transition.FadeOut -> {
                AnimatedVisibility(
                    visible = item.active,
                    enter = EnterTransition.None,
                    exit = fadeOut(animationSpec = tween(transTime, easing = FastOutSlowInEasing)),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ){
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background( Color.Black )
                        )

                        content(
                            modifier
                        )
                    }
                }
            }

            Transition.FadeOutLine -> {
                AnimatedVisibility(
                    visible = item.active,
                    enter = EnterTransition.None,
                    exit = fadeOut(animationSpec = tween(transTime, easing = FastOutSlowInEasing)),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip( diagonalShape ),
                        contentAlignment = Alignment.Center
                    ){
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background( Color.Black )
                        )

                        content(
                            modifier
                        )
                    }
                }
            }

            Transition.FadeOutCircle -> {
                AnimatedVisibility(
                    visible = item.active,
                    enter = EnterTransition.None,
                    exit = fadeOut(animationSpec = tween(transTime, easing = FastOutSlowInEasing)),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip( circleShape ),
                        contentAlignment = Alignment.Center
                    ){
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background( Color.Black )
                        )

                        content(
                            modifier
                        )
                    }
                }
            }

            Transition.FadeOutCustom -> {
                AnimatedVisibility(
                    visible = item.active,
                    enter = EnterTransition.None,
                    exit = fadeOut(animationSpec = tween(transTime, easing = FastOutSlowInEasing)),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip( customShape ),
                        contentAlignment = Alignment.Center
                    ){
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background( Color.Black )
                        )

                        content(
                            modifier
                        )
                    }
                }
            }

        }

    }
}

package com.exaple.mediaexplorer.ui.custom

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.zIndex

@Composable
fun DefaultContainer(
    modifier: Modifier = Modifier,
    onPress: () -> Unit,
    zIndex: Int,
    content: @Composable () -> Unit
){
    Box(
       modifier = modifier
           .fillMaxSize()
           .background(Color.Black)
           .zIndex(zIndex.toFloat())
           .pointerInput(Unit){
               detectTapGestures(
                   onPress = {
                       onPress()
                   }
               )
           },
        contentAlignment = Alignment.Center
    ){
        content()
    }
}
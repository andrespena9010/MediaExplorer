package com.exaple.mediaexplorer.ui.custom

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.exaple.mediaexplorer.ui.models.Effect
import com.exaple.mediaexplorer.ui.models.Transition
import com.exaple.mediaexplorer.ui.viewmodels.MediaExplorerViewModel
import com.exaple.mediaexplorer.ui.viewmodels.MediaExplorerViewModelClass

@Composable
fun Options(){

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .zIndex(102f),
        contentAlignment = Alignment.Center
    ){
        Column (
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ){
                OptionItem (
                    effect = Transition.SlideOutLeft
                )
                OptionItem (
                    effect = Transition.FadeScaleOut
                )
                OptionItem (
                    effect = Transition.FadeOut
                )
            }

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            )

            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ){
                OptionItem (
                    effect = Transition.FadeLine
                )
                OptionItem (
                    effect = Transition.FadeCircle
                )
                OptionItem (
                    effect = Transition.FadeCustom
                )
            }
        }
    }

}

@Composable
fun OptionItem(
    viewModel: MediaExplorerViewModelClass = MediaExplorerViewModel,
    effect: Effect
){
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .shadow(20.dp, RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF999999))
            .size(150.dp)
            .clickable(
                onClick = {
                    viewModel.setTransition( effect )
                    viewModel.start( context )
                }
            ),
        contentAlignment = Alignment.Center

    ){
        Text(
            text = effect.label,
            color = Color.Black
        )
    }
}

@Preview(
    device = "spec:width=960dp,height=540dp,dpi=640"
)
@Composable
private fun Preview(){
    Options()
}
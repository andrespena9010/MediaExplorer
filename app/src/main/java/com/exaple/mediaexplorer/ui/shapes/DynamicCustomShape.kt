package com.exaple.mediaexplorer.ui.shapes

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.PageSize.Fill
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

class DynamicCustomShape( private val progress: Float ) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic( createPath( size, progress) )
    }



}

fun createPath(size: Size, progress: Float ): Path {

    val path = Path()

    val width = size.width
    val height = size.height

    val cant = 5
    val fraction = height / cant
    val midFraction = fraction / 2
    var progressFraction = 0f
    var progressWidthFraction = 1f

    if ( progress > 0.5f ){
        progressFraction = ( midFraction / 2 ) * ( 1 - progress )
        progressWidthFraction = width
    } else {
        progressFraction = ( midFraction / 2 ) * 0.5f
        progressWidthFraction = width * ( 2 * progress )
    }

    for ( i in 0 .. cant - 1 ){

        val xf0 = 0f
        val xf1 = progressWidthFraction
        val xf2 = progressWidthFraction
        val xf3 = 0f
        val xf4 = 0f

        val yf0 = i * fraction
        val yf1 = i * fraction
        val yf2 = ( ( i + 1 ) * fraction ) - midFraction
        val yf3 = ( ( i + 1 ) * fraction ) - midFraction
        val yf4 = i * fraction

        path.moveTo( xf0 , yf0 + progressFraction )
        path.lineTo( xf1 , yf1 + progressFraction )
        path.lineTo( xf2 , yf2 - progressFraction)
        path.lineTo( xf3 , yf3 - progressFraction)
        path.moveTo( xf4 , yf4 + progressFraction )
        path.close()

        val xs0 = width - progressWidthFraction
        val xs1 = width
        val xs2 = width
        val xs3 = width - progressWidthFraction
        val xs4 = width - progressWidthFraction

        val ys0 = ( i * fraction ) + midFraction
        val ys1 = ( i * fraction ) + midFraction
        val ys2 = ( i + 1 ) * fraction
        val ys3 = ( i + 1 ) * fraction
        val ys4 = i * fraction

        path.moveTo( xs0 , ys0 + progressFraction)
        path.lineTo( xs1 , ys1 + progressFraction)
        path.lineTo( xs2 , ys2 - progressFraction)
        path.lineTo( xs3 , ys3 - progressFraction)
        path.moveTo( xs4 , ys4 + progressFraction)
        path.close()

    }

    return path
}

@Preview(
    device = "spec:width=960dp,height=540dp,dpi=640"
)
@Composable
private fun Preview(){
    Canvas(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val path = createPath(
            size = size,
            progress = 0.4f
        )

        drawPath(
            path = path,
            color = Color.Cyan
        )
    }
}
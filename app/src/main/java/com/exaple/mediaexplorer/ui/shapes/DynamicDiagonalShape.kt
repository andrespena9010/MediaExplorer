package com.exaple.mediaexplorer.ui.shapes

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

class DynamicDiagonalShape( private val progress: Float ) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic( createPath(size) )
    }

    fun createPath(size: Size): Path {
        val path = Path()

        val width = size.width
        val height = size.height

        val rightX = width * progress
        val leftX = width * (1 - progress)

        path.moveTo(0f, 0f)
        path.lineTo(0f, height)
        path.lineTo(rightX, height)
        path.lineTo(0f, 0f)
        path.close()

        path.moveTo(width, height)
        path.lineTo(width, 0f)
        path.lineTo(leftX, 0f)
        path.lineTo(width, height)
        path.close()

        return path
    }
}
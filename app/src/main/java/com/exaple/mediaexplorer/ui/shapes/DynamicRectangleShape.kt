package com.exaple.mediaexplorer.ui.shapes

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

class DynamicRectangleShape( private val progress: Float ) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic( createPath(size) )
    }

    fun createPath(size: Size): Path {
        val path = Path()

        val centerX = size.width / 2f
        val centerY = size.height / 2f

        val halfWidth = (size.width / 2f) * progress
        val halfHeight = (size.height / 2f) * progress

        val left = centerX - halfWidth
        val top = centerY - halfHeight
        val right = centerX + halfWidth
        val bottom = centerY + halfHeight

        path.moveTo(left, top)
        path.lineTo(right, top)
        path.lineTo(right, bottom)
        path.lineTo(left, bottom)
        path.close()

        return path
    }
}
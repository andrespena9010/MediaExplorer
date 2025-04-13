package com.exaple.mediaexplorer.ui.shapes

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import kotlin.math.hypot

class DynamicCircleShape( private val progress: Float ) : Shape {

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

        val maxRadius = hypot(size.width, size.height) / 2f
        val currentRadius = maxRadius * progress

        val left = centerX - currentRadius
        val top = centerY - currentRadius
        val right = centerX + currentRadius
        val bottom = centerY + currentRadius

        path.addOval(
            Rect(
                left = left,
                top = top,
                right = right,
                bottom = bottom
            )
        )

        return path
    }
}
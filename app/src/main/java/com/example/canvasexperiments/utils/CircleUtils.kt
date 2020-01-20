package com.example.canvasexperiments.utils

import kotlin.math.cos
import kotlin.math.sin

fun getXCoOrdinateOfCircle(originX: Float, radius: Float, angle: Float): Float =
    originX + (radius * cos(Math.toRadians(angle.toDouble())).toFloat())

fun getYCoOrdinateOfCircle(originY: Float, radius: Float, angle: Float): Float =
    originY + (radius * sin(Math.toRadians(angle.toDouble())).toFloat())
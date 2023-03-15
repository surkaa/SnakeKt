package com.surkaa.game

import com.surkaa.ui.Draw
import java.awt.Color
import java.awt.Graphics

sealed class Food(
    val point: Point = Point.random(),
    val value: Int = 1,
    val color: Color = Color.YELLOW
) : Draw {

    fun isNear(target: Point) = point.isNear(target, 2.0)

    override fun onDraw(g: Graphics) = point.onDraw(g, 0.5, color)

    companion object {
        fun random() = listOf(Pitaya(), Banana(), Grape()).random()
    }
}

class Pitaya(
    point: Point = Point.random()
) : Food(point = point, value = 3, color = Color.RED)

class Banana(
    point: Point = Point.random()
) : Food(point = point, value = 2, color = Color.YELLOW)

class Grape(
    point: Point = Point.random()
) : Food(point = point, value = 1, color = Color.GREEN)
package com.surkaa.snake

import com.surkaa.game.Manager
import com.surkaa.game.Point
import java.awt.Color

class ToFoodSnake(
    head: Point,
    angle: Double = 0.0,
    tail: MutableList<Point> = mutableListOf(),
    headColor: Color = Color.BLACK,
    tailColor: Color = Color.GRAY
) : DontHitWallSnake(head, angle, tail, headColor, tailColor) {

    override fun turn() = super.turn() ?: head.getAngle(getFood.point)

    private val getFood get() = Manager.getInstance().foods.minBy {
        head.getDistance(it.point)
    }

}
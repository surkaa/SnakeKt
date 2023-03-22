package com.surkaa.entities.snake

import com.surkaa.Manager
import com.surkaa.entities.Point
import java.awt.Color

class ToFoodSnake(
    head: Point,
    angle: Double = 0.0,
    body: MutableList<Point> = mutableListOf(),
    headColor: Color = Color(0X5868A1),
    bodyColor: Color = Color(0X737FB4)
) : DontHitWallSnake(head, angle, body, headColor, bodyColor) {

    override fun turn() = super.turn() ?: head.getAngle(getFood.point)

    private val getFood get() = Manager.getInstance().foods.minBy {
        head.getDistance(it.point)
    }

}
package com.surkaa.snake

import com.surkaa.game.Point
import com.surkaa.game.Snake
import java.awt.Color
import kotlin.random.Random

open class DontHitWallSnake(
    head: Point,
    angle: Double = 0.0,
    tail: MutableList<Point> = mutableListOf(),
    headColor: Color = Color(0X582A16),
    tailColor: Color = Color(0XB98458)
) : Snake(head, angle, tail, headColor, tailColor) {

    override fun turn(): Double? {
        if (nextTarget.isBroken()) {
            return random()
        }
        return null
    }

    // 随便找一个方向直到这个方向不会撞到墙
    private fun random(): Double {
        val nextAngle = Random.nextDouble(0.0, 360.0)
        if (head.getTarget(nextAngle).isBroken())
            return random()
        return nextAngle
    }
}
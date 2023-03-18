package com.surkaa.snake

import com.surkaa.game.Point
import com.surkaa.game.Snake
import java.awt.Color
import kotlin.random.Random

open class SmartSnake(
    head: Point,
    angle: Double = 0.0,
    tail: MutableList<Point> = mutableListOf(),
    headColor: Color = Color.BLACK,
    tailColor: Color = Color.GRAY
) : Snake(head, angle, tail, headColor, tailColor) {

    override fun turn(): Double? {
        if (nextTarget.isBroken()) {
            return random()
        }
        return null
    }

    private fun random(): Double {
        val nextAngle = Random.nextDouble(0.0, 360.0)
        if (head.getTarget(nextAngle).isBroken())
            return random()
        return nextAngle
    }
}
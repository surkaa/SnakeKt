package com.surkaa.entities.food

import com.surkaa.game.Point
import java.awt.Color

// 香蕉
class Banana(
    point: Point = Point.random()
) : Food(point = point, value = 2, color = Color.YELLOW) {

    override fun toString(): String {
        return "Banana$point"
    }
}

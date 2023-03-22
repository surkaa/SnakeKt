package com.surkaa.entities.food

import com.surkaa.entities.Point
import java.awt.Color

// 葡萄
class Grape(
    point: Point = Point.random()
) : Food(point = point, value = 1, color = Color.GREEN) {

    override fun toString(): String {
        return "Grape$point"
    }
}
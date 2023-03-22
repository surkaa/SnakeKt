package com.surkaa.entities.food

import com.surkaa.entities.Point
import java.awt.Color

// 火龙果
class Pitaya(
    point: Point = Point.random()
) : Food(point = point, value = 3, color = Color.RED) {

    override fun toString(): String {
        return "Pitaya$point"
    }
}

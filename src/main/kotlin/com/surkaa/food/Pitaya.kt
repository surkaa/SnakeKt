package com.surkaa.food

import com.surkaa.game.Point
import java.awt.Color

// 火龙果
class Pitaya(
    point: Point = Point.random()
) : Food(point = point, value = 3, color = Color.RED) {

    override fun toString(): String {
        return "Pitaya$point"
    }
}

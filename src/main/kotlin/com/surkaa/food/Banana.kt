package com.surkaa.food

import com.surkaa.game.Point
import java.awt.Color

// 香蕉
class Banana(
    point: Point = Point.random()
) : Food(point = point, value = 2, color = Color.YELLOW)

package com.surkaa.food

import com.surkaa.game.Point
import java.awt.Color

// 葡萄
class Grape(
    point: Point = Point.random()
) : Food(point = point, value = 1, color = Color.GREEN)
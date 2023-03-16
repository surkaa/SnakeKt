package com.surkaa.game

import com.surkaa.ui.Draw
import java.awt.Color
import java.awt.Graphics

/**
 * @author kaa
 */
sealed class Food(
    val point: Point = Point.random(),
    val value: Int = 1,
    val color: Color = Color.YELLOW
) : Draw {

    /**
     * 设置 powerForDistance 2.0 是为了让容易吃到食物
     */
    fun isNear(target: Point) = point.isNear(
        point = target,
        powerForDistance = 2.0
    )

    /**
     * 绘画 powerForRadius 0.5 是为了绘画食物比蛇半径小一倍
     */
    override fun onDraw(g: Graphics) = point.onDraw(
        g = g,
        powerForRadius = 0.5,
        color = color
    )

    companion object {
        /**
         * 随机生成一个食物
         */
        fun random() = listOf(Pitaya(), Banana(), Grape()).random()
    }
}

// 火龙果
class Pitaya(
    point: Point = Point.random()
) : Food(point = point, value = 3, color = Color.RED)

// 香蕉
class Banana(
    point: Point = Point.random()
) : Food(point = point, value = 2, color = Color.YELLOW)

// 葡萄
class Grape(
    point: Point = Point.random()
) : Food(point = point, value = 1, color = Color.GREEN)
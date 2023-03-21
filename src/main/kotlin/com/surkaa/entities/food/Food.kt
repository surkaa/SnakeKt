package com.surkaa.entities.food

import com.surkaa.game.Point
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

    override fun toString(): String {
        return "Food$point"
    }

    companion object {
        /**
         * 随机生成一个食物
         */
        fun random() = listOf(Pitaya(), Banana(), Grape()).random()
    }
}
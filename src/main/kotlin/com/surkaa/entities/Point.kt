package com.surkaa.entities

import com.surkaa.ui.Draw
import java.awt.Color
import java.awt.Graphics
import kotlin.math.*
import kotlin.random.Random

/**
 * A point in the game.
 * 用于记录地图上的点
 *
 * @author Kaa
 */
data class Point(
    val x: Double, val y: Double
) : Draw {

    /**
     * @param distanceForGameMap the distance from the edge of the map
     * @return true if the point is out of the map
     */
    fun isBroken(distanceForGameMap: Double = 0.0) = (x < distanceForGameMap)
            || (x > GAME_MAP_X - distanceForGameMap)
            || (y < distanceForGameMap)
            || (y > GAME_MAP_Y - distanceForGameMap)

    /**
     * 通过方向获取目的地的点
     *
     * @param angle 目的地的方向
     * @return 目的地的点
     */
    fun getTarget(angle: Double): Point {
        val rad = Math.toRadians(angle % 360)
        val newX = x + cos(rad) * GAME_MOVE_DISTANCE
        val newY = y + sin(rad) * GAME_MOVE_DISTANCE
        return Point(newX, newY)
    }

    /**
     * 检测与该点连线的角度
     * @param target 检测点
     * @return 角度
     */
    fun getAngle(target: Point): Double {
        val dx = target.x - x
        val dy = target.y - y
        val rad = atan2(dy, dx)
        return Math.toDegrees(rad)
    }

    /**
     * 检测与某点是否足够靠近
     * @param point 检测点
     * @param powerForDistance 与MOVE_DISTANCE相乘的倍数，数值越大越容易返回true
     * @return 与point的距离是否小于power * MOVE_DISTANCE
     */
    fun isNear(point: Point, powerForDistance: Double = 1.0): Boolean {
        // 加上1e-3防止计算误差
        val distance = getDistance(point) + 1e-3
        return distance < GAME_MOVE_DISTANCE * powerForDistance
    }

    fun getDistance(point: Point): Double {
        val dx2 = (point.x - this.x).pow(2)
        val dy2 = (point.y - this.y).pow(2)
        return sqrt(dx2 + dy2)
    }

    override fun onDraw(g: Graphics) {
        onDraw(g, color = Color.BLACK)
    }

    /**
     * 绘制一个圆
     * @param g 画笔
     * @param color 颜色
     * @param powerForRadius 与正常DRAW_RADIUS的倍数 默认为1,0
     */
    fun onDraw(g: Graphics, powerForRadius: Double = 1.0, color: Color? = null) {
        g.color = color
        val radius = (DRAW_RADIUS * powerForRadius).toInt()
        g.fillOval(
            (x * MULTIPLE).toInt() - radius / 2,
            (y * MULTIPLE).toInt() - radius / 2,
            radius,
            radius
        )
    }

    companion object {
        // 地图的最大宽度
        const val GAME_MAP_X = 64

        // 地图的最大高度
        const val GAME_MAP_Y = 40

        // 用于绘制的倍数（绘画时Point的坐标将乘以此倍数）
        const val MULTIPLE = 15

        // 每次移动的距离
        const val GAME_MOVE_DISTANCE = 0.5

        // 绘图的大小
        const val DRAW_SIZE_X = GAME_MAP_X * MULTIPLE
        const val DRAW_SIZE_Y = GAME_MAP_Y * MULTIPLE
        const val DRAW_RADIUS = 18

        /**
         * 生成一个随机的点
         * @param distanceForGameMap 随机点的范围距离地图四周的距离
         *
         * @return a random point in the map
         */
        fun random(distanceForGameMap: Double = 0.0) = Point(
            Random.nextDouble(distanceForGameMap, GAME_MAP_X - distanceForGameMap),
            Random.nextDouble(distanceForGameMap, GAME_MAP_Y - distanceForGameMap)
        )
    }

    override fun toString(): String {
        return "(%.1f, %.1f)".format(x, y)
    }
}
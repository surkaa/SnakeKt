package com.surkaa.entities.snake

import com.surkaa.controller.GameController
import com.surkaa.entities.Data
import com.surkaa.entities.Point
import com.surkaa.entities.Result
import com.surkaa.entities.food.Food
import com.surkaa.ui.Draw
import com.surkaa.util.fullString
import mu.KotlinLogging
import java.awt.Color
import java.awt.Graphics

/**
 * @author kaa
 */
open class Snake(
    // 蛇的头部
    var head: Point,
    // 蛇的方向
    private var angle: Double = 0.0,
    // 蛇的尾巴 除了头 都是尾巴 body 0 => ... => size-1 head
    var body: MutableList<Point> = mutableListOf(),
    private val headColor: Color = Color(0XFBE816),
    private val bodyColor: Color = Color(0X55C762)
) : Draw {

    private val snakeName = this::class.java.simpleName.fullString()

    private val logger = KotlinLogging.logger {}

    // 是否存活 私有化setter
    var isAlive: Boolean = true
        private set

    val nextTarget get() = head.getTarget(angle)

    /**
     * 当下一个位置空旷时被调用
     */
    private fun move(next: Point) {
        body.add(head)
        head = next
        body.removeAt(0)
    }

    /**
     * 当下一个位置与食物足够靠进的时被调用
     */
    private fun eat(food: Food) {
        Data.foods.remove(food)
        Data.foods.add(Food.random())
        val newHead: Point = nextTarget
        body.add(head)
        head = newHead
    }

    override fun onDraw(g: Graphics) {
        head.onDraw(g, color = headColor)
        for (point in body) {
            point.onDraw(g, color = bodyColor)
        }
    }

    private fun die() {
        isAlive = false
    }

    //<editor-fold desc="run">
    private fun isHitOther(other: Snake): Boolean {
        if (head.isNear(other.head))
            return true
        other.body.forEach {
            if (it.isNear(head))
                return true
        }
        return false
    }

    fun run() {
        // turn返回非空时扭转方向
        turn()?.let {
            angle = it
        }
        when (val result = getResult()) {
            is Result.Eat -> {
                // TODO 开启多线程后可能造成ConcurrentModificationException
                eat(result.food)
                logger.warn { "$snakeName eat ${result.food}" }
            }

            is Result.HitOther -> {
                // TODO 可以通过result.snake给被撞的蛇加分
                die()
                logger.warn { "$snakeName die by hit other ${result.snake.snakeName}" }
            }

            is Result.Move -> {
                move(result.next)
                logger.warn { "$snakeName move to ${result.next}" }
            }

            Result.HitSelf -> {
                die()
                logger.warn { "$snakeName die by hit self" }
            }

            Result.HitWall -> {
                die()
                logger.warn { "$snakeName die by hit wall" }
            }
        }
    }

    /**
     * 让蛇必要时自己走
     */
    open fun turn(): Double? = null

    /**
     * 获取Result
     */
    private fun getResult(): Result {
        val next = nextTarget

        // 检查是否撞到边界
        if (next.isBroken())
            return Result.HitWall

        // 比较器
        val predicateSelf: (Point) -> Boolean = { p -> p.isNear(next) }

        // 根据canHitSelf检查是否撞到自身
        if (!GameController.canHitSelf && body.any(predicateSelf))
            return Result.HitSelf

        // 检查是否撞到其他蛇
        Data.snakes.forEach { other ->
            // 排除自身
            if (other != this@Snake && isHitOther(other))
                return Result.HitOther(other)
        }

        // 检查能否吃到食物
        Data.foods.forEach { food ->
            if (food.isNear(next))
                return Result.Eat(food)
        }

        return Result.Move(next)
    }
    //</editor-fold>

    //<editor-fold desc="equals & hashCode & toString">
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Snake

        if (head != other.head) return false
        if (angle != other.angle) return false
        if (body != other.body) return false
        if (isAlive != other.isAlive) return false
        if (headColor != other.headColor) return false
        if (bodyColor != other.bodyColor) return false

        return true
    }

    override fun hashCode(): Int {
        var result = head.hashCode()
        result = 31 * result + angle.hashCode()
        result = 31 * result + body.hashCode()
        result = 31 * result + isAlive.hashCode()
        result = 31 * result + headColor.hashCode()
        result = 31 * result + bodyColor.hashCode()
        return result
    }

    override fun toString(): String {
        return "${body.first()}==>${body.size - 1}==>$head"
    }
    //</editor-fold>

}
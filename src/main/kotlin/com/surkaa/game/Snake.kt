package com.surkaa.game

import com.surkaa.ui.Draw
import java.awt.Color
import java.awt.Graphics
import java.awt.event.KeyEvent
import java.awt.event.MouseEvent

/**
 * @author kaa
 */
open class Snake(
    // 蛇的头部
    var head: Point,
    // 蛇的方向
    private var angle: Double = 0.0,
    // 蛇的尾巴 除了头 都是尾巴 tail 0 => ... => size-1 head
    var tail: MutableList<Point> = mutableListOf(),
    private val headColor: Color = Color.BLACK,
    private val tailColor: Color = Color.GRAY
) : Draw {

    // 是否存活 私有化setter
    var isAlive: Boolean = true
        private set

    val nextTarget get() = head.getTarget(angle)

    /**
     * 当下一个位置空旷时被调用
     */
    private fun move() {
        val newHead: Point = nextTarget
        tail.add(head)
        head = newHead
        tail.removeAt(0)
    }

    /**
     * 当下一个位置与食物足够靠进的时被调用
     */
    private fun eat() {
        val newHead: Point = head.getTarget(angle)
        tail.add(head)
        head = newHead
    }

    override fun onDraw(g: Graphics) {
        head.onDraw(g, color = headColor)
        for (point in tail) {
            point.onDraw(g, color = tailColor)
        }
    }

    private fun die() {
        isAlive = false
    }

    private fun isHitOther(other: Snake): Boolean {
        if (head.isNear(other.head))
            return true
        other.tail.forEach {
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
        val manager = Manager.getInstance()
        when (val result = getResult(manager)) {
            is Result.Eat -> {
                // TODO 开启多线程后可能造成ConcurrentModificationException
                manager.onEat(result.food)
                eat()
            }
            // TODO 可以通过result.snake给被撞的蛇加分
            is Result.HitOther, Result.HitSelf, Result.HitWall -> die()
            Result.Move -> move()
        }
    }

    /**
     * 让蛇必要时自己走
     */
    open fun turn(): Double? = null

    /**
     * 获取Result
     */
    private fun getResult(manager: Manager): Result {
        val next = nextTarget

        // 检查是否撞到边界
        if (next.isBroken())
            return Result.HitWall

        // 比较器
        val predicateSelf: (Point) -> Boolean = { p -> p.isNear(next) }

        manager.let {

            // 根据canHitSelf检查是否撞到自身
            if (!it.canHitSelf && tail.any(predicateSelf))
                return Result.HitSelf

            // 检查是否撞到其他蛇
            it.snakes.forEach { other ->
                // 排除自身
                if (other != this@Snake && isHitOther(other))
                    return Result.HitOther(other)
            }

            // 检查能否吃到食物
            it.foods.forEach { food ->
                if (food.isNear(next))
                    return Result.Eat(food)
            }
        }
        return Result.Move
    }

    //<editor-fold desc="KeyListener & MouseListener">
    fun mouseDragged(e: MouseEvent) = targetMouse(e)

    fun mousePressed(e: MouseEvent) = targetMouse(e)

    /**
     * 让蛇往鼠标点击的位置走
     */
    private fun targetMouse(e: MouseEvent) {
        val targetPoint = Point(
            e.x / Point.MULTIPLE.toDouble(),
            e.y / Point.MULTIPLE.toDouble()
        )
        val newAngle = head.getAngle(targetPoint)
        angle = newAngle
    }

    /**
     * 监听按键修改蛇的方向
     */
    fun keyPressed(e: KeyEvent) = when (e.keyCode) {
        KeyEvent.VK_W -> angle = 270.0
        KeyEvent.VK_S -> angle = 90.0
        KeyEvent.VK_A -> angle = 180.0
        KeyEvent.VK_D -> angle = 0.0
        KeyEvent.VK_LEFT -> angle -= 10.0
        KeyEvent.VK_RIGHT -> angle += 10.0
        else -> {}
    }
    //</editor-fold>

    //<editor-fold desc="equals & hashCode & toString">
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Snake

        if (head != other.head) return false
        if (angle != other.angle) return false
        if (tail != other.tail) return false
        if (isAlive != other.isAlive) return false
        if (headColor != other.headColor) return false
        if (tailColor != other.tailColor) return false

        return true
    }

    override fun hashCode(): Int {
        var result = head.hashCode()
        result = 31 * result + angle.hashCode()
        result = 31 * result + tail.hashCode()
        result = 31 * result + isAlive.hashCode()
        result = 31 * result + headColor.hashCode()
        result = 31 * result + tailColor.hashCode()
        return result
    }

    override fun toString(): String {
        return "${tail.first()}==>${tail.size - 1}==>$head"
    }
    //</editor-fold>

}
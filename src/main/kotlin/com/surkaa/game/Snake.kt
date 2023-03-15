package com.surkaa.game

import com.surkaa.ui.Draw
import java.awt.Color
import java.awt.Graphics
import java.awt.event.KeyEvent
import java.awt.event.MouseEvent

class Snake(
    var head: Point,
    var angle: Double = 0.0,
    var tail: MutableList<Point> = mutableListOf(),
    var isAlive: Boolean = true,
    private val headColor: Color = Color.BLACK,
    private val tailColor: Color = Color.GRAY
) : Draw {

    val nextTarget get() = head.target(angle)

    /**
     * 当下一个位置空旷时被调用
     */
    fun onMove() {
        val newHead: Point = nextTarget
        tail.add(head)
        head = newHead
        tail.removeAt(0)
    }

    /**
     * 当下一个位置与食物足够靠进的时被调用
     */
    fun onEat() {
        val newHead: Point = head.target(angle)
        tail.add(head)
        head = newHead
    }

    override fun onDraw(g: Graphics) {
        head.onDraw(g, color = headColor)
        for (point in tail) {
            point.onDraw(g, color = tailColor)
        }
    }

    fun die() {
        isAlive = false
    }

    //<editor-fold desc="KeyListener & MouseListener">
    fun mouseDragged(e: MouseEvent) = targetMouse(e)

    fun mousePressed(e: MouseEvent) = targetMouse(e)

    private fun targetMouse(e: MouseEvent) {
        val targetPoint = Point(
            e.x / Point.MULTIPLE.toDouble(),
            e.y / Point.MULTIPLE.toDouble()
        )
        val newAngle = head.getAngle(targetPoint)
        angle = newAngle
    }

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
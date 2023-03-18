package com.surkaa.snake

import com.surkaa.game.Point
import com.surkaa.game.Snake
import java.awt.Color
import java.awt.event.*

class PlayerSnake(
    head: Point,
    angle: Double = 0.0,
    tail: MutableList<Point> = mutableListOf(),
    headColor: Color = Color.BLACK,
    tailColor: Color = Color.GRAY
) : Snake(head, angle, tail, headColor, tailColor),
    KeyListener,
    MouseListener,
    MouseMotionListener {

    private var angle: Double? = null

    override fun turn() = angle

    /**
     * 让蛇往鼠标点击的位置走
     */
    private fun targetMouse(e: MouseEvent?) {
        if (e == null) return
        val targetPoint = Point(
            e.x / Point.MULTIPLE.toDouble(),
            e.y / Point.MULTIPLE.toDouble()
        )
        val newAngle = head.getAngle(targetPoint)
        angle = newAngle
    }

    override fun keyTyped(e: KeyEvent?) {}

    /**
     * 监听按键修改蛇的方向
     */
    override fun keyPressed(e: KeyEvent?) = when (e?.keyCode) {
        KeyEvent.VK_W -> angle = 270.0
        KeyEvent.VK_S -> angle = 90.0
        KeyEvent.VK_A -> angle = 180.0
        KeyEvent.VK_D -> angle = 0.0
        KeyEvent.VK_LEFT -> angle = angle?.minus(10.0)
        KeyEvent.VK_RIGHT -> angle = angle?.plus(10.0)
        else -> {}
    }

    override fun keyReleased(e: KeyEvent?) {}

    override fun mouseClicked(e: MouseEvent?) {}

    override fun mousePressed(e: MouseEvent?) = targetMouse(e)

    override fun mouseReleased(e: MouseEvent?) {}

    override fun mouseEntered(e: MouseEvent?) {}

    override fun mouseExited(e: MouseEvent?) {}

    override fun mouseDragged(e: MouseEvent?) = targetMouse(e)

    override fun mouseMoved(e: MouseEvent?) {}
}
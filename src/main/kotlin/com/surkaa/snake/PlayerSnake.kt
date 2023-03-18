package com.surkaa.snake

import com.surkaa.game.Point
import java.awt.Color
import java.awt.event.*

class PlayerSnake(
    head: Point,
    angle: Double = 0.0,
    tail: MutableList<Point> = mutableListOf(),
    headColor: Color = Color.BLACK,
    tailColor: Color = Color.GRAY
) : DontHitWallSnake(head, angle, tail, headColor, tailColor),
    KeyListener,
    MouseListener,
    MouseMotionListener {

    private var nextAngle: Double? = null

    /**
     * 父类SmartSnake.turn的返回不为空的时候依照父类走
     */
    override fun turn() = super.turn() ?: nextAngle

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
        nextAngle = newAngle
    }

    override fun keyTyped(e: KeyEvent?) {}

    /**
     * 监听按键修改蛇的方向
     */
    override fun keyPressed(e: KeyEvent?) = when (e?.keyCode) {
        KeyEvent.VK_W -> nextAngle = 270.0
        KeyEvent.VK_S -> nextAngle = 90.0
        KeyEvent.VK_A -> nextAngle = 180.0
        KeyEvent.VK_D -> nextAngle = 0.0
        KeyEvent.VK_LEFT -> nextAngle = nextAngle?.minus(10.0)
        KeyEvent.VK_RIGHT -> nextAngle = nextAngle?.plus(10.0)
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
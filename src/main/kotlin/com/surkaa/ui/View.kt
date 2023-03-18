package com.surkaa.ui

import com.surkaa.game.Manager
import com.surkaa.game.Point
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import javax.swing.JPanel

/**
 * @author kaa
 */
class View : JPanel() {

    private val manager: Manager = Manager.getInstance()

    init {
        // 监听鼠标运动
        addMouseListener(manager)
        addMouseMotionListener(manager)
    }

    // 绘画
    override fun paint(g: Graphics?) {
        super.paint(g)
        g?.let {
            // 画背景
            g.color = Color.LIGHT_GRAY
            g.fillRect(0, 0, Point.DRAW_SIZE_X, Point.DRAW_SIZE_Y)
            // 画主体
            try {
                manager.onDraw(g)
            } catch (_: ConcurrentModificationException) {

            }
        }
    }

    // 窗口大小
    override fun getPreferredSize() = Dimension(
        Point.DRAW_SIZE_X,
        Point.DRAW_SIZE_Y
    )

}
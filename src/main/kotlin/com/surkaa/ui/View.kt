package com.surkaa.ui

import com.surkaa.game.Manager
import com.surkaa.game.Point
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import javax.swing.JPanel

class View : JPanel() {
    private val manager: Manager = Manager.getInstance()

    init {
        addMouseListener(manager)
        addMouseMotionListener(manager)
    }

    override fun paint(g: Graphics?) {
        super.paint(g)
        g?.let {
            // 画背景
            g.color = Color.LIGHT_GRAY
            g.fillRect(0, 0, Point.DRAW_SIZE_X, Point.DRAW_SIZE_Y)
            // 画主体
            manager.onDraw(g)
        }
    }

    override fun getPreferredSize() = Dimension(
        Point.DRAW_SIZE_X,
        Point.DRAW_SIZE_Y
    )

}
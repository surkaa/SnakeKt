package com.surkaa.controller

import java.awt.event.*

object MouseController :
    MouseListener,
    MouseWheelListener,
    MouseMotionListener {

    private var mouseListener: MouseListener? = null
    private var mouseWheelListener: MouseWheelListener? = null
    private var mouseMotionListener: MouseMotionListener? = null

    override fun mouseClicked(e: MouseEvent?) {
        mouseListener?.mouseClicked(e)
    }

    override fun mousePressed(e: MouseEvent?) {
        mouseListener?.mousePressed(e)
    }

    override fun mouseReleased(e: MouseEvent?) {
        mouseListener?.mouseReleased(e)
    }

    override fun mouseEntered(e: MouseEvent?) {
        mouseListener?.mouseEntered(e)
    }

    override fun mouseExited(e: MouseEvent?) {
        mouseListener?.mouseExited(e)
    }

    override fun mouseWheelMoved(e: MouseWheelEvent?) {
        mouseWheelListener?.mouseWheelMoved(e)
    }

    override fun mouseDragged(e: MouseEvent?) {
        mouseMotionListener?.mouseDragged(e)
    }

    override fun mouseMoved(e: MouseEvent?) {
        mouseMotionListener?.mouseMoved(e)
    }

    fun setMouseListener(l: MouseListener?) {
        this.mouseListener = l
    }

    fun setMouseMotionListener(l: MouseMotionListener?) {
        this.mouseMotionListener = l
    }

}
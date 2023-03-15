package com.surkaa.ui

import java.awt.Graphics

/**
 * Interface for drawing.
 * 用于绘制的接口
 * 所有需要绘制的类都需要实现这个接口
 * @author Kaa
 */
interface Draw {

    /**
     * @param g Graphics
     */
    fun onDraw(g: Graphics)
}
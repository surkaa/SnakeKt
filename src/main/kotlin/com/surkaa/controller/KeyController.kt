package com.surkaa.controller

import java.awt.event.KeyEvent
import java.awt.event.KeyListener

object KeyController : KeyListener {

    private val keyListeners: ArrayList<KeyListener> = ArrayList()

    /**
     * 监听键盘, 使游戏加速或减速
     * esc结束程序
     * space暂停或继续游戏
     */
    override fun keyPressed(e: KeyEvent?) {
        keyListeners.forEach { it.keyPressed(e) }
    }

    /**
     * 监听键盘, 恢复每帧睡眠时间
     */
    override fun keyReleased(e: KeyEvent?) {
        keyListeners.forEach { it.keyReleased(e) }
    }

    override fun keyTyped(e: KeyEvent?) {
        keyListeners.forEach { it.keyTyped(e) }
    }

    fun addKeyListener(l: KeyListener) {
        this.keyListeners.add(l)
    }

}
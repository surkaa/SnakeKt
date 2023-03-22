package com.surkaa.controller

import com.surkaa.Manager
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import kotlin.system.exitProcess

object KeyController : KeyListener {

    private var keyListener: KeyListener? = null
    private val manager = Manager.getInstance()

    /**
     * 监听键盘, 使游戏加速或减速
     * esc结束程序
     * space暂停或继续游戏
     */
    override fun keyPressed(e: KeyEvent?) {
        // TODO 加速减速只能所有蛇都加速减速
        if (e == null) return
        when (e.keyCode) {
            KeyEvent.VK_CONTROL, KeyEvent.VK_DOWN -> {
                manager.toSlow()
            }

            KeyEvent.VK_SHIFT, KeyEvent.VK_UP -> {
                manager.toFast()
            }

            KeyEvent.VK_SPACE -> {
                if (manager.isPause) manager.start()
                else manager.pause()
            }

            KeyEvent.VK_ESCAPE -> {
                exitProcess(0)
            }

            else -> {
                keyListener?.keyPressed(e)
            }
        }
    }

    /**
     * 监听键盘, 恢复每帧睡眠时间
     */
    override fun keyReleased(e: KeyEvent?) {
        if (e == null) return
        when (e.keyCode) {
            KeyEvent.VK_SHIFT,
            KeyEvent.VK_CONTROL,
            KeyEvent.VK_UP,
            KeyEvent.VK_DOWN -> {
                manager.toGeneral()
            }

            else -> {
                keyListener?.keyReleased(e)
            }
        }
    }

    override fun keyTyped(e: KeyEvent?) {
        keyListener?.keyTyped(e)
    }

    fun setKeyListener(l: KeyListener?) {
        this.keyListener = l
    }

}
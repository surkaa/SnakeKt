package com.surkaa.ui

import com.surkaa.game.Manager
import javax.swing.JFrame

class GameFrame(title: String) : JFrame(title) {

    private val manager: Manager = Manager.getInstance()
    private val view: View = View()

    init {
        // 关闭窗口后程序结束
        this.defaultCloseOperation = EXIT_ON_CLOSE
        // 窗口大小不可变
        this.isResizable = false
        // 添加视图
        this.add(view)
        // 自动调整窗口大小 根据view的大小
        this.pack()
        // 显示窗口
        this.isVisible = true
        // 窗口居中
        this.setLocationRelativeTo(null)
        // 添加键盘监听
        this.addKeyListener(manager)
        // 添加field运行监听
        manager.addRunListener(object : Manager.RunListener {
            override fun beforeRun() {
                view.repaint()
            }

            override fun afterRun() {
            }
        })
    }

    fun defaultGame() = manager.defaultStart()

}
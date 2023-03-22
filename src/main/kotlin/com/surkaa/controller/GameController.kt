package com.surkaa.controller

import com.surkaa.entities.Data
import com.surkaa.entities.Point
import com.surkaa.entities.food.Food
import com.surkaa.entities.snake.DontHitWallSnake
import com.surkaa.entities.snake.PlayerSnake
import com.surkaa.entities.snake.ToFoodSnake
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import kotlin.system.exitProcess

object GameController : KeyListener {

    init {
        KeyController.addKeyListener(this)
    }

    // 游戏每帧的睡眠时间(ms)
    private const val sleepTimeGeneral = 80L
    private const val sleepTimeFast = 8L
    private const val sleepTimeSlow = 160L
    var sleepTime: Long = sleepTimeGeneral

    // 是否处于暂停状态
    var isPause: Boolean = false

    // 能否撞到自身
    var canHitSelf: Boolean = true

    //<editor-fold desc="RunListener">
    // 储存监听器
    private var runListener: RunListener? = null

    /**
     * 用于运行前后监听
     */
    interface RunListener {
        fun beforeRun()
        fun afterRun()
    }

    fun setRunListener(runListener: RunListener) {
        this.runListener = runListener
    }

    private fun toFast() {
        sleepTime = sleepTimeFast
    }

    private fun toSlow() {
        sleepTime = sleepTimeSlow
    }

    private fun toGeneral() {
        sleepTime = sleepTimeGeneral
    }

    // 开启游戏
    private fun start() {
        isPause = false
        Thread(runnable).start()
    }

    // 暂停游戏
    private fun pause() {
        isPause = true
    }

    // 开启游戏(或者说是继续游戏)用的Runnable接口
    private val runnable = Runnable {
        while (!isPause) {
            runListener?.beforeRun()
            for (snake in Data.snakes) {
                // 跳过死了的蛇
                if (!snake.isAlive) continue
                snake.run()
            }
            runListener?.afterRun()
            Thread.sleep(sleepTime)
        }
    }
    //</editor-fold>

    fun defaultStart() {
        // 生成一条地图左上角的蛇
        val x: Double = Point.GAME_MAP_X / 4.0
        val y: Double = Point.GAME_MAP_Y / 4.0
        val dx: Double = Point.GAME_MOVE_DISTANCE
        val body = ArrayList<Point>()
        // 尾巴
        repeat(30) {
            body.add(0, Point(x - dx * it, y))
        }
        Data.snakes.add(PlayerSnake(head = Point(x, y), angle = 0.0, body = body))
        Data.snakes.add(DontHitWallSnake(head = Point.random()))
        Data.snakes.add(ToFoodSnake(head = Point.random()))
        // 添加十个食物
        repeat(10) {
            Data.foods.add(Food.random())
        }
        // 开始游戏
        start()
    }

    override fun keyTyped(e: KeyEvent?) {}

    override fun keyPressed(e: KeyEvent?) {
        // TODO 加速减速只能所有蛇都加速减速
        when (e?.keyCode) {
            KeyEvent.VK_CONTROL, KeyEvent.VK_DOWN -> {
                toSlow()
            }

            KeyEvent.VK_SHIFT, KeyEvent.VK_UP -> {
                toFast()
            }

            KeyEvent.VK_SPACE -> {
                if (isPause) start()
                else pause()
            }

            KeyEvent.VK_ESCAPE -> {
                exitProcess(0)
            }
        }
    }

    override fun keyReleased(e: KeyEvent?) {
        when (e?.keyCode) {
            KeyEvent.VK_SHIFT,
            KeyEvent.VK_CONTROL,
            KeyEvent.VK_UP,
            KeyEvent.VK_DOWN -> {
                toGeneral()
            }
        }
    }

}
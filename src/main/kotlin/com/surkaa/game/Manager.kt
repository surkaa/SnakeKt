package com.surkaa.game

import com.surkaa.snake.DontHitWallSnake
import com.surkaa.snake.PlayerSnake
import com.surkaa.snake.ToFoodSnake
import com.surkaa.ui.Draw
import java.awt.Graphics
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import kotlin.system.exitProcess

class Manager private constructor() : Draw, KeyListener, MouseAdapter() {

    // 游戏每帧的睡眠时间(ms)
    private val sleepTimeGeneral = 80L
    private val sleepTimeFast = 8L
    private val sleepTimeSlow = 160L
    private var sleepTime: Long = sleepTimeGeneral

    // 是否处于暂停状态
    private var isPause: Boolean = false
    // 能否撞到自身
    var canHitSelf: Boolean = true

    // 存储所有蛇 食物
    val snakes: MutableList<Snake> = mutableListOf()
    val foods: MutableList<Food> = mutableListOf()

    // 仅有一条蛇的时候将某些交互事件传递到此蛇
    private val mainSnake : PlayerSnake?
        get() = snakes.firstOrNull {
            it is PlayerSnake
        } as PlayerSnake?

    //<editor-fold desc="RunListener">
    // 储存监听器
    private var runListener: RunListener? = null

    /**
     * 用于运行前监听
     */
    interface RunListener {
        fun beforeRun()
        fun afterRun()
    }

    fun setRunListener(runListener: RunListener) {
        this.runListener = runListener
    }
    //</editor-fold>

    //<editor-fold desc="Game Controller (暂停继续)">
    // 开启游戏
    fun start() {
        isPause = false
        Thread(runnable).start()
    }

    // 暂停游戏
    fun pause() {
        isPause = true
    }

    // 开启游戏(或者说是继续游戏)用的Runnable接口
    private val runnable = Runnable {
        while (!isPause) {
            run()
        }
    }
    //</editor-fold>

    //<editor-fold desc="Run (CPU)">
    private fun run() {
        runListener?.beforeRun()
        snakes.forEach {
            // 跳过死了的蛇 return@forEach相当于continue
            if (!it.isAlive) return@forEach
            it.run()
        }
        runListener?.afterRun()
        Thread.sleep(sleepTime)
    }

    fun onEat(food: Food) {
        foods.remove(food)
        foods.add(Food.random())
    }
    //</editor-fold>

    //<editor-fold desc="KeyListener & MouseListener">
    // TODO 加速减速只能所有蛇都加速减速
    /**
     * 监听键盘, 使游戏加速或减速
     * esc结束程序
     * space暂停或继续游戏
     */
    override fun keyPressed(e: KeyEvent?) {
        if (e == null) return
        when (e.keyCode) {
            KeyEvent.VK_CONTROL, KeyEvent.VK_DOWN -> sleepTime = sleepTimeSlow
            KeyEvent.VK_SHIFT, KeyEvent.VK_UP -> sleepTime = sleepTimeFast
            KeyEvent.VK_SPACE -> {
                if (isPause) start()
                else pause()
            }
            KeyEvent.VK_ESCAPE -> exitProcess(0)
            else -> mainSnake?.keyPressed(e)
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
            KeyEvent.VK_DOWN -> sleepTime = sleepTimeGeneral
        }
    }

    override fun keyTyped(e: KeyEvent?) {
    }

    /**
     * 监听鼠标 若只有一条蛇就将鼠标事件传递给这条蛇
     */
    override fun mouseDragged(e: MouseEvent?) {
        if (e == null) return
        mainSnake?.mouseDragged(e)
    }

    /**
     * 监听鼠标 若只有一条蛇就将鼠标事件传递给这条蛇
     */
    override fun mousePressed(e: MouseEvent?) {
        if (e == null) return
        mainSnake?.mousePressed(e)
    }
    //</editor-fold>

    override fun onDraw(g: Graphics) {
        foods.forEach { it.onDraw(g) }
        snakes.forEach { it.onDraw(g) }
    }

    fun defaultStart() {
        // 生成一条地图左上角的蛇
        val x: Double = Point.GAME_MAP_X / 4.0
        val y: Double = Point.GAME_MAP_Y / 4.0
        val dx: Double = Point.GAME_MOVE_DISTANCE
        val tail = ArrayList<Point>()
        // 尾巴
        repeat(30) {
            tail.add(0, Point(x - dx * it, y))
        }
        snakes.add(
            PlayerSnake(
                head = Point(x, y),
                angle = 0.0,
                tail = tail
            )
        )
        snakes.add(DontHitWallSnake(head = Point.random()))
        snakes.add(ToFoodSnake(head = Point.random()))
        // 添加十个食物
        repeat(10) {
            foods.add(Food.random())
        }
        // 开始游戏
        start()
    }

    companion object {
        // 单例模式
        private var INSTANCE: Manager? = null

        fun getInstance() = INSTANCE ?: Manager().also {
            INSTANCE = it
        }
    }

}
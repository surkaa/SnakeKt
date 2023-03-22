package com.surkaa

import com.surkaa.controller.KeyController
import com.surkaa.entities.Point
import com.surkaa.entities.food.Food
import com.surkaa.entities.snake.DontHitWallSnake
import com.surkaa.entities.snake.PlayerSnake
import com.surkaa.entities.snake.Snake
import com.surkaa.entities.snake.ToFoodSnake
import com.surkaa.ui.Draw
import java.awt.Graphics
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent

class Manager private constructor() : Draw, MouseAdapter() {

    // 游戏每帧的睡眠时间(ms)
    val sleepTimeGeneral = 80L
    val sleepTimeFast = 8L
    val sleepTimeSlow = 160L
    var sleepTime: Long = sleepTimeGeneral

    // 是否处于暂停状态
    var isPause: Boolean = false

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
        val body = ArrayList<Point>()
        // 尾巴
        repeat(30) {
            body.add(0, Point(x - dx * it, y))
        }
        PlayerSnake(head = Point(x, y), angle = 0.0, body = body).let {
            snakes.add(it)
            KeyController.setKeyListener(it)
        }
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
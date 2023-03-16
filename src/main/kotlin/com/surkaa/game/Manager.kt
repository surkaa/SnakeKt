package com.surkaa.game

import com.surkaa.ui.Draw
import java.awt.Graphics
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import kotlin.system.exitProcess

class Manager private constructor() : Draw, KeyListener, MouseAdapter() {

    // 游戏每帧的睡眠时间(ms)
    private var sleepTime: Long = 100L
    // 是否处于暂停状态
    private var isPause: Boolean = false
    // 能否撞到自身
    private var canHitSelf: Boolean = true

    // 存储所有蛇 食物
    private val snakes: MutableList<Snake> = mutableListOf()
    private val foods: MutableList<Food> = mutableListOf()

    // 仅有一条蛇的时候将某些交互事件传递到此蛇
    private val mainSnake
        get() = if (snakes.size == 1) snakes.first() else null

    //<editor-fold desc="RunListener">
    // 储存监听器
    private val runListeners: MutableList<RunListener> = mutableListOf()

    /**
     * 用于运行前监听
     */
    interface RunListener {
        fun beforeRun()
        fun afterRun()
    }

    fun addRunListener(runListener: RunListener) = runListeners.add(runListener)

    fun removeRunListener(runListener: RunListener) = runListeners.remove(runListener)

    fun clearRunListener() = runListeners.clear()
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
        runListeners.forEach { it.beforeRun() }
        snakes.forEach { runOne(it) }
        runListeners.forEach { it.afterRun() }
        Thread.sleep(sleepTime)
    }

    // 走一边这条蛇 // TODO 值得优化, 开线程跑多条蛇
    private fun runOne(snake: Snake) {
        if (!snake.isAlive) return
        when (val result = getResult(snake)) {
            is Result.Move -> onMove(snake)
            is Result.Eat -> onEat(snake, result.food)
            is Result.HitSelf -> onDie(snake)
            is Result.HitWall -> onDie(snake)
            is Result.HitOther -> onDie(snake)
        }
    }

    private fun onMove(snake: Snake) = snake.onMove()

    private fun onDie(snake: Snake) = snake.die()

    private fun onEat(snake: Snake, food: Food) {
        snake.onEat()
        foods.remove(food)
        foods.add(Food.random())
    }

    /**
     * 获取Result
     */
    private fun getResult(snake: Snake): Result {
        val next = snake.nextTarget

        // 检查是否撞到边界
        if (next.isBroken())
            return Result.HitWall

        // 比较器
        val predicateSelf: (Point) -> Boolean = { p -> p.isNear(next) }

        // 根据canHitSelf检查是否撞到自身
        if (!canHitSelf && snake.tail.any(predicateSelf))
            return Result.HitSelf

        // 检查是否撞到其他蛇
        snakes.forEach { other ->
            // 排除自身
            if (other != snake) {
                if (other.head.isNear(next) || other.tail.any(predicateSelf))
                    return Result.HitOther(other)
            }
        }

        // 检查能否吃到食物
        foods.forEach { food ->
            if (food.isNear(next))
                return Result.Eat(food)
        }

        return Result.Move
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
            KeyEvent.VK_CONTROL, KeyEvent.VK_DOWN -> sleepTime = 150
            KeyEvent.VK_SHIFT, KeyEvent.VK_UP -> sleepTime = 10
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
            KeyEvent.VK_SHIFT, KeyEvent.VK_CONTROL, KeyEvent.VK_UP, KeyEvent.VK_DOWN -> sleepTime = 50
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
        snakes.forEach { it.onDraw(g) }
        foods.forEach { it.onDraw(g) }
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
            Snake(
                head = Point(x, y),
                angle = 0.0,
                tail = tail
            )
        )
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
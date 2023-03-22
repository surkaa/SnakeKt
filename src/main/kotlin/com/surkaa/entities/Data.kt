package com.surkaa.entities

import com.surkaa.entities.food.Food
import com.surkaa.entities.snake.Snake
import com.surkaa.ui.Draw
import java.awt.Graphics

object Data : Draw {

    // 存储所有蛇 食物
    val snakes: MutableList<Snake> = mutableListOf()
    val foods: MutableList<Food> = mutableListOf()

    override fun onDraw(g: Graphics) {
        foods.forEach { it.onDraw(g) }
        snakes.forEach { it.onDraw(g) }
    }


}
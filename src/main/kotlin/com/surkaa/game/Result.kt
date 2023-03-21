package com.surkaa.game

import com.surkaa.food.Food

/**
 * 用于判定蛇每走一步的结果, 并存储一些数据
 * @author kaa
 */
sealed class Result {

    // 撞到自身
    object HitSelf : Result()

    // 撞到墙
    object HitWall : Result()

    // 吃食物
    data class Eat(val food: Food) : Result()

    // 正常移动
    data class Move(val next: Point) : Result()

    // 撞到其他蛇
    data class HitOther(val snake: Snake) : Result()

    override fun toString(): String {
        return when (this) {
            is HitSelf -> "HitSelf"
            is HitWall -> "HitWall"
            is Eat -> "Eat[food=$food]"
            is Move -> "Move[next=$next]"
            is HitOther -> "HitOther[snake=$snake]"
        }
    }
}
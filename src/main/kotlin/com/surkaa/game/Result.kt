package com.surkaa.game

sealed class Result {
    object Move : Result()
    object HitSelf : Result()
    object HitWall : Result()
    data class Eat(val food: Food) : Result()
    data class HitOther(val snake: Snake) : Result()

    override fun toString(): String {
        return when (this) {
            is Move -> "Move"
            is Eat -> "Eat[food=$food]"
            is HitSelf -> "HitSelf"
            is HitOther -> "HitOther[snake=$snake]"
            is HitWall -> "HitWall"
        }
    }
}
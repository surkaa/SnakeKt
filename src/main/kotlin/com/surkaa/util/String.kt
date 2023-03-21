package com.surkaa.util

fun String.fullString(
    fullLength: Int = 20,
    fullChar: Char = ' '
): String {
    val sb = StringBuilder(this)
    return if (this.length < fullLength) {
        // 在末尾添加fullLength - length个fullChar
        repeat(fullLength - length) {
            sb.append(fullChar)
        }
        sb.toString()
    } else if (this.length > fullLength) {
        sb.substring(0, fullLength)
    } else {
        this
    }
}
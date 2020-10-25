package com.example.game.game.util

import android.util.DisplayMetrics
import android.view.View
import android.view.Window
import kotlin.math.sqrt


object Configurations {

    fun getItemSize(container: View, itemsCount: Int): Int {
        return container.width.coerceAtMost(container.height) / sqrt(itemsCount.toFloat()).toInt()
    }

    fun getPlayingFieldSize(window: Window): Int {
        val displayMetrics = DisplayMetrics()
        window.windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels
        return width.coerceAtMost(height)
    }

    fun getSpanCount(size: Int): Int {
        return sqrt(size.toFloat()).toInt()
    }

}
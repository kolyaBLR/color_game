package com.example.game.game

import androidx.annotation.ColorInt
import androidx.core.graphics.ColorUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ColorFieldViewModel : ViewModel() {

    private val colors = ArrayList<Int>()
    private var prevColor = 0
    var nextColor = 0
        private set

    val colorsChangedAction = MutableLiveData<Pair<Int, Int>>()

    fun onColorDropped(@ColorInt color: Int) {
        colors.add(color)
        if (colors.size == 1) {
            prevColor = colors.first()
            nextColor = colors.first()
        } else {
            prevColor = nextColor
            var nextColor = colors[0]
            var prevColor = nextColor
            for (i in 1 until colors.size) {
                prevColor = nextColor
                val ration = 1f.div(i + 1f)
                nextColor = ColorUtils.blendARGB(colors[i], nextColor, ration)
            }
            this.prevColor = prevColor
            this.nextColor = nextColor
        }
        colorsChangedAction.value = Pair(prevColor, nextColor)
    }
}
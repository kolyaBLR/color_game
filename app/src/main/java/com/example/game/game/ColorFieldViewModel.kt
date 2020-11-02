package com.example.game.game

import android.app.Application
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.game.game.levels.Level

class ColorFieldViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var level: Level
    lateinit var outputColors: List<Int>
        private set

    private val colors = ArrayList<Int>()
    private var prevColor = 0

    var nextColor = 0
        private set
    val lastColor: Int
        get() = colors.last()

    val colorsChangedAction = MutableLiveData<Pair<Int, Int>>()//selected colors
    val initTargetColorAction = MutableLiveData<Int>()//target color (value)
    val levelCompletedAction = MutableLiveData<Level>()
    val levelFailedAction = MutableLiveData<Level>()

    fun initLevel(level: Level) {
        this.level = level
        outputColors = level.outputColorsRes.map { ContextCompat.getColor(getApplication(), it) }
        colors.clear()
        nextColor = 0
        prevColor = 0

        var targetColor = ContextCompat.getColor(getApplication(), level.outputColorsRes[0])
        for (i in 1 until level.outputColorsRes.size) {
            val ration = 1f.div(i + 1f)
            val color = ContextCompat.getColor(getApplication(), level.outputColorsRes[i])
            targetColor = ColorUtils.blendARGB(color, targetColor, ration)
        }
        initTargetColorAction.value = targetColor
    }

    fun onColorDropped(@ColorRes color: Int) {
        colors.add(ContextCompat.getColor(getApplication(), color))
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

    fun onColorChangeAnimationEnded() {
        if (colors.sorted() == outputColors.sorted()) {
            levelCompletedAction.value = level
        } else if (colors.size >= outputColors.size) {
            levelFailedAction.value = level
        }
    }
}
package com.example.game.game

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.game.game.item.SquareData
import com.example.game.game.levels.Level
import com.example.game.game.levels.LevelFactory
import kotlin.random.Random

class SquaresViewModel : ViewModel() {

    val touchAction = MutableLiveData<Int>()
    val viewDroppedAction = MutableLiveData<Int>()
    val dragAndDropFailedAction = MutableLiveData<Unit>()
    val nextLevelAction = MutableLiveData<Pair<Int, Level>>()
    private var currentLevel = 0
    private val cache = HashMap<Int, SquareData?>()

    private val gameSettings = LevelFactory().create()
    val size: Int
        get() = nextLevelAction.value!!.second.fieldSize

    init {
        nextLevelAction.value = Pair(currentLevel, gameSettings.levels[currentLevel])
    }

    private fun generateColor(): Int {
        val colors = nextLevelAction.value!!.second.inputColorsRes
        return colors[Random.nextInt(0, colors.size)]
    }

    fun getItem(position: Int): SquareData? {
        if (position >= size) {
            return null
        } else {
            return cache[position] ?: kotlin.run {
                cache[position] = SquareData(generateColor())
                cache[position]
            }
        }
    }

    fun onItemTouched(position: Int) {
        touchAction.value = position
    }

    fun onViewDroppedFromPlayingField(position: Int) {
        cache[position] = null
        viewDroppedAction.value = position
    }

    fun dragAndDropFailed() {
        dragAndDropFailedAction.value = Unit
    }

    fun onLevelCompleted() {
        currentLevel++
        nextLevelAction.value = Pair(currentLevel, gameSettings.levels[currentLevel])
    }
}

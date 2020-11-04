package com.example.game.game

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.game.game.item.SquareData
import com.example.game.game.levels.Level
import com.example.game.game.levels.LevelFactory
import kotlin.random.Random

class SquaresViewModel : ViewModel() {

    private val storageKey = "GAME_STORAGE"
    private val levelKey = "levelKey"
    private lateinit var storage: SharedPreferences

    private var currentLevel: Int
        get() = storage.getInt(levelKey, 0)
        set(value) {
            storage.edit().putInt(levelKey, value).apply()
        }

    val touchAction = MutableLiveData<Int>()
    val viewDroppedAction = MutableLiveData<Int>()
    val dragAndDropFailedAction = MutableLiveData<Unit>()
    val nextLevelAction = MutableLiveData<Pair<Int, Level>>()
    val gameCompletedAction = MutableLiveData<Unit>()

    private val cache = HashMap<Int, SquareData?>()

    private val gameSettings = LevelFactory().create()
    val size: Int
        get() = nextLevelAction.value!!.second.fieldSize

    fun init(context: Context) {
        storage = context.getSharedPreferences(storageKey, Context.MODE_PRIVATE)
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
        if (currentLevel == gameSettings.levels.size - 1) {
            currentLevel = 0
            gameCompletedAction.value = Unit
        } else {
            currentLevel++
            nextLevelAction.value = Pair(currentLevel, gameSettings.levels[currentLevel])
        }
    }
}

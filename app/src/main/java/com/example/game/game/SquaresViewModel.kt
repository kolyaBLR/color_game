package com.example.game.game

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.game.R
import com.example.game.game.item.SquareData
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

class SquaresViewModel : ViewModel() {

    private val colors = listOf(
        R.color.square_1,
        R.color.square_2,
        R.color.square_3,
        R.color.square_4,
        R.color.square_5,
        R.color.square_6,
        R.color.square_7,
        R.color.square_8
    )

    val size = 36
    val items = MutableLiveData<ArrayList<SquareData>>()
    val touchAction = MutableLiveData<Int>()//todo clear
    val viewDroppedAction = MutableLiveData<Int>()//todo clear
    val dragAndDropFailedAction = MutableLiveData<Unit>()//todo clear

    init {
        val array = ArrayList<SquareData>()
        for (i in 0 until size) {
            array.add(SquareData(generateColor()))
        }
        items.value = array
    }

    private fun generateColor() = colors[Random.nextInt(0, colors.size)]

    fun getItem(position: Int): SquareData? = items.value?.get(position)

    fun onItemTouched(position: Int) {
        touchAction.value = position
    }

    fun onViewDroppedFromPlayingField(position: Int) {
        items.value?.set(position, SquareData(generateColor()))
        viewDroppedAction.value = position
    }

    fun dragAndDropFailed() {
        dragAndDropFailedAction.value = Unit
    }
}

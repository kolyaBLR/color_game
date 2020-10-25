package com.example.game.game

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.game.game.item.SquareData

class ColorFieldViewModel : ViewModel() {

    val addColorAction = MutableLiveData<SquareData>()

    fun onColorDropped(data: SquareData) {
        addColorAction.value = data
    }

}
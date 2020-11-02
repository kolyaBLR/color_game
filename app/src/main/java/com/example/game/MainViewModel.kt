package com.example.game

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.game.game.levels.GameSettings
import com.example.game.game.levels.LevelFactory

class MainViewModel : ViewModel() {

    val playClick = MutableLiveData<Unit>()
    val progressClick = MutableLiveData<Unit>()

    fun onPlayClick() {
        playClick.value = Unit
    }

    fun onProgressClick() {
        progressClick.value = Unit
    }

}

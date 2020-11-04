package com.example.game

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.game.game.levels.GameSettings
import com.example.game.game.levels.LevelFactory

class MainViewModel : ViewModel() {

    val playClick = MutableLiveData<Unit>()

    fun onPlayClick() {
        playClick.value = Unit
    }
}

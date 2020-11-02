package com.example.game.game.levels

import com.example.game.R

class LevelFactory {

    fun create(): GameSettings {
        return GameSettings(
            listOf(
                Level(
                    listOf(R.color.square_7, R.color.square_8),
                    listOf(R.color.square_7, R.color.square_8),
                    16
                ),
                Level(
                    listOf(R.color.square_7, R.color.square_8),
                    listOf(R.color.square_7, R.color.square_7, R.color.square_8),
                    16
                ),
                Level(
                    listOf(R.color.square_7, R.color.square_8),
                    listOf(R.color.square_7, R.color.square_8, R.color.square_8),
                    16
                ),
                Level(
                    listOf(R.color.square_7, R.color.square_8, R.color.square_3),
                    listOf(R.color.square_3, R.color.square_8),
                    25
                ),
                Level(
                    listOf(R.color.square_7, R.color.square_8, R.color.square_3),
                    listOf(R.color.square_3, R.color.square_7),
                    25
                ),
                Level(
                    listOf(R.color.square_7, R.color.square_8, R.color.square_3),
                    listOf(R.color.square_3, R.color.square_7, R.color.square_7),
                    25
                ),
                Level(
                    listOf(R.color.square_7, R.color.square_8, R.color.square_3),
                    listOf(R.color.square_3, R.color.square_3, R.color.square_8),
                    25
                )
            )
        )
    }
}
package com.example.game

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.game.databinding.ActivityMainBinding
import com.example.game.game.fragmens.GameFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val binding = ActivityMainBinding.inflate(layoutInflater)

        supportActionBar?.hide()

        val mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        supportFragmentManager.beginTransaction()
            .add(binding.root.id, GameFragment())
            .commit()
    }
}
package com.example.game

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.game.databinding.ActivityMainBinding
import com.example.game.game.fragmens.CongratsFragment
import com.example.game.game.fragmens.GameFragment
import com.example.game.game.fragmens.GameListener
import com.example.game.menu.MenuFragment
import com.example.game.progress.ProgressFragment

class MainActivity : AppCompatActivity(), GameListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        supportActionBar?.hide()

        supportFragmentManager.beginTransaction()
            .add(binding.root.id, MenuFragment())
            .commit()

        mainViewModel.playClick.observe(this, Observer { onNewGameClick() })
    }

    private fun onNewGameClick() {
        replaceFragment(GameFragment(), true)
    }

    private fun replaceFragment(fragment: Fragment, anim: Boolean) {
        val transaction = supportFragmentManager.beginTransaction()
        if (anim) {
            transaction.setCustomAnimations(
                android.R.anim.fade_in, android.R.anim.fade_out,
                android.R.anim.fade_in, android.R.anim.fade_out
            )
        }
        transaction.replace(binding.root.id, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onGameCompleted() {
        onBackPressed()
        CongratsFragment().show(supportFragmentManager, null)
    }
}
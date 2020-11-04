package com.example.game.game.fragmens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.game.R
import com.example.game.databinding.FragmentGameBinding
import com.example.game.game.SquaresViewModel

class GameFragment : Fragment() {

    private lateinit var gameViewModel: SquaresViewModel
    private var _binding: FragmentGameBinding? = null
    private val binding: FragmentGameBinding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gameViewModel = ViewModelProvider(this)[SquaresViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        gameViewModel.init(requireContext())

        val topFragment = TopFragment()
        val bottomFragment = BottomFragment()
        bottomFragment.setTargetFragment(topFragment, 0)
        childFragmentManager.beginTransaction()
            .add(binding.containerTop.id, topFragment)
            .add(binding.containerBottom.id, bottomFragment)
            .commit()

        gameViewModel.nextLevelAction.observe(this, Observer {
            val level = it.first + 1
            binding.titleTextView.text = getString(R.string.level_n, level)
        })

        gameViewModel.gameCompletedAction.observe(this, Observer {
            (activity as? GameListener)?.onGameCompleted()
        })
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
package com.example.game.menu

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.game.MainViewModel
import com.example.game.databinding.FragmentMenuBinding

class MenuFragment : Fragment() {

    private var _binding: FragmentMenuBinding? = null
    private val binding: FragmentMenuBinding
        get() = _binding!!

    private lateinit var mainViewModel: MainViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainViewModel = activity?.let { activity ->
            ViewModelProvider(activity)[MainViewModel::class.java]
        } ?: throw NullPointerException("activity can't be null")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.playButton.setOnClickListener {
            mainViewModel.onPlayClick()
        }
        binding.progressButton.setOnClickListener {
            mainViewModel.onProgressClick()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}

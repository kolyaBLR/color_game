package com.example.game.game.fragmens

import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.game.databinding.FragmentBottomBinding
import com.example.game.exceptions.SquareDragDataCastException
import com.example.game.game.ColorFieldViewModel
import com.example.game.game.SquaresViewModel
import com.example.game.game.item.SquareDragData
import java.lang.NullPointerException

class BottomFragment : Fragment() {

    private var _binding: FragmentBottomBinding? = null
    private val binding: FragmentBottomBinding
        get() = _binding!!

    private lateinit var gameViewModel: SquaresViewModel
    private lateinit var colorFieldViewModel: ColorFieldViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parentFragment?.let {
            gameViewModel = ViewModelProvider(it)[SquaresViewModel::class.java]
        } ?: throw NullPointerException("see to GameFragment")
        colorFieldViewModel = ViewModelProvider(this)[ColorFieldViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBottomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.root.setOnDragListener { _, dragEvent ->
            try {
                val state =
                    dragEvent.localState as? SquareDragData ?: throw SquareDragDataCastException()
                when (dragEvent.action) {
                    DragEvent.ACTION_DRAG_ENTERED -> {
                    }
                    DragEvent.ACTION_DRAG_ENDED -> {
                    }
                    DragEvent.ACTION_DRAG_EXITED -> {
                    }
                    DragEvent.ACTION_DROP -> {
                        gameViewModel.onViewDroppedFromPlayingField(state.position)
                        colorFieldViewModel.onColorDropped(state.squareData)
                    }
                }
                true
            } catch (ex: SquareDragDataCastException) {
                Log.d("BottomFragment", "cast exception")
                gameViewModel.dragAndDropFailed()
                false
            }
        }

        colorFieldViewModel.addColorAction.observe(this, Observer { item ->
            context?.let { context ->
                val color = ContextCompat.getColor(context, item.colorRes)
                binding.root.setBackgroundColor(color)
            }
        })
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}
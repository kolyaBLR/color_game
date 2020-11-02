package com.example.game.game.fragmens

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.core.view.setMargins
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.game.R
import com.example.game.databinding.FragmentBottomBinding
import com.example.game.databinding.ItemSquareBinding
import com.example.game.exceptions.SquareDragDataCastException
import com.example.game.game.ColorFieldViewModel
import com.example.game.game.SquaresViewModel
import com.example.game.game.item.SquareDragData

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
        colorFieldViewModel =
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
                .create(ColorFieldViewModel::class.java)
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

        gameViewModel.nextLevelAction.observe(this, Observer { level ->
            colorFieldViewModel.initLevel(level.second)
        })

        colorFieldViewModel.initViewAction.observe(this, Observer { targetColor ->
            binding.currentColorView.setBackgroundColor(Color.TRANSPARENT)
            binding.targetColorView.setBackgroundColor(targetColor)
            binding.colorsContainer.removeAllViews()
        })

        colorFieldViewModel.levelCompletedAction.observe(this, Observer {
            gameViewModel.onLevelCompleted()
            Toast.makeText(requireContext(), R.string.game_success, Toast.LENGTH_LONG).show()
        })

        colorFieldViewModel.levelFailedAction.observe(this, Observer {
            colorFieldViewModel.initLevel(it)
            Toast.makeText(requireContext(), R.string.game_fail, Toast.LENGTH_LONG).show()
        })

        binding.root.setOnDragListener { view, dragEvent ->
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
                        colorFieldViewModel.onColorDropped(state.squareData.colorRes)
                        gameViewModel.onViewDroppedFromPlayingField(state.position)
                    }
                }
                true
            } catch (ex: SquareDragDataCastException) {
                Log.d("BottomFragment", "cast exception")
                gameViewModel.dragAndDropFailed()
                false
            }
        }

        colorFieldViewModel.colorsChangedAction.observe(this, Observer { pair ->
            val animator = ValueAnimator.ofObject(ArgbEvaluator(), pair.first, pair.second)
            animator.addUpdateListener {
                val color = it.animatedValue as Int
                binding.currentColorView.setBackgroundColor(color)
            }

            val view = createSquareView(colorFieldViewModel.lastColor)
            binding.colorsContainer.addView(view)
            view.alpha = 0f

            view.animate().alpha(1f).setDuration(300).start()

            animator.duration = 300
            animator.addListener(AnimatorListener())
            animator.start()
        })
    }

    private fun createSquareView(@ColorInt color: Int): View {
        val context = requireContext()
        val binding = ItemSquareBinding.inflate(LayoutInflater.from(context))
        val size = context.resources.getDimensionPixelSize(R.dimen.color_preview_item_height)
        val margin = context.resources.getDimensionPixelSize(R.dimen.default_margin)
        val layoutParams = ViewGroup.MarginLayoutParams(size, size)
        layoutParams.setMargins(margin)
        binding.root.layoutParams = layoutParams
        binding.colorItem.setBackgroundColor(color)

        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    inner class AnimatorListener : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator?) {
            super.onAnimationEnd(animation)
            colorFieldViewModel.onColorChangeAnimationEnded()
        }
    }
}
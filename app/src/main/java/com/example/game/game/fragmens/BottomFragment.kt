package com.example.game.game.fragmens

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.core.graphics.component1
import androidx.core.graphics.component2
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
import com.example.game.views.SelectedSquareView
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

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

    fun calculate(p1: Float, p2: Float): Float {
        return abs(p1 - p2).pow(2)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.root.setOnTouchListener { view, event ->
            if (event.action == MotionEvent.ACTION_MOVE && event.pointerCount > 1 && event.historySize > 0) {
                val startRange = sqrt(
                    calculate(event.getHistoricalX(0, 0), event.getHistoricalX(1, 0)) +
                            calculate(event.getHistoricalY(0, 0), event.getHistoricalY(1, 0))
                )
                val currentRange = sqrt(
                    calculate(event.getX(0), event.getX(1)) +
                            calculate(event.getY(0), event.getY(1))
                )
                val viewSize = sqrt(
                    (view.width * view.width).toFloat() +
                            (view.height * view.height)
                )
                val scale = (currentRange - startRange) / viewSize
                view.scaleY += scale
                view.scaleX += scale
            } else if (
                event.action == MotionEvent.ACTION_CANCEL ||
                event.action == MotionEvent.ACTION_UP
            ) {
                view.runScaleAnimation(1f)
            }
            return@setOnTouchListener true
        }

        gameViewModel.nextLevelAction.observe(this, Observer { level ->
            colorFieldViewModel.initLevel(level.second)
        })

        colorFieldViewModel.initTargetColorAction.observe(this, Observer { targetColor ->
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
            binding.colorsContainer.addView(view, 0)
            view.alpha = 0f

            view.animate().alpha(1f).setDuration(300).start()

            animator.duration = 300
            animator.addListener(AnimatorListener())
            animator.start()
        })
    }

    private fun View.runScaleAnimation(to: Float) {
        this.animate()
            .scaleX(to)
            .scaleY(to)
            .setDuration(300)
            .start()
    }

    private fun createSquareView(@ColorInt color: Int): View {
        val context = requireContext()
        val view = SelectedSquareView(context)
        val margin = context.resources.getDimensionPixelSize(R.dimen.default_margin)
        val layoutParams = LinearLayout.LayoutParams(0, 0)
        layoutParams.weight = 1f
        layoutParams.setMargins(margin)
        view.layoutParams = layoutParams
        view.setBackgroundColor(color)

        return view
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
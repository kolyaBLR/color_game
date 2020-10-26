package com.example.game.game.fragmens

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
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
                        gameViewModel.onViewDroppedFromPlayingField(state.position)
                        val color = ContextCompat.getColor(view.context, state.squareData.colorRes)
                        colorFieldViewModel.onColorDropped(color)
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
                binding.colorsContainer.setBackgroundColor(color)
            }

            val view = createSquareView(colorFieldViewModel.nextColor)
            binding.previewColorsContainer.addView(view)
            view.alpha = 0f

            view.animate().alpha(1f).setDuration(300).start()
            animator.setDuration(300).start()
        })
    }

    private fun createSquareView(@ColorInt color: Int): View {
        val context = context!!
        val binding = ItemSquareBinding.inflate(LayoutInflater.from(context))
        val size = context.resources.getDimensionPixelSize(R.dimen.color_preview_item_height)
        val layoutParams = ViewGroup.LayoutParams(size, size)
        binding.root.layoutParams = layoutParams

        binding.colorItem.setBackgroundColor(color)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}
package com.example.game.game.item

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.content.ClipData
import android.view.DragEvent
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import com.example.game.ContextViewHolder
import com.example.game.databinding.ItemSquareBinding
import com.example.game.game.SquaresViewModel
import com.example.game.game.util.Configurations

@SuppressLint("ClickableViewAccessibility")
class SquareViewHolder(
    private val binding: ItemSquareBinding, private val viewModel: SquaresViewModel
) : ContextViewHolder(binding.root) {

    init {
        binding.root.setOnTouchListener { _, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN && adapterPosition != -1) {
                viewModel.onItemTouched(adapterPosition)
            }
            return@setOnTouchListener false
        }

        val activity = context as AppCompatActivity

        viewModel.viewDroppedAction.observe(activity, Observer { position ->
            if (adapterPosition == position) {
                binding.root.visibility = View.VISIBLE
                bind()
            }
        })

        viewModel.dragAndDropFailedAction.observe(activity, Observer {
            binding.root.visibility = View.VISIBLE
        })
    }

    fun bind() {
        if (adapterPosition != -1) {
            viewModel.getItem(adapterPosition)?.let { value -> bind(value) }
        }
    }

    private fun bind(item: SquareData) {
        val color = ContextCompat.getColor(context, item.colorRes)
        binding.colorItem.setBackgroundColor(color)
    }

    fun startDragAndDrop() {
        if (adapterPosition != -1) {
            val item = viewModel.getItem(adapterPosition)
            val view = binding.root
            if (item != null) {
                val state = SquareDragData(adapterPosition, item)
                val success = ViewCompat.startDragAndDrop(
                    view,
                    ClipData.newPlainText(adapterPosition.toString(), ""),
                    View.DragShadowBuilder(view),
                    state,
                    View.DRAG_FLAG_OPAQUE
                )
                if (success) {
                    view.visibility = View.INVISIBLE
                }
            }
        }
    }

    fun getBackAfterDragAndDrop(dragEvent: DragEvent) {
        val view = binding.root
        view.translationX = dragEvent.x - view.x - view.width.div(2)
        view.translationY = dragEvent.y - view.y - view.height.div(2)
        view.elevation = 1f
        view.visibility = View.VISIBLE
        view.animate().translationX(0f).translationY(0f)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    view.elevation = 0f
                }
            })
    }
}

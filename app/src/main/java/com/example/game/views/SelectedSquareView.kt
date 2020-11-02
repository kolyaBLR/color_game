package com.example.game.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

class SelectedSquareView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val parentWidth = (parent as? ViewGroup)?.width ?: 0
        //measuredHeight > parentWidth && parentWidth > 0
        val size = if (parentWidth in 1 until measuredHeight) {
            parentWidth
        } else {
            measuredHeight
        }
        setMeasuredDimension(size, size)
    }
}

package com.example.game

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView

open class ContextViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val context: Context = view.context
}
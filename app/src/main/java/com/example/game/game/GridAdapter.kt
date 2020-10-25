package com.example.game.game

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.game.databinding.ItemSquareBinding
import com.example.game.game.item.SquareViewHolder
import com.example.game.game.util.Configurations

class GridAdapter(private val viewModel: SquaresViewModel) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemSquareBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val size = Configurations.getItemSize(parent, viewModel.size)
        binding.root.layoutParams.width = size
        binding.root.layoutParams.height = size
        return SquareViewHolder(binding, viewModel)
    }

    override fun getItemCount() = viewModel.size

    override fun onBindViewHolder(h: RecyclerView.ViewHolder, position: Int) {
        (h as? SquareViewHolder)?.bind()
    }
}

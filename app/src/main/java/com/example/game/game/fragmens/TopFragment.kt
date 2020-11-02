package com.example.game.game.fragmens

import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.game.databinding.FragmentMainBinding
import com.example.game.exceptions.SquareDragDataCastException
import com.example.game.game.SquaresViewModel
import com.example.game.game.GridAdapter
import com.example.game.game.item.SquareDragData
import com.example.game.game.item.SquareViewHolder
import com.example.game.game.util.Configurations
import java.lang.NullPointerException

class TopFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding
        get() = _binding!!

    private lateinit var gameViewModel: SquaresViewModel

    private lateinit var adapter: GridAdapter
    private lateinit var layoutManager: GridLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parentFragment?.let {
            gameViewModel = ViewModelProvider(it)[SquaresViewModel::class.java]
        } ?: throw NullPointerException("see to GameFragment")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val window = (container?.context as? AppCompatActivity)?.window
            ?: throw  NullPointerException("window can't be null")
        binding.root.layoutParams.height = Configurations.getPlayingFieldSize(window)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        gameViewModel.nextLevelAction.observe(this, Observer {
            layoutManager = createLayoutManager()
            binding.mainRecyclerView.layoutManager = layoutManager
            adapter = GridAdapter(gameViewModel)
            binding.mainRecyclerView.adapter = adapter

        })

        gameViewModel.touchAction.observe(this, Observer { position ->
            findSquareHolder(position)?.startDragAndDrop()
        })

        gameViewModel.viewDroppedAction.observe(this, Observer { position ->
            adapter.notifyItemChanged(position)
        })

        binding.root.setOnDragListener { view, dragEvent ->
            try {
                val state =
                    dragEvent.localState as? SquareDragData ?: throw SquareDragDataCastException()
                if (dragEvent.action == DragEvent.ACTION_DROP) {
                    findSquareHolder(state.position)?.getBackAfterDragAndDrop(dragEvent)
                }
            } catch (ex: SquareDragDataCastException) {
                Log.d("TopFragment", "cast exception")
                gameViewModel.dragAndDropFailed()
            }
            true
        }
    }

    private fun createLayoutManager(): GridLayoutManager {
        return GridLayoutManager(
            context,
            Configurations.getSpanCount(gameViewModel.size),
            GridLayoutManager.VERTICAL,
            false
        )
    }

    private fun findSquareHolder(position: Int): SquareViewHolder? {
        return binding.mainRecyclerView.findViewHolderForAdapterPosition(position) as? SquareViewHolder
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}

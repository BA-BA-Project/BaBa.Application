package kids.baba.mobile.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kids.baba.mobile.databinding.ItemAllViewBabyBinding
import kids.baba.mobile.presentation.model.GatheringAlbumUiModel

class AllViewAdapter : ListAdapter<GatheringAlbumUiModel, AllViewAdapter.AllViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllViewHolder {
        val binding = ItemAllViewBabyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AllViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AllViewHolder, position: Int) {
        val content = getItem(position)
        holder.bind(content)
    }

    class AllViewHolder(private val binding: ItemAllViewBabyBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(baby: GatheringAlbumUiModel) {
            binding.baby = baby
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<GatheringAlbumUiModel>() {
            override fun areItemsTheSame(oldItem: GatheringAlbumUiModel, newItem: GatheringAlbumUiModel) =
                oldItem == newItem


            override fun areContentsTheSame(oldItem: GatheringAlbumUiModel, newItem: GatheringAlbumUiModel) =
                oldItem == newItem


        }
    }


}
package kids.baba.mobile.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kids.baba.mobile.databinding.ItemMonthViewBabyBinding
import kids.baba.mobile.presentation.model.AlbumUiModel
import kids.baba.mobile.presentation.model.BabyUiModel
import kids.baba.mobile.presentation.model.GatheringAllAlbumUiModel


class MonthViewAdapter : ListAdapter<GatheringAllAlbumUiModel, MonthViewAdapter.MonthViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthViewHolder {
        val binding = ItemMonthViewBabyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MonthViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MonthViewHolder, position: Int) {
        val content = getItem(position)
        holder.bind(content)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    inner class MonthViewHolder(private val binding: ItemMonthViewBabyBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(baby: GatheringAllAlbumUiModel) {
            Log.d("MonthViewAdapter", "bind called")
            binding.baby = baby

        }
    }


    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<GatheringAllAlbumUiModel>() {
            override fun areItemsTheSame(oldItem: GatheringAllAlbumUiModel, newItem: GatheringAllAlbumUiModel) =
                oldItem.contentId == newItem.contentId

            override fun areContentsTheSame(oldItem: GatheringAllAlbumUiModel, newItem: GatheringAllAlbumUiModel) =
                oldItem == newItem

        }
    }


}
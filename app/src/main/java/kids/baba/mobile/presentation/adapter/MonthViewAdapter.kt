package kids.baba.mobile.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kids.baba.mobile.databinding.ItemMonthViewBabyBinding
import kids.baba.mobile.presentation.model.GatheringAlbumCountUiModel


class MonthViewAdapter : ListAdapter<GatheringAlbumCountUiModel, MonthViewAdapter.YearViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YearViewHolder {
        val binding = ItemMonthViewBabyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return YearViewHolder(binding)
    }

    override fun onBindViewHolder(holder: YearViewHolder, position: Int) {
        val content = getItem(position)
        holder.bind(content)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    class YearViewHolder(private val binding: ItemMonthViewBabyBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(baby: GatheringAlbumCountUiModel) {
            binding.baby = baby

        }
    }


    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<GatheringAlbumCountUiModel>() {
            override fun areItemsTheSame(oldItem: GatheringAlbumCountUiModel, newItem: GatheringAlbumCountUiModel) =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: GatheringAlbumCountUiModel, newItem: GatheringAlbumCountUiModel) =
                oldItem == newItem

        }
    }


}
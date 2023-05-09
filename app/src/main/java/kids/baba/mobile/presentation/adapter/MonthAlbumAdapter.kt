package kids.baba.mobile.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kids.baba.mobile.databinding.ItemMonthAlbumBinding
import kids.baba.mobile.presentation.model.GatheringAlbumCountUiModel
import kids.baba.mobile.presentation.model.RepresentativeAlbumUiModel
import java.time.format.DateTimeFormatter


class MonthAlbumAdapter(private val albumClick: (GatheringAlbumCountUiModel) -> Unit) :
    ListAdapter<GatheringAlbumCountUiModel, MonthAlbumAdapter.YearViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YearViewHolder {
        val binding = ItemMonthAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM" + "월")
        return YearViewHolder(binding, albumClick)
    }

    override fun onBindViewHolder(holder: YearViewHolder, position: Int) {
        val content = getItem(position)
        holder.bind(content)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    class YearViewHolder(private val binding: ItemMonthAlbumBinding, albumClick: (GatheringAlbumCountUiModel) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        private lateinit var baby: GatheringAlbumCountUiModel

        init {
            binding.ivMonthBaby.setOnClickListener{
                albumClick(baby)
            }
        }

        fun bind(baby: GatheringAlbumCountUiModel) {
            this.baby = baby
            binding.baby = baby
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<GatheringAlbumCountUiModel>() {
            override fun areItemsTheSame(oldItem: GatheringAlbumCountUiModel, newItem: GatheringAlbumCountUiModel) =
                oldItem.representativeAlbumUiModel == newItem.representativeAlbumUiModel

            override fun areContentsTheSame(oldItem: GatheringAlbumCountUiModel, newItem: GatheringAlbumCountUiModel) =
                oldItem == newItem
        }
    }
}
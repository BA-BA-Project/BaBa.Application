package kids.baba.mobile.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kids.baba.mobile.databinding.ItemYearAlbumBinding
import kids.baba.mobile.presentation.model.GatheringAlbumCountUiModel
import java.time.format.DateTimeFormatter

class YearAlbumAdapter(
    private val albumClick: (Int) -> Unit
) : ListAdapter<GatheringAlbumCountUiModel, YearAlbumAdapter.YearViewHolder>(diffUtil) {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YearViewHolder {
        val binding = ItemYearAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy" + "년")
        return YearViewHolder(binding, albumClick)
    }

    override fun onBindViewHolder(holder: YearViewHolder, position: Int) {
        val content = getItem(position)
        holder.bind(content)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    class YearViewHolder(
        private val binding: ItemYearAlbumBinding,
        albumClick: (Int) -> Unit
    ) : RecyclerView.ViewHolder(
        binding.root
    ) {

        init {
            binding.ivMonthBaby.setOnClickListener { albumClick(itemId.toInt()) }
        }

        fun bind(albumAndCount: GatheringAlbumCountUiModel) {
            binding.albumAndCount = albumAndCount
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
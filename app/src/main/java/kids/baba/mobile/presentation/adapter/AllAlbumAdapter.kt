package kids.baba.mobile.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kids.baba.mobile.databinding.ItemAllAlbumBinding
import kids.baba.mobile.presentation.model.AlbumUiModel
import java.time.format.DateTimeFormatter

class AllAlbumAdapter : ListAdapter<AlbumUiModel, AllAlbumAdapter.AllViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllViewHolder {
        val binding = ItemAllAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.dateTimeFormatter = DateTimeFormatter.ofPattern("yy-MM-dd")
        return AllViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AllViewHolder, position: Int) {
        val content = getItem(position)
        holder.bind(content)
    }

    class AllViewHolder(private val binding: ItemAllAlbumBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(baby: AlbumUiModel) {
            binding.baby = baby
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<AlbumUiModel>() {
            override fun areItemsTheSame(oldItem: AlbumUiModel, newItem: AlbumUiModel) =
                oldItem.contentId == newItem.contentId

            override fun areContentsTheSame(oldItem: AlbumUiModel, newItem: AlbumUiModel) =
                oldItem == newItem
        }
    }
}
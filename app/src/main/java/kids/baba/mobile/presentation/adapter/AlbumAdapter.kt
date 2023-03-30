package kids.baba.mobile.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kids.baba.mobile.databinding.ItemAlbumBinding
import kids.baba.mobile.presentation.model.AlbumUiModel

class AlbumAdapter : ListAdapter<AlbumUiModel, AlbumAdapter.AlbumViewHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val view = ItemAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlbumViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class AlbumViewHolder(private val binding: ItemAlbumBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AlbumUiModel) {
            binding.like = item.like
            binding.photo = item.photo
        }
    }
    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<AlbumUiModel>() {
            override fun areItemsTheSame(oldItem: AlbumUiModel, newItem: AlbumUiModel) =
                oldItem.contentId == newItem.contentId

            override fun areContentsTheSame(oldItem: AlbumUiModel, newItem: AlbumUiModel): Boolean =
                oldItem.hashCode() == newItem.hashCode()
        }
    }
}
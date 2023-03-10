package kids.baba.mobile.presentation.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kids.baba.mobile.databinding.ItemAlbumBinding
import kids.baba.mobile.domain.model.Album

class AlbumAdapter : ListAdapter<Album, AlbumAdapter.AlbumViewHolder>(diffUtil) {
    val list = mutableListOf<Album>()

    class AlbumViewHolder(private val binding: ItemAlbumBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Album) {
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val view = ItemAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlbumViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun setItem(item: Album) {
        list.add(item)
        notifyDataSetChanged()
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Album>() {
            override fun areItemsTheSame(oldItem: Album, newItem: Album) =
                oldItem.contentId == newItem.contentId

            override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean =
                oldItem.hashCode() == newItem.hashCode()
        }
    }
}
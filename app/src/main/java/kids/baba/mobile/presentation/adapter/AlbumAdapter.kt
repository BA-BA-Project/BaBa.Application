package kids.baba.mobile.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kids.baba.mobile.databinding.ItemAlbumBinding
import kids.baba.mobile.presentation.model.AlbumUiModel

class AlbumAdapter(private val likeClick : (AlbumUiModel) -> Unit) : ListAdapter<AlbumUiModel, AlbumAdapter.AlbumViewHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val view = ItemAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlbumViewHolder(view, likeClick)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class AlbumViewHolder(private val binding: ItemAlbumBinding, likeClick: (AlbumUiModel) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        private lateinit var album : AlbumUiModel
        init {
            binding.cbAlbumLike.setOnClickListener {
                likeClick(album)
            }
        }
        fun bind(album: AlbumUiModel) {
            this.album = album
            binding.like = album.like
            binding.photo = album.photo
            //Todo test용 날짜지우기
            binding.date = album.date.toString()
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
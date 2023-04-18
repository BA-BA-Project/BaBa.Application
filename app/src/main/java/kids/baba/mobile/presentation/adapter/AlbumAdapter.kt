package kids.baba.mobile.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kids.baba.mobile.databinding.ItemAlbumBinding
import kids.baba.mobile.presentation.model.AlbumUiModel
import java.time.LocalDate

class AlbumAdapter(private val likeClick : (AlbumUiModel) -> Unit, private val createAlbum : () -> Unit) : ListAdapter<AlbumUiModel, AlbumAdapter.AlbumViewHolder>(diffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val view = ItemAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlbumViewHolder(view, likeClick, createAlbum)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class AlbumViewHolder(private val binding: ItemAlbumBinding, likeClick: (AlbumUiModel) -> Unit, createAlbum : () -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        private lateinit var album : AlbumUiModel
        init {
            binding.btnAlbumLike.setOnClickListener {
                likeClick(album)
            }
            binding.btnCreateAlbum.setOnClickListener {
                createAlbum()
            }
        }
        fun bind(album: AlbumUiModel) {
            this.album = album
            binding.like = album.like
            binding.photo = album.photo

            if(album.contentId == null){
                binding.btnAlbumLike.visibility = View.GONE
                binding.btnCreateAlbum.visibility = View.VISIBLE

            } else {
                binding.btnAlbumLike.visibility = View.VISIBLE
                binding.btnCreateAlbum.visibility = View.GONE
            }

            if(album.contentId == null && album.date == LocalDate.now()){
                binding.ivAlbum.visibility = View.INVISIBLE
                binding.lavTodayAlbum.visibility = View.VISIBLE
            } else {
                binding.ivAlbum.visibility = View.VISIBLE
                binding.lavTodayAlbum.visibility = View.INVISIBLE
            }

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
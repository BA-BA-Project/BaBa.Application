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
            val isMyBaby = album.isMyBaby
            binding.like = album.like
            binding.photo = album.photo


            binding.btnAlbumLike.visibility = if(album.contentId != null){View.VISIBLE} else {View.GONE}
            binding.btnCreateAlbum.visibility = if(album.contentId == null && isMyBaby){View.VISIBLE} else {View.GONE}

            if(album.date == LocalDate.now() && album.contentId == null){
                binding.ivAlbum.visibility = View.INVISIBLE
                binding.lavTodayAlbum.visibility = View.VISIBLE
                binding.lavTodayAlbum.playAnimation()
            } else {
                binding.ivAlbum.visibility = View.VISIBLE
                binding.lavTodayAlbum.visibility = View.INVISIBLE
                binding.lavTodayAlbum.pauseAnimation()
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
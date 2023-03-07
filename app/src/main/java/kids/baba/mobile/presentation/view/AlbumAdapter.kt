package kids.baba.mobile.presentation.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kids.baba.mobile.databinding.ItemAlbumBinding
import kids.baba.mobile.domain.model.Album

class AlbumAdapter : RecyclerView.Adapter<AlbumAdapter.ViewHolder>() {
    val list = mutableListOf<Album>()

    inner class ViewHolder(private val binding: ItemAlbumBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Album) {
            binding.test.text = item.id.toString()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun setItem(item: Album) {
        list.add(item)
        notifyDataSetChanged()
    }
}
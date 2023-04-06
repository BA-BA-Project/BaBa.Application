package kids.baba.mobile.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kids.baba.mobile.R
import kids.baba.mobile.databinding.ItemAlbumCommentBinding
import kids.baba.mobile.presentation.model.CommentUiModel

class AlbumDetailCommentAdapter
    : ListAdapter<CommentUiModel, AlbumDetailCommentAdapter.CommentViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CommentViewHolder(parent)

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class CommentViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_album_comment, parent, false)
    ) {
        private val binding = ItemAlbumCommentBinding.bind(itemView)

        fun bind(comment: CommentUiModel){
            binding.comment = comment
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun submitList(list: List<CommentUiModel>?) {
        super.submitList(list)
        notifyDataSetChanged()
    }
    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<CommentUiModel>() {
            override fun areItemsTheSame(oldItem: CommentUiModel, newItem: CommentUiModel) =
                oldItem.commentId == newItem.commentId

            override fun areContentsTheSame(
                oldItem: CommentUiModel,
                newItem: CommentUiModel
            ): Boolean =
                oldItem.hashCode() == newItem.hashCode()
        }
    }
}
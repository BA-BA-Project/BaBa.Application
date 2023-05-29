package kids.baba.mobile.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kids.baba.mobile.R
import kids.baba.mobile.databinding.ItemLikeUserPreviewBinding
import kids.baba.mobile.presentation.model.UserIconUiModel

class LikeUsersAdapter() :
    ListAdapter<UserIconUiModel, LikeUsersAdapter.LikeUserViewHolder>(diffUtil) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        LikeUserViewHolder(parent)

    override fun onBindViewHolder(holder: LikeUserViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    class LikeUserViewHolder(
        parent: ViewGroup,
    ) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_like_user_preview, parent, false)
    ) {
        private val binding = ItemLikeUserPreviewBinding.bind(itemView)

        fun bind(item: UserIconUiModel) {
            binding.likeUser = item
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<UserIconUiModel>() {
            override fun areItemsTheSame(oldItem: UserIconUiModel, newItem: UserIconUiModel) =
                oldItem.userProfileIconUiModel == newItem.userProfileIconUiModel

            override fun areContentsTheSame(oldItem: UserIconUiModel, newItem: UserIconUiModel) =
                oldItem.hashCode() == newItem.hashCode()
        }
    }
}
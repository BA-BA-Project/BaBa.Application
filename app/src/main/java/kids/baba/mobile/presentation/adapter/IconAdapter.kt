package kids.baba.mobile.presentation.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kids.baba.mobile.databinding.ItemIconBinding
import kids.baba.mobile.presentation.model.UserIconUiModel

class IconAdapter(private val itemClick: (UserIconUiModel) -> Unit) :
    ListAdapter<UserIconUiModel, IconAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(
        private val binding: ItemIconBinding,
        private val itemClick: (UserIconUiModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(icon: UserIconUiModel, position: Int) {
            val backgroundColor = if (position == selectedItemPosition) {
                Color.parseColor("#FF3481FF")
            } else {
                Color.parseColor("#EDEDED")
            }
            binding.icon.circleBackgroundColor = backgroundColor
            binding.icon.setImageResource(icon.userProfileIconUiModel.iconRes)
            binding.root.setOnClickListener {
                selectedItemPosition = position
                itemClick(icon)
                notifyDataSetChanged()
            }
        }
    }

    private var selectedItemPosition = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemIconBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view, itemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<UserIconUiModel>() {
            override fun areItemsTheSame(
                oldItem: UserIconUiModel,
                newItem: UserIconUiModel
            ): Boolean = newItem == oldItem

            override fun areContentsTheSame(
                oldItem: UserIconUiModel,
                newItem: UserIconUiModel
            ): Boolean = newItem == oldItem
        }
    }
}

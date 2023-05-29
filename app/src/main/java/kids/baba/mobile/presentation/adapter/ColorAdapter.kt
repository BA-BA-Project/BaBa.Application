package kids.baba.mobile.presentation.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kids.baba.mobile.databinding.ItemColorBinding
import kids.baba.mobile.presentation.model.ColorUiModel

class ColorAdapter(private val itemClick: (ColorUiModel) -> Unit) :
    ListAdapter<ColorUiModel, ColorAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(
        private val binding: ItemColorBinding,
        private val itemClick: (ColorUiModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(color: ColorUiModel, position: Int) {
            if (position == selectedItemPosition) {
                binding.civBlue.circleBackgroundColor = Color.parseColor("#FF3481FF")
                binding.civColor.circleBackgroundColor = Color.parseColor(color.value)
            } else {
                binding.civBlue.circleBackgroundColor = Color.parseColor(color.value)
                binding.civColor.circleBackgroundColor = Color.parseColor(color.value)
            }
            binding.root.setOnClickListener {
                selectedItemPosition = position
                itemClick(color)
                notifyDataSetChanged()
            }
        }
    }

    private var selectedItemPosition = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemColorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view, itemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }


    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ColorUiModel>() {
            override fun areItemsTheSame(
                oldItem: ColorUiModel,
                newItem: ColorUiModel
            ): Boolean = newItem == oldItem

            override fun areContentsTheSame(
                oldItem: ColorUiModel,
                newItem: ColorUiModel
            ): Boolean = newItem == oldItem
        }
    }
}

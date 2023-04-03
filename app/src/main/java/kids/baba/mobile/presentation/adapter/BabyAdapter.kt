package kids.baba.mobile.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kids.baba.mobile.databinding.ItemBabyBinding
import kids.baba.mobile.presentation.model.BabyUiModel

class BabyAdapter : ListAdapter<BabyUiModel, BabyAdapter.BabyViewHolder>(diffUtil) {
    class BabyViewHolder(private val binding: ItemBabyBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BabyUiModel) {
            binding.baby = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BabyViewHolder {
        val view = ItemBabyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BabyViewHolder(view)
    }

    override fun onBindViewHolder(holder: BabyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<BabyUiModel>() {
            override fun areItemsTheSame(oldItem: BabyUiModel, newItem: BabyUiModel) =
                oldItem.babyId == newItem.babyId

            override fun areContentsTheSame(oldItem: BabyUiModel, newItem: BabyUiModel): Boolean =
                oldItem.hashCode() == newItem.hashCode()
        }
    }
}
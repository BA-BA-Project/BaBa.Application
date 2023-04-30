package kids.baba.mobile.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kids.baba.mobile.databinding.ItemBabyBinding
import kids.baba.mobile.presentation.model.BabyUiModel

class BabyAdapter(private val itemClick: (BabyUiModel) -> Unit) : ListAdapter<BabyUiModel, BabyAdapter.BabyViewHolder>(diffUtil) {
    class BabyViewHolder(private val binding: ItemBabyBinding, itemClick: (BabyUiModel) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {

        lateinit var baby : BabyUiModel
        init {
            itemView.setOnClickListener {
                itemClick(baby)
            }
        }
        fun bind(baby: BabyUiModel) {
            binding.baby = baby
            this.baby = baby
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BabyViewHolder {
        val view = ItemBabyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BabyViewHolder(view, itemClick)
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
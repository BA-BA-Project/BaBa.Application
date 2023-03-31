package kids.baba.mobile.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kids.baba.mobile.databinding.ItemCardsStyleBinding
import kids.baba.mobile.presentation.model.CardStyleUiModel

class CardStyleAdapter(private val listener: OnItemClickListener) :
    ListAdapter<CardStyleUiModel, CardStyleAdapter.CardViewHolder>(diffUtil) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = ItemCardsStyleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
//        holder.bind(position)
    }


    inner class CardViewHolder(private val binding: ItemCardsStyleBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val card = getItem(position)
                    binding.ivItemCard.isActivated = true
                    listener.onItemClick(card, position)
                }
            }
        }

        fun bind(card: CardStyleUiModel) {
            binding.card = card
        }

//        fun bind(position: Int) {
//            val bindingItem = getItem(position)
//            binding.ivItemCard.isActivated = bindingItem.isSelected
//        }

    }


    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<CardStyleUiModel>() {
            override fun areItemsTheSame(oldItem: CardStyleUiModel, newItem: CardStyleUiModel) =
                oldItem.cardStyleIcon == newItem.cardStyleIcon

            override fun areContentsTheSame(oldItem: CardStyleUiModel, newItem: CardStyleUiModel): Boolean =
                oldItem == newItem
        }
    }

    interface OnItemClickListener {
        fun onItemClick(card: CardStyleUiModel, position: Int)
    }


}
package kids.baba.mobile.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
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
    }


    inner class CardViewHolder(private val binding: ItemCardsStyleBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        init {
//            val initPosition = 0
//            if (initPosition != RecyclerView.NO_POSITION) {
//                val card = getItem(initPosition)
//                listener.initItem(card, initPosition)
//            }

            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val card = getItem(position)
                    listener.onItemClick(card, position)
                }
            }
        }

        fun bind(card: CardStyleUiModel) {
            binding.card = card

        }

    }


    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<CardStyleUiModel>() {
            override fun areItemsTheSame(oldItem: CardStyleUiModel, newItem: CardStyleUiModel) =
                oldItem.cardStyleName == newItem.cardStyleName

            override fun areContentsTheSame(oldItem: CardStyleUiModel, newItem: CardStyleUiModel): Boolean =
//                oldItem.hashCode() == newItem.hashCode()
                oldItem == newItem
        }
    }

    interface OnItemClickListener {
        fun onItemClick(card: CardStyleUiModel, position: Int)
    }


}
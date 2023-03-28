package kids.baba.mobile.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kids.baba.mobile.R
import kids.baba.mobile.databinding.ItemCardsStyleBinding
import kids.baba.mobile.presentation.model.CardStyleUiModel

class CardStyleAdapter : ListAdapter<CardStyleUiModel, CardStyleAdapter.CardViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        return CardViewHolder(parent)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        return holder.bind(getItem(position))
    }


    class CardViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_cards_style, parent, false)
    ) {
        private val binding = ItemCardsStyleBinding.bind(itemView)

        fun bind(card: CardStyleUiModel) {
            binding.card = card
        }

    }


    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<CardStyleUiModel>() {
            override fun areItemsTheSame(oldItem: CardStyleUiModel, newItem: CardStyleUiModel) =
                oldItem.cardStyleName == newItem.cardStyleName

            override fun areContentsTheSame(oldItem: CardStyleUiModel, newItem: CardStyleUiModel): Boolean =
                oldItem.hashCode() == newItem.hashCode()

        }
    }


}
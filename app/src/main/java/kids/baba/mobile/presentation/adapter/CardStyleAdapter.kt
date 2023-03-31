package kids.baba.mobile.presentation.adapter

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kids.baba.mobile.databinding.ItemCardsStyleBinding
import kids.baba.mobile.presentation.model.CardStyleUiModel

class CardStyleAdapter(private val listener: OnItemClickListener) :
    ListAdapter<CardStyleUiModel, CardStyleAdapter.CardViewHolder>(diffUtil) {

    init {
        setHasStableIds(true)
    }

    private lateinit var selectionTracker: SelectionTracker<Long>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = ItemCardsStyleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
//        val currentItem = getItem(position)
//        holder.bind(currentItem)
//        val content = currentList[position]
        val content = getItem(position)
        holder.bind(content, position)

    }

    fun setSelectionTracker(selectionTracker: SelectionTracker<Long>) {
        this.selectionTracker = selectionTracker
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    inner class CardViewHolder(private val binding: ItemCardsStyleBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val card = getItem(position)
                    listener.onItemClick(card, position)
                }
            }
        }

        fun bind(card: CardStyleUiModel, itemPosition: Int) {
            binding.card = card
            binding.ivItemCard.setOnClickListener {
                selectionTracker.select(itemPosition.toLong())
            }
            binding.ivItemCard.isActivated = selectionTracker.isSelected(itemPosition.toLong())

        }

//        fun bind(position: Int) {
//            val bindingItem = getItem(position)
//            binding.ivItemCard.isActivated = bindingItem.isSelected
//        }

        fun getItemDetails(viewHolder: RecyclerView.ViewHolder?): ItemDetailsLookup.ItemDetails<Long> {
            return object : ItemDetailsLookup.ItemDetails<Long>() {
                override fun getPosition(): Int {
                    if (viewHolder == null) {
                        return RecyclerView.NO_POSITION
                    }
                    return viewHolder.adapterPosition
                }

                override fun getSelectionKey(): Long? {
                    return itemId
                }

            }
        }

    }


    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<CardStyleUiModel>() {
            override fun areItemsTheSame(oldItem: CardStyleUiModel, newItem: CardStyleUiModel) =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: CardStyleUiModel, newItem: CardStyleUiModel): Boolean =
                oldItem == newItem
        }
    }

    interface OnItemClickListener {
        fun onItemClick(card: CardStyleUiModel, position: Int)
    }

    class CardDetailsLookup(private val recyclerView: RecyclerView) : ItemDetailsLookup<Long>() {
        override fun getItemDetails(e: MotionEvent): ItemDetails<Long>? {
            val view = recyclerView.findChildViewUnder(e.x, e.y)
            if (view != null) {
                val viewHolder = recyclerView.getChildViewHolder(view) as CardStyleAdapter.CardViewHolder
                return viewHolder.getItemDetails(viewHolder)
            }
            return null
        }

    }

}
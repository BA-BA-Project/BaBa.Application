package kids.baba.mobile.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kids.baba.mobile.databinding.ItemCardsStyleBinding
import kids.baba.mobile.presentation.model.CardStyleUiModel


class CardStyleAdapter(
    private val listener: OnItemClickListener,
) : ListAdapter<CardStyleUiModel, CardStyleAdapter.CardViewHolder>(diffUtil) {

    private val tag = "CardStyleAdapter"

    init {
        setHasStableIds(true)
    }

    private lateinit var selectionTracker: SelectionTracker<Long>
    private var isFirst = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = ItemCardsStyleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val content = getItem(position)
        holder.bind(content)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    fun setSelectionTracker(selectionTracker: SelectionTracker<Long>) {
        this.selectionTracker = selectionTracker
    }


    inner class CardViewHolder(private val binding: ItemCardsStyleBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {

        init {
            if (isFirst) {
                isFirst = false
                selectionTracker.select(0)
                binding.ivItemCard.isActivated = selectionTracker.isSelected(0)
            }
        }

        fun bind(card: CardStyleUiModel) {

            binding.card = card
            binding.ivItemCard.setOnClickListener {
                selectionTracker.select(itemId)
            }

            selectionTracker.let {
                if (it.isSelected(itemId)) {
                    Log.d(tag, "selectionTracker- itemId : $itemId")
                    listener.onItemClick(itemId.toInt())
                } else {
                    binding.ivItemCard.isActivated = false
                }
            }

            selectionTracker.addObserver(object : SelectionTracker.SelectionObserver<Long>() {
                override fun onSelectionChanged() {
                    super.onSelectionChanged()
                    binding.ivItemCard.isActivated = selectionTracker.isSelected(itemId)
                }
            })
        }

        fun getItemDetails(viewHolder: RecyclerView.ViewHolder?): ItemDetailsLookup.ItemDetails<Long> {
            return object : ItemDetailsLookup.ItemDetails<Long>() {
                override fun getPosition(): Int {
                    if (viewHolder == null) {
                        return RecyclerView.NO_POSITION
                    }
                    return viewHolder.adapterPosition
                }

                override fun getSelectionKey(): Long {
                    return itemId
                }
            }
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<CardStyleUiModel>() {
            override fun areItemsTheSame(oldItem: CardStyleUiModel, newItem: CardStyleUiModel) =
                oldItem.cardStyleName == newItem.cardStyleName

            override fun areContentsTheSame(oldItem: CardStyleUiModel, newItem: CardStyleUiModel): Boolean =
                oldItem == newItem
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }


}
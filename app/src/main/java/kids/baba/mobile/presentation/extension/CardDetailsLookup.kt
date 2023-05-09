package kids.baba.mobile.presentation.extension

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView
import kids.baba.mobile.presentation.adapter.CardStyleAdapter

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
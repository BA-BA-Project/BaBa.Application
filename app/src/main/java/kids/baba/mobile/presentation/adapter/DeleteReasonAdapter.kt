package kids.baba.mobile.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kids.baba.mobile.R
import kids.baba.mobile.databinding.ComposableCheckBinding
import kids.baba.mobile.presentation.model.DeleteReason

class DeleteReasonAdapter(
    private val itemClick: (DeleteReason) -> Unit
) : ListAdapter<DeleteReason, DeleteReasonAdapter.DeleteReasonViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DeleteReasonViewHolder(parent, itemClick)

    override fun onBindViewHolder(holder: DeleteReasonViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DeleteReasonViewHolder(
        parent: ViewGroup,
        itemClick: (DeleteReason) -> Unit
    ) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.composable_check, parent, false)
    ) {
        private val binding = ComposableCheckBinding.bind(itemView)
        private lateinit var deleteReason: DeleteReason
        init {
            binding.root.setOnClickListener {
                itemClick(deleteReason)
            }
        }

        fun bind(deleteReason: DeleteReason) {
            this.deleteReason = deleteReason
            binding.deleteReason = deleteReason
        }
    }


    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<DeleteReason>() {
            override fun areItemsTheSame(oldItem: DeleteReason, newItem: DeleteReason) =
                oldItem.reason == newItem.reason

            override fun areContentsTheSame(oldItem: DeleteReason, newItem: DeleteReason) =
                oldItem.hashCode() == newItem.hashCode()
        }
    }


}
package kids.baba.mobile.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kids.baba.mobile.R
import kids.baba.mobile.databinding.ItemBabaTermsBinding
import kids.baba.mobile.presentation.model.TermsData

class TermsAdapter(
    private val termsClickListener: TermsClickListener
) : ListAdapter<TermsData, TermsAdapter.TermsDataViewHolder>(diffUtil) {

    interface TermsClickListener{
        fun onTermsClickListener(termsData: TermsData, checked: Boolean)
        fun onDetailContentClickListener(url: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        TermsDataViewHolder(termsClickListener, parent)

    override fun onBindViewHolder(holder: TermsDataViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class TermsDataViewHolder(
        termsClickListener: TermsClickListener,
        parent: ViewGroup,
    ) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_baba_terms, parent, false)
    ) {
        private val binding = ItemBabaTermsBinding.bind(itemView)
        private lateinit var termsData: TermsData

        init {
            binding.cbAgree.setOnClickListener {
                termsClickListener.onTermsClickListener(
                    termsData,
                    binding.cbAgree.isChecked
                )
            }
            binding.tvSeeDetailContent.setOnClickListener {
                termsClickListener.onDetailContentClickListener(
                    termsData.detailUrl
                )
            }
        }

        fun bind(item: TermsData) {
            this.termsData = item
            binding.termsItem = item

        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<TermsData>() {
            override fun areItemsTheSame(oldItem: TermsData, newItem: TermsData) =
                oldItem.content == newItem.content

            override fun areContentsTheSame(oldItem: TermsData, newItem: TermsData) =
                oldItem.hashCode() == newItem.hashCode()
        }
    }
}
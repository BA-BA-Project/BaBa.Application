package kids.baba.mobile.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kids.baba.mobile.R
import kids.baba.mobile.databinding.ItemBabyBinding
import kids.baba.mobile.domain.model.Baby

class BabyAdapter : ListAdapter<Baby, BabyAdapter.BabyViewHolder>(diffUtil) {
    val list = mutableListOf<Baby>()

    class BabyViewHolder(private val binding: ItemBabyBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Baby) {
            binding.babyImage.setImageResource(R.drawable.profile_baby_1)
            binding.babyName.text = item.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BabyViewHolder {
        val view = ItemBabyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BabyViewHolder(view)
    }

    override fun onBindViewHolder(holder: BabyViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size


    fun setItem(item: Baby) {
        list.add(item)
        notifyDataSetChanged()
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Baby>() {
            override fun areItemsTheSame(oldItem: Baby, newItem: Baby) =
                oldItem.babyId == newItem.babyId

            override fun areContentsTheSame(oldItem: Baby, newItem: Baby): Boolean =
                oldItem.hashCode() == newItem.hashCode()
        }
    }
}
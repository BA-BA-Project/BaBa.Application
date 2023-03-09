package kids.baba.mobile.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kids.baba.mobile.R
import kids.baba.mobile.databinding.ItemProfileIconBinding
import kids.baba.mobile.presentation.model.ProfileIcon

class ProfileIconAdapter(
    private val iconClickListener: IconClickListener,
) : ListAdapter<ProfileIcon, ProfileIconAdapter.ProfileIconViewHolder>(diffUtil){

    interface IconClickListener{
        fun setIconClickListener(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileIconViewHolder {
        return ProfileIconViewHolder(iconClickListener,parent)
    }

    override fun onBindViewHolder(holder: ProfileIconViewHolder, position: Int) {
        holder.bind(getItem(position)?:return)
    }

    class ProfileIconViewHolder(
        iconClickListener: IconClickListener,
        parent: ViewGroup
    ) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_profile_icon,parent,false)
    ) {
        private val binding = ItemProfileIconBinding.bind(itemView)

        init {
            binding.ivProfileIcon.setOnClickListener{
                iconClickListener.setIconClickListener(layoutPosition)
            }
        }
        fun bind(item: ProfileIcon){
            binding.profileIcon = item
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ProfileIcon>(){
            override fun areItemsTheSame(oldItem: ProfileIcon, newItem: ProfileIcon) =
                (oldItem.icon == newItem.icon) && (oldItem.selected == newItem.selected)

            override fun areContentsTheSame(oldItem: ProfileIcon, newItem: ProfileIcon): Boolean =
                oldItem.hashCode() == newItem.hashCode()

        }
    }


}
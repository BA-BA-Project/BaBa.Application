package kids.baba.mobile.presentation.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ListAdapter
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kids.baba.mobile.databinding.ComposableGroupViewBinding
import kids.baba.mobile.domain.model.Group
import kids.baba.mobile.domain.model.Member
import kids.baba.mobile.presentation.view.bottomsheet.GroupEditBottomSheet
import kids.baba.mobile.presentation.view.dialog.EditMemberDialog

class MyPageGroupAdapter(
    private val context: Context,
    private val childFragmentManager: FragmentManager
) :
    androidx.recyclerview.widget.ListAdapter<Group, MyPageGroupAdapter.ViewHolder>(
        diffUtil
    ) {
    inner class ViewHolder(private val binding: ComposableGroupViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(group: Group) {
            val adapter = MemberAdapter() {
                val editMemberDialog = EditMemberDialog()
                val bundle = Bundle()
                bundle.putParcelable(EditMemberDialog.SELECTED_MEMBER_KEY, it)
                editMemberDialog.arguments = bundle
                editMemberDialog.show(childFragmentManager, EditMemberDialog.TAG)
            }
            binding.title = group.groupName
            binding.description = "[${group.groupName}]그룹과 소식을 공유할 수 있어요"
            binding.rvGroupMembers.adapter = adapter
            binding.rvGroupMembers.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter.submitList(group.members)
            binding.ivEditButton.setOnClickListener {
                val bundle = Bundle()
                val bottomSheet = GroupEditBottomSheet()
                bottomSheet.arguments = bundle
                bottomSheet.show(childFragmentManager, GroupEditBottomSheet.TAG)
            }
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Group>() {
            override fun areItemsTheSame(oldItem: Group, newItem: Group): Boolean =
                oldItem.groupName == newItem.groupName


            override fun areContentsTheSame(oldItem: Group, newItem: Group): Boolean =
                oldItem.groupName == newItem.groupName

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            ComposableGroupViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
package kids.baba.mobile.presentation.view.bottomsheet

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kids.baba.mobile.R
import kids.baba.mobile.databinding.BottomSheetEditGroupBinding
import kids.baba.mobile.databinding.BottomSheetEditProfileBinding
import kids.baba.mobile.presentation.view.activity.MyPageActivity
import kids.baba.mobile.presentation.view.fragment.AddCompleteFragment
import kids.baba.mobile.presentation.view.fragment.InviteMemberFragment

class GroupEditBottomSheet: BottomSheetDialogFragment() {

    private var _binding: BottomSheetEditGroupBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.nameView.tvTitle.text = "가족 이름"
        binding.permissionView.tvTitle.text = "성장앨범 업로드 권함"
        binding.colorView.tvGroupNameTitle.text = "그룹 컬러"
        binding.permissionView.tvDesc.text = "없음"
        binding.deleteView.tvDeleteTitle.text = "그룹 삭제"
        binding.deleteView.tvDeleteDesc.text = "삭제하기"
        binding.addMemberView.tvAddButtonTitle.text = "멤버 초대"
        binding.addMemberView.tvAddButtonDesc.isGone = true
        binding.addMemberView.ivAddButton.setOnClickListener {
            binding.addMemberView.ivAddButton.setOnClickListener {
                requireActivity().startActivity(
                    Intent(
                        requireContext(),
                        MyPageActivity::class.java
                    ).putExtra("next", "member")
                )
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetEditGroupBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        const val TAG = "GroupEditBottomSheet"
        const val SELECTED_GROUP_KEY = "SELECTED_GROUP"
    }

}
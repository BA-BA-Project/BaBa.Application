package kids.baba.mobile.presentation.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kids.baba.mobile.databinding.BottomSheetEditProfileBinding

class MemberEditProfileBottomSheet : BottomSheetDialogFragment() {
    private var _binding: BottomSheetEditProfileBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.nameView.tvTitle.text = "아이 이름"
        binding.introView.tvInputTitle.text = "내 소개"
        binding.colorView.tvGroupNameTitle.text = "배경 컬러"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        const val TAG = "MemberEditProfileBottomSheet"
        const val SELECTED_MEMBER_KEY = "SELECTED_MEMBER"
    }
}
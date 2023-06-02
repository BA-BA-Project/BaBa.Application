package kids.baba.mobile.presentation.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.databinding.BottomSheetEditGroupBinding
import kids.baba.mobile.presentation.view.activity.MyPageActivity
import kids.baba.mobile.presentation.view.fragment.MyPageFragment.Companion.GROUP_NAME
import kids.baba.mobile.presentation.view.fragment.MyPageFragment.Companion.INVITE_MEMBER_PAGE
import kids.baba.mobile.presentation.viewmodel.EditGroupBottomSheetViewModel

@AndroidEntryPoint
class GroupEditBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetEditGroupBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }
    private val viewModel: EditGroupBottomSheetViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addMemberView.tvAddButtonDesc.isGone = true

        binding.addMemberView.ivAddButton.setOnClickListener {
            MyPageActivity.startActivityWithCode(
                requireContext(),
                INVITE_MEMBER_PAGE,
                GROUP_NAME,
                viewModel.groupName.value ?: ""
            )
        }

        binding.nameView.tvEditButton.setOnClickListener {
            val name = binding.nameView.tvEdit.text.toString()
            viewModel.patch(name = name)
            dismiss()
        }

        binding.deleteView.tvDeleteDesc.setOnClickListener {
            viewModel.delete()
            dismiss()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetEditGroupBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "GroupEditBottomSheet"
        const val SELECTED_GROUP_KEY = "SELECTED_GROUP"
    }

}
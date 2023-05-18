package kids.baba.mobile.presentation.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.databinding.BottomSheetEditProfileBinding
import kids.baba.mobile.domain.model.Profile
import kids.baba.mobile.presentation.viewmodel.EditMemberProfileBottomSheetViewModel

@AndroidEntryPoint
class MemberEditProfileBottomSheet(
    val viewModel: EditMemberProfileBottomSheetViewModel,
    val itemClick: (Profile) -> Unit
) : BottomSheetDialogFragment() {
    private var _binding: BottomSheetEditProfileBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.introView.tvInputButton.setOnClickListener {
            val name = binding.nameView.tvEdit.text.toString()
            val intro = binding.introView.etInput.text.toString()
            itemClick(Profile(name = name, introduction = intro, iconName = viewModel.icon.value, iconColor = viewModel.color.value))
            dismiss()
        }
        binding.colorView.pink.setOnClickListener {
            viewModel.color.value = "#FFAEBA"
        }
        binding.colorView.blue.setOnClickListener {
            viewModel.color.value = "#97BEFF"
        }
        binding.iconView.w1.setOnClickListener {
            viewModel.icon.value = trim("W_1")
        }
        binding.iconView.w2.setOnClickListener {
            viewModel.icon.value = trim("W_2")
        }
        binding.iconView.w3.setOnClickListener {
            viewModel.icon.value = trim("W_3")
        }
        binding.iconView.w4.setOnClickListener {
            viewModel.icon.value = trim("W_4")
        }
    }

    private fun trim(icon: String) = "PROFILE_$icon"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetEditProfileBinding.inflate(inflater, container, false)
        binding.viewmodel = viewModel
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "MemberEditProfileBottomSheet"
        const val SELECTED_MEMBER_KEY = "SELECTED_MEMBER"
    }
}
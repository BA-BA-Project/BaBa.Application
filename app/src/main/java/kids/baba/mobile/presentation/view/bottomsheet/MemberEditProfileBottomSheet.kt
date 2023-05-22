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
        setColorButton()
        setIconButton()
    }

    private fun setIconButton() {
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
        binding.iconView.w5.setOnClickListener {
            viewModel.icon.value = trim("W_5")
        }
        binding.iconView.m1.setOnClickListener {
            viewModel.icon.value = trim("M_1")
        }
        binding.iconView.m2.setOnClickListener {
            viewModel.icon.value = trim("M_2")
        }
        binding.iconView.m3.setOnClickListener {
            viewModel.icon.value = trim("M_3")
        }
        binding.iconView.m4.setOnClickListener {
            viewModel.icon.value = trim("M_4")
        }
        binding.iconView.m5.setOnClickListener {
            viewModel.icon.value = trim("M_5")
        }
        binding.iconView.m6.setOnClickListener {
            viewModel.icon.value = trim("M_6")
        }
        binding.iconView.g1.setOnClickListener {
            viewModel.icon.value = trim("G_1")
        }
        binding.iconView.g2.setOnClickListener {
            viewModel.icon.value = trim("G_2")
        }
        binding.iconView.g3.setOnClickListener {
            viewModel.icon.value = trim("G_3")
        }
        binding.iconView.g4.setOnClickListener {
            viewModel.icon.value = trim("G_4")
        }
    }

    private fun setColorButton() {
        binding.colorView.pink.setOnClickListener {
            viewModel.color.value = "#FFAEBA"
        }
        binding.colorView.red.setOnClickListener {
            viewModel.color.value = "#FF8698"
        }
        binding.colorView.blue.setOnClickListener {
            viewModel.color.value = "#97BEFF"
        }
        binding.colorView.darkGreen.setOnClickListener {
            viewModel.color.value = "#30BE9B"
        }
        binding.colorView.green.setOnClickListener {
            viewModel.color.value = "#9ED883"
        }
        binding.colorView.people.setOnClickListener {
            viewModel.color.value = "#98A2FF"
        }
        binding.colorView.violet.setOnClickListener {
            viewModel.color.value = "#BFA1FF"
        }
        binding.colorView.sky.setOnClickListener {
            viewModel.color.value = "#5BD2FF"
        }
        binding.colorView.mint.setOnClickListener {
            viewModel.color.value = "#81E0D5"
        }
        binding.colorView.skin.setOnClickListener {
            viewModel.color.value = "#FFD2A7"
        }
        binding.colorView.whiteSkin.setOnClickListener {
            viewModel.color.value = "#FFE3C8"
        }
        binding.colorView.yellow.setOnClickListener {
            viewModel.color.value = "#FFD400"
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
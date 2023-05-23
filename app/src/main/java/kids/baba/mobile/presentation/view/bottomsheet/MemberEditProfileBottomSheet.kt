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
        mapOf(
            binding.iconView.w1 to "PROFILE_W_1",
            binding.iconView.w2 to "PROFILE_W_2",
            binding.iconView.w3 to "PROFILE_W_3",
            binding.iconView.w4 to "PROFILE_W_4",
            binding.iconView.w5 to "PROFILE_W_5",
            binding.iconView.m1 to "PROFILE_M_1",
            binding.iconView.m2 to "PROFILE_M_2",
            binding.iconView.m3 to "PROFILE_M_3",
            binding.iconView.m4 to "PROFILE_M_4",
            binding.iconView.m5 to "PROFILE_M_5",
            binding.iconView.m6 to "PROFILE_M_6",
            binding.iconView.g1 to "PROFILE_G_1",
            binding.iconView.g2 to "PROFILE_G_2",
            binding.iconView.g3 to "PROFILE_G_3",
            binding.iconView.g4 to "PROFILE_G_4"
        ).forEach { (iconView, icon) ->
            iconView.setOnClickListener {
                viewModel.icon.value = icon
            }
        }
    }

    private fun setColorButton() {
        mapOf(
            binding.colorView.pink to "#FFAEBA",
            binding.colorView.red to "#FF8698",
            binding.colorView.blue to "#97BEFF",
            binding.colorView.darkGreen to "#30BE9B",
            binding.colorView.green to "#9ED883",
            binding.colorView.people to "#98A2FF",
            binding.colorView.violet to "#BFA1FF",
            binding.colorView.sky to "#5BD2FF",
            binding.colorView.mint to "#81E0D5",
            binding.colorView.skin to "#FFD2A7",
            binding.colorView.whiteSkin to "#FFE3C8",
            binding.colorView.yellow to "#FFD400"
        ).forEach { (colorView, color) ->
            colorView.setOnClickListener {
                viewModel.color.value = color
            }
        }
    }

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
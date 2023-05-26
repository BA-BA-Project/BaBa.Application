package kids.baba.mobile.presentation.view.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.databinding.BottomSheetEditProfileBinding
import kids.baba.mobile.domain.model.Profile
import kids.baba.mobile.presentation.adapter.ColorAdapter
import kids.baba.mobile.presentation.adapter.IconAdapter
import kids.baba.mobile.presentation.mapper.getUserProfileIconName
import kids.baba.mobile.presentation.model.ColorModel
import kids.baba.mobile.presentation.model.ColorUiModel
import kids.baba.mobile.presentation.model.UserIconUiModel
import kids.baba.mobile.presentation.model.UserProfileIconUiModel
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
        binding.lifecycleOwner = viewLifecycleOwner
        binding.introView.tvInputButton.setOnClickListener {
            val name = binding.nameView.tvEdit.text.toString()
            val intro = binding.introView.etInput.text.toString()
            itemClick(
                Profile(
                    name = name,
                    introduction = intro,
                    iconName = viewModel.icon.value,
                    iconColor = viewModel.color.value
                )
            )
            dismiss()
        }
        setIconButton()
        setColorButton()
    }

    private fun setIconButton() {

        val icons = mutableListOf<UserIconUiModel>().apply {
            addAll(
                UserProfileIconUiModel
                    .values()
                    .dropLast(1)
                    .map { UserIconUiModel(it, "#EDEDED") }
            )
        }

        val adapter = IconAdapter { item ->
            viewModel.icon.value = getUserProfileIconName(item.userProfileIconUiModel)
        }
        binding.iconView.iconContainer.adapter = adapter
        adapter.submitList(icons)
    }

    private fun setColorButton() {
        val colors = mutableListOf<ColorUiModel>().apply {
            addAll(
                ColorModel
                    .values()
                    .map { ColorUiModel(it.name, it.colorCode) }
            )
        }
        val adapter = ColorAdapter { color -> viewModel.color.value = color.value }
        binding.colorView.colorContainer.adapter = adapter
        adapter.submitList(colors)
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
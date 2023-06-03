package kids.baba.mobile.presentation.view.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.databinding.BottomSheetEditGroupBinding
import kids.baba.mobile.presentation.adapter.ColorAdapter
import kids.baba.mobile.presentation.event.EditGroupSheetEvent
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.model.ColorModel
import kids.baba.mobile.presentation.model.ColorUiModel
import kids.baba.mobile.presentation.view.activity.MyPageActivity
import kids.baba.mobile.presentation.view.fragment.MyPageFragment.Companion.GROUP_NAME
import kids.baba.mobile.presentation.view.fragment.MyPageFragment.Companion.INVITE_MEMBER_PAGE
import kids.baba.mobile.presentation.viewmodel.EditGroupBottomSheetViewModel

@AndroidEntryPoint
class GroupEditBottomSheet(val itemClick: () -> Unit) : BottomSheetDialogFragment() {

    private var _binding: BottomSheetEditGroupBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }
    private val viewModel: EditGroupBottomSheetViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectEvent()
        bindViewModel()
        setColorButton()
    }
       
    private fun bindViewModel() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.addMemberView.ivAddButton.setOnClickListener {
            MyPageActivity.startActivityWithCode(
                requireContext(),
                INVITE_MEMBER_PAGE,
                GROUP_NAME,
                viewModel.groupName.value ?: ""
            )
        }
    }

    private fun setColorButton() {
        val colors = mutableListOf<ColorUiModel>().apply {
            addAll(
                ColorModel
                    .values()
                    .map { ColorUiModel(it.name, it.colorCode) }
            )
        }
        val adapter = ColorAdapter(
            itemClick = { color ->
                viewModel.setColor(color)
            }
        )
        binding.colorView.colorContainer.adapter = adapter
        adapter.submitList(colors)
    }

    private fun collectEvent() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.eventFlow.collect { event ->
                when (event) {
                    is EditGroupSheetEvent.SuccessPatchGroupRelation -> {
                        itemClick()
                        dismiss()
                    }
                    is EditGroupSheetEvent.SuccessDeleteGroup -> {
                        itemClick()
                        dismiss()
                    }
                    is EditGroupSheetEvent.ShowSnackBar -> {
                        dismiss()
                    }
                    is EditGroupSheetEvent.GoToAddMemberPage -> {
                        MyPageActivity.startActivityWithGroupName(
                            requireContext(),
                            INVITE_MEMBER_PAGE,
                            GROUP_NAME,
                            event.groupName
                        )

                    }
                }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "GroupEditBottomSheet"
        const val SELECTED_GROUP_KEY = "SELECTED_GROUP"
    }

}
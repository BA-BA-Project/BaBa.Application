package kids.baba.mobile.presentation.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.databinding.BottomSheetEditBabyBinding
import kids.baba.mobile.presentation.event.BabyEditSheetEvent
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.view.FunctionHolder
import kids.baba.mobile.presentation.view.activity.MyPageActivity
import kids.baba.mobile.presentation.view.fragment.MyPageFragment.Companion.ADD_BABY_PAGE
import kids.baba.mobile.presentation.view.fragment.MyPageFragment.Companion.INVITE_WITH_CODE_PAGE
import kids.baba.mobile.presentation.viewmodel.BabyEditBottomSheetViewModel
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class BabyEditBottomSheet(val itemClick: (String) -> Unit) : BottomSheetDialogFragment() {
    private var _binding: BottomSheetEditBabyBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }
    private val viewModel: BabyEditBottomSheetViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
        collectEvent()
    }

    private fun bindViewModel() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        viewModel.itemClick = itemClick
        viewModel.dismiss = { dismiss() }
        viewModel.getText = { binding.inputNameView.tvEdit.text.toString() }
        binding.inputNameView.tvEditButton.setOnClickListener {
            val name = binding.inputNameView.tvEdit.text.toString()
            itemClick(name)
            dismiss()
        }
        binding.addBabyView.ivAddButton.setOnClickListener {
            MyPageActivity.startActivity(requireContext(), ADD_BABY_PAGE)
        }

        binding.inviteView.ivAddButton.setOnClickListener {
            MyPageActivity.startActivity(requireContext(), INVITE_WITH_CODE_PAGE)
        }
    }

    private fun collectEvent() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    is BabyEditSheetEvent.GoToAddBabyPage -> MyPageActivity.startActivity(requireContext(),"addBaby")
                    is BabyEditSheetEvent.GoToInputInviteCodePage -> MyPageActivity.startActivity(requireContext(),"invite")
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetEditBabyBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        const val TAG = "BabyEditBottomSheet"
        const val SELECTED_BABY_KEY = "SELECTED_BABY"

    }
}
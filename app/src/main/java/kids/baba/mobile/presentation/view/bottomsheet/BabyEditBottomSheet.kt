package kids.baba.mobile.presentation.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.databinding.BottomSheetEditBabyBinding
import kids.baba.mobile.presentation.event.BabyGroupEditEvent
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.view.activity.MyPageActivity
import kids.baba.mobile.presentation.view.fragment.MyPageFragment.Companion.ADD_BABY_PAGE
import kids.baba.mobile.presentation.view.fragment.MyPageFragment.Companion.INVITE_WITH_CODE_PAGE
import kids.baba.mobile.presentation.viewmodel.BabyEditBottomSheetViewModel

@AndroidEntryPoint
class BabyEditBottomSheet(/*val itemClick: (String) -> Unit*/) : BottomSheetDialogFragment() {
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
        binding.lifecycleOwner = viewLifecycleOwner

        collectEvent()

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

    private fun collectEvent() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.eventFlow.collect { event ->
                when (event) {
                    is BabyGroupEditEvent.SuccessBabyGroupEdit -> {
                        dismiss()
                    }
                    is BabyGroupEditEvent.GoToAddBaby -> MyPageActivity.startActivity(requireContext(), ADD_BABY_PAGE)
                    is BabyGroupEditEvent.GoToInputInviteCode -> MyPageActivity.startActivity(
                        requireContext(),
                        INVITE_WITH_CODE_PAGE
                    )
                    is BabyGroupEditEvent.ShowSnackBar -> showSnackBar(event.message)
                }
            }
        }
    }

    private fun showSnackBar(@StringRes text: Int) {
        Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
    }

    companion object {
        const val TAG = "BabyEditBottomSheet"
        const val SELECTED_BABY_KEY = "SELECTED_BABY"
    }
}
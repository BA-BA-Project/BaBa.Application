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
import kids.baba.mobile.databinding.BottomSheetEditBabyProfileBinding
import kids.baba.mobile.presentation.event.BabyEditEvent
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.viewmodel.BabyEditProfileBottomSheetViewModel
import javax.inject.Inject

@AndroidEntryPoint
class BabyEditProfileBottomSheet @Inject constructor(
    var itemClick: (String) -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: BottomSheetEditBabyProfileBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }
    private val viewModel: BabyEditProfileBottomSheetViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
        collectEvent()
    }

    private fun collectEvent() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.eventFlow.collect { event ->
                when (event) {
                    is BabyEditEvent.SuccessBabyEdit -> {
                        itemClick(event.babyName)
                        dismiss()
                    }
                    is BabyEditEvent.ShowSnackBar -> {
                        dismiss()
                        showSnackBar(event.message)
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
        _binding = BottomSheetEditBabyProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun bindViewModel() {
        binding.viewmodel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }


    private fun showSnackBar(@StringRes text: Int) {
        Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "BabyEditProfileBottomSheet"
        const val SELECTED_BABY_KEY = "SELECTED_BABY"
    }
}
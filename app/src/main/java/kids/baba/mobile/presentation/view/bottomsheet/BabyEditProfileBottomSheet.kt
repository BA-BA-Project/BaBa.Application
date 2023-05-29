package kids.baba.mobile.presentation.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.databinding.BottomSheetEditBabyProfileBinding
import kids.baba.mobile.presentation.event.BabyEditEvent
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.viewmodel.BabyEditProfileBottomSheetViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BabyEditProfileBottomSheet(val itemClick: (String) -> Unit) : BottomSheetDialogFragment() {

    private var _binding: BottomSheetEditBabyProfileBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }
    private val viewModel: BabyEditProfileBottomSheetViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.nameView.tvEditButton.setOnClickListener {
            val name = binding.nameView.tvEdit.text.toString()
            viewLifecycleOwner.lifecycleScope.launch {
                itemClick(name)
                viewModel.edit(babyId = viewModel.baby.value?.memberId ?: "", name = name).join()
                dismiss()
            }
        }
        collectEvent()
    }

    private fun collectEvent() {
        repeatOnStarted {
            viewModel.eventFlow.collect { event ->
                when (event) {
                    is BabyEditEvent.SuccessBabyEdit -> {}
                    is BabyEditEvent.ShowSnackBar -> {
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
        binding.viewmodel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
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
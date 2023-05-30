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
import kids.baba.mobile.databinding.BottomSheetBabyListBinding
import kids.baba.mobile.presentation.adapter.BabyAdapter
import kids.baba.mobile.presentation.event.BabyListEvent
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.model.BabyUiModel
import kids.baba.mobile.presentation.viewmodel.BabyListViewModel

@AndroidEntryPoint
class BabyListBottomSheet(
    val itemClick: (BabyUiModel) -> Unit,
    val moveBabyManagement: () -> Unit
) : BottomSheetDialogFragment() {
    private var _binding: BottomSheetBabyListBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }

    private val viewModel: BabyListViewModel by viewModels()

    private lateinit var babiesAdapter: BabyAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetBabyListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBinding()
        setBabyList()
        collectEvent()
    }

    private fun setBinding() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun collectEvent() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.eventFlow.collect { event ->
                when (event) {
                    is BabyListEvent.ShowSnackBar -> showSnackBar(event.message)
                    is BabyListEvent.MoveBabyManagement -> {
                        dismiss()
                        moveBabyManagement()
                    }
                }
            }
        }
    }

    private fun setBabyList() {
        babiesAdapter = BabyAdapter {
            itemClick(it)
            dismiss()
        }

        binding.rvBabies.adapter = babiesAdapter
        viewLifecycleOwner.repeatOnStarted {
            viewModel.babyList.collect {
                babiesAdapter.submitList(it)
            }
        }
    }

    private fun showSnackBar(@StringRes message: Int) {
        //TODO 바텀시트에 가려져 스낵바가 보이지 않음
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "BabyListBottomSheet"
        const val SELECTED_BABY_ID_KEY = "SELECTED_BABY"
    }
}
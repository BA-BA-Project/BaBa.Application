package kids.baba.mobile.presentation.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.databinding.FragmentAddCompleteBinding
import kids.baba.mobile.presentation.event.AddBabyEvent
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.viewmodel.AddCompleteViewModel
import kids.baba.mobile.presentation.viewmodel.MyPageActivityViewModel

@AndroidEntryPoint
class AddCompleteFragment : Fragment() {

    private var _binding: FragmentAddCompleteBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }

    private val viewModel: AddCompleteViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collectEvent()
        binding.topAppBar.ivBackButton.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnComplete.setOnClickListener {
            requireActivity().finish()
        }
    }

    private fun collectEvent() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.eventFlow.collect { event ->
                when (event) {
                    is AddBabyEvent.SuccessAddBaby -> {
                        requireActivity().finish()
                    }
                    is AddBabyEvent.ShowSnackBar -> {
                        showSnackBar(event.message)
                    }
                    is AddBabyEvent.BackButtonClicked -> {
                        findNavController().navigateUp()
                    }
                }
            }
        }
    }

    private fun showSnackBar(@StringRes text: Int) {
        Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
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
        _binding = FragmentAddCompleteBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        return binding.root
    }
}
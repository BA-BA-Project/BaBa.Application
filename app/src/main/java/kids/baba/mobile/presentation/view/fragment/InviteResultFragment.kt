package kids.baba.mobile.presentation.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.databinding.FragmentInviteResultBinding
import kids.baba.mobile.presentation.event.InviteResultEvent
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.viewmodel.InviteResultViewModel
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class InviteResultFragment : Fragment() {

    private var _binding: FragmentInviteResultBinding? = null
    private val binding get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }
    private val viewModel: InviteResultViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInviteResultBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collectEvent()

//        binding.topAppBar.ivBackButton.setOnClickListener {
//            findNavController().navigateUp()
//        }
//        binding.btnComplete.setOnClickListener {
//            requireActivity().finish()
//        }
    }


    private fun collectEvent() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.eventFlow.collect { event ->
                when (event) {
                    is InviteResultEvent.SuccessGetInvitationInfo -> Log.e("SuccessGetInvitationInfo", "SuccessGetInvitationInfo")
                    is InviteResultEvent.ShowSnackBar -> showSnackBar(event.message)
                    is InviteResultEvent.BackButtonClicked -> {
                        findNavController().navigateUp()
                    }
                    is InviteResultEvent.GoToMyPage -> {
                        requireActivity().finish()
                    }
                }
            }
        }
    }

    private fun showSnackBar(@StringRes text: Int) {
        Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
    }
}

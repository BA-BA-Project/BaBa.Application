package kids.baba.mobile.presentation.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.databinding.FragmentInputInvitecodeBinding
import kids.baba.mobile.presentation.event.BabyInviteCodeEvent
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.viewmodel.InputInviteCodeViewModel

@AndroidEntryPoint
class InputInviteCodeFragment : Fragment() {

    private var _binding: FragmentInputInvitecodeBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }
    private val viewModel: InputInviteCodeViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInputInvitecodeBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectState()
    }

    private fun collectState() {
        viewLifecycleOwner.repeatOnStarted {
//            viewModel.uiState.collect{
//                when(it){
//                    is InputCodeState.Idle -> {}
//                    is InputCodeState.LoadInfo -> {
//                        val response = it.data
//                        Log.e("response","$response")
////                        val action = InputInviteCodeFragmentDirections.actionInputInviteFragmentToInputInviteResultFragment(response)
////                        navController.navigate(action)
//						  findNavController().navigate(R.id.action_input_invite_fragment_to_input_invite_result_fragment)
//
//                    }
//                }
//            }
            viewModel.eventFlow.collect{event ->
                when(event){
                    is BabyInviteCodeEvent.SuccessAddBabyWithInviteCode -> {
                        val action = InputInviteCodeFragmentDirections.actionInputInviteFragmentToInputInviteResultFragment(event.inviteCode.inviteCode)
                        findNavController().navigate(action)
                    }
                    is BabyInviteCodeEvent.BackButtonClicked -> requireActivity().finish()
                    is BabyInviteCodeEvent.ShowSnackBar -> {
                        showSnackBar(event.message)
                    }
                }
            }


        }
    }

    private fun showSnackBar(@StringRes text: Int) {
        Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
    }
}
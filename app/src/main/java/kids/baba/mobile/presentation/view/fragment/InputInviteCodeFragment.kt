package kids.baba.mobile.presentation.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.R
import kids.baba.mobile.databinding.FragmentInputInvitecodeBinding
import kids.baba.mobile.presentation.viewmodel.InputInviteCodeViewModel
import kotlinx.coroutines.flow.collect

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
        binding.topAppBar.ivBackButton.setOnClickListener {
            requireActivity().finish()
        }

        binding.btnAddInviteUser.setOnClickListener {
            val inviteCode = binding.inputView.etInput.text.toString()
            viewModel.add(inviteCode)
        }
    }

    private fun collectState() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.uiState.collect{
                when(it){
                    is InputCodeState.Idle -> {}
                    is InputCodeState.LoadInfo -> {
                        val response = it.data
                        Log.e("response","$response")
//                        val action = InputInviteCodeFragmentDirections.actionInputInviteFragmentToInputInviteResultFragment(response)
//                        navController.navigate(action)
						  findNavController().navigate(R.id.action_input_invite_fragment_to_input_invite_result_fragment)

                    }
                }
            }
        }
    }
}
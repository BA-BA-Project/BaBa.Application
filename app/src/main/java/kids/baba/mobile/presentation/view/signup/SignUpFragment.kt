package kids.baba.mobile.presentation.view.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.R
import kids.baba.mobile.databinding.FragmentSignUpBinding
import kids.baba.mobile.presentation.adapter.SignUpChatAdapter
import kids.baba.mobile.presentation.event.SignUpEvent
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.model.ChatUserType
import kids.baba.mobile.presentation.state.SignUpUiState
import kids.baba.mobile.presentation.viewmodel.SignUpViewModel

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }

    private lateinit var signUpChatAdapter: SignUpChatAdapter

    val viewModel: SignUpViewModel by viewModels()

    private lateinit var childNavController: NavController


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tbSighUp.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        setNavController()

        signUpChatAdapter = SignUpChatAdapter()
        binding.rvSignUpChat.adapter = signUpChatAdapter
        collectUiState()
        collectEvent()
    }

    private fun setNavController() {
        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.fcv_input_profile) as NavHostFragment
        childNavController = navHostFragment.navController
    }

    private fun collectUiState() {
        repeatOnStarted {
            viewModel.signUpUiState.collect { uiState ->
                when (uiState) {
                    is SignUpUiState.SelectGreeting -> {
                        viewModel.addChat(
                            ChatUserType.BABA_FIRST,
                            getString(R.string.sitn_up_greeting1),
                            false
                        )
                        viewModel.addChat(
                            ChatUserType.BABA,
                            getString(R.string.sitn_up_greeting2),
                            false
                        )
                        viewModel.setEvent(SignUpEvent.WaitGreeting)
                    }

                    is SignUpUiState.InputName -> {
                        viewModel.addChat(
                            ChatUserType.BABA_FIRST,
                            getString(R.string.please_input_name),
                            false
                        )
                        viewModel.setEvent(SignUpEvent.WaitName)
                    }

                    is SignUpUiState.ModifyName -> {
                        viewModel.setEvent(SignUpEvent.WaitProfile)
                    }

                    is SignUpUiState.SelectProfile -> {
                        viewModel.setEvent(SignUpEvent.EndCreateProfile)
                    }

                    else -> Unit
                }
            }
        }
        repeatOnStarted {
            viewModel.chatList.collect {
                signUpChatAdapter.submitList(it)
            }
        }
    }

    private fun collectEvent() {
        repeatOnStarted {
            viewModel.eventFlow.collect { event ->
                when (event) {
                    is SignUpEvent.WaitGreeting -> {
                        childNavController.navigate(R.id.action_global_greetingSelectFragment)
                    }

                    is SignUpEvent.WaitName -> {
                        childNavController.navigate(R.id.action_global_textInputFragment)
                    }

                    else -> {}
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
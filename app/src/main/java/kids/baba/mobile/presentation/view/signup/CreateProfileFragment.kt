package kids.baba.mobile.presentation.view.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.R
import kids.baba.mobile.databinding.FragmentCreateProfileBinding
import kids.baba.mobile.presentation.adapter.SignUpChatAdapter
import kids.baba.mobile.presentation.event.CreateProfileEvent
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.model.ProfileIcon
import kids.baba.mobile.presentation.state.CreateProfileUiState
import kids.baba.mobile.presentation.viewmodel.CreateProfileViewModel
import kids.baba.mobile.presentation.viewmodel.IntroViewModel

@AndroidEntryPoint
class CreateProfileFragment : Fragment(), SignUpChatAdapter.ChatEventListener {

    private var _binding: FragmentCreateProfileBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }

    private lateinit var signUpChatAdapter: SignUpChatAdapter

    private val activityViewModel : IntroViewModel by activityViewModels()

    private val args: CreateProfileFragmentArgs by navArgs()

    val viewModel: CreateProfileViewModel by viewModels()

    private lateinit var childNavController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateProfileBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tbCreateProfile.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        setNavController()
        setRecyclerView()
        collectUiState()
        collectEvent()
    }

    private fun setRecyclerView() {
        signUpChatAdapter = SignUpChatAdapter(this)
        binding.rvCreateProfile.adapter = signUpChatAdapter
    }

    private fun setNavController() {
        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.fcv_input_profile) as NavHostFragment
        childNavController = navHostFragment.navController
    }

    private fun collectUiState() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.signUpUiState.collect { uiState ->
                when (uiState) {
                    is CreateProfileUiState.SelectGreeting -> {
                        viewModel.setEvent(CreateProfileEvent.WaitGreeting)
                    }

                    is CreateProfileUiState.InputName -> {
                        viewModel.setEvent(CreateProfileEvent.WaitName)
                    }

                    is CreateProfileUiState.ModifyName -> {
                        viewModel.setEvent(CreateProfileEvent.WaitName)
                        viewModel.changeModifyState(uiState.position)
                    }

                    is CreateProfileUiState.SelectProfileIcon -> {
                        viewModel.setEvent(CreateProfileEvent.ProfileSelectLoading)
                    }

                    is CreateProfileUiState.EndCreateProfile -> {
                        viewModel.setEvent(CreateProfileEvent.EndCreateProfile)
                    }

                    is CreateProfileUiState.Loading -> Unit

                }
            }
        }
        viewLifecycleOwner.repeatOnStarted {
            viewModel.chatList.collect {
                signUpChatAdapter.submitList(it)
                binding.rvCreateProfile.post {
                    binding.rvCreateProfile.smoothScrollToPosition(it.size - 1)
                }
            }
        }
    }

    private fun collectEvent() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.eventFlow.collect { event ->
                childNavController.popBackStack()
                when (event) {
                    is CreateProfileEvent.WaitGreeting -> {
                        childNavController.navigate(R.id.action_global_greetingSelectFragment)
                    }

                    is CreateProfileEvent.WaitName -> {
                        childNavController.navigate(R.id.action_global_textInputFragment)
                    }

                    is CreateProfileEvent.ProfileSelectLoading -> {
                        childNavController.navigate(R.id.action_global_blankFragment)
                    }

                    is CreateProfileEvent.EndCreateProfile -> {
                        childNavController.navigate(R.id.action_global_InputEndFragment)
                    }

                    is CreateProfileEvent.MoveToInputChildInfo -> {
                        activityViewModel.isNeedToBabiesInfo(args.signToken, event.userProfile)
                    }
                }
            }
        }
    }

    override fun onModifyClickListener(position: Int) {
        viewModel.setUiState(CreateProfileUiState.ModifyName(position))
    }

    override fun onIconSelectedListener(profileIcon: ProfileIcon, idx: Int, position: Int) {
        viewModel.setUserIcon(profileIcon, idx, position)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}
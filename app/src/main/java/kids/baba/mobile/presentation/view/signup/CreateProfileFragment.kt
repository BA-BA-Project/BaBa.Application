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
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.R
import kids.baba.mobile.databinding.FragmentCreateProfileBinding
import kids.baba.mobile.presentation.adapter.SignUpChatAdapter
import kids.baba.mobile.presentation.event.CreateProfileEvent
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.model.ChatItem
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

    private lateinit var activityViewModel : IntroViewModel

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
        repeatOnStarted {
            viewModel.signUpUiState.collect { uiState ->
                when (uiState) {
                    is CreateProfileUiState.SelectGreeting -> {
                        viewModel.addChat(
                            ChatItem.BabaFirstChatItem(getString(R.string.sign_up_greeting1))
                        )
                        viewModel.addChat(
                            ChatItem.BabaChatItem(getString(R.string.sign_up_greeting2))
                        )
                        viewModel.setEvent(CreateProfileEvent.WaitGreeting)
                    }

                    is CreateProfileUiState.InputName -> {
                        viewModel.addChat(
                            ChatItem.BabaFirstChatItem(getString(R.string.please_input_name))
                        )
                        viewModel.setEvent(CreateProfileEvent.WaitName)
                    }

                    is CreateProfileUiState.ModifyName -> {
                        viewModel.setEvent(CreateProfileEvent.WaitName)
                        viewModel.changeModifyState(uiState.position)
                    }

                    is CreateProfileUiState.SelectProfileIcon -> {
                        viewModel.addChat(
                            ChatItem.BabaFirstChatItem(getString(R.string.please_select_profile_icon))
                        )
                        viewModel.addProfileList()
                    }

                    is CreateProfileUiState.EndCreateProfile -> {
                        viewModel.setEvent(CreateProfileEvent.EndCreateProfile)
                    }

                    is CreateProfileUiState.Loading -> {
                        viewModel.setEvent(CreateProfileEvent.ProfileSelectLoading)
                    }

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
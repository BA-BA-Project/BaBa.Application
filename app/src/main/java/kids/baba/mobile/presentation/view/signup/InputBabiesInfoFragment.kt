package kids.baba.mobile.presentation.view.signup

import android.os.Bundle
import android.util.Log
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
import kids.baba.mobile.databinding.FragmentInputBabiesInfoBinding
import kids.baba.mobile.presentation.adapter.SignUpChatAdapter
import kids.baba.mobile.presentation.event.InputBabiesInfoEvent
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.model.ProfileIcon
import kids.baba.mobile.presentation.state.InputBabiesInfoUiState
import kids.baba.mobile.presentation.viewmodel.InputBabiesInfoViewModel

@AndroidEntryPoint
class InputBabiesInfoFragment : Fragment() {

    private var _binding: FragmentInputBabiesInfoBinding? = null
    val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }

    val viewModel: InputBabiesInfoViewModel by viewModels()

    private lateinit var childNavController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInputBabiesInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tbInputBabiesInfo.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        setRecyclerView()
        setNavController()
        collectUiState()
        collectEvent()
        collectHaveInviteCode()
    }

    private fun collectHaveInviteCode() {
        repeatOnStarted {
            viewModel.haveInviteCode.collect { haveInviteCode ->
                if (haveInviteCode != null) {
                    if (haveInviteCode) {
                        viewModel.inputInviteCode()
                    } else {
                        viewModel.inputBabiesInfo()
                    }
                }
            }
        }
    }

    private fun collectUiState() {
        repeatOnStarted {
            viewModel.uiState.collect { uiState ->
                when (uiState) {
                    is InputBabiesInfoUiState.CheckInviteCode -> {
                        viewModel.setEvent(InputBabiesInfoEvent.SelectHaveInviteCode)
                    }

                    is InputBabiesInfoUiState.CheckMoreBaby -> {
                        viewModel.setEvent(InputBabiesInfoEvent.InputCheckMoreBaby)
                    }

                    is InputBabiesInfoUiState.InputBabyBirthDay -> {
                        viewModel.setEvent(InputBabiesInfoEvent.InputBirthDay)
                    }

                    is InputBabiesInfoUiState.InputBabyName -> {
                        viewModel.setEvent(InputBabiesInfoEvent.InputText)
                    }

                    is InputBabiesInfoUiState.InputInviteCode -> {
                        viewModel.setEvent(InputBabiesInfoEvent.InputText)
                    }

                    is InputBabiesInfoUiState.InputEnd -> {
                        viewModel.setEvent(InputBabiesInfoEvent.InputEnd)
                    }

                    is InputBabiesInfoUiState.InputRelation -> {
                        viewModel.setEvent(InputBabiesInfoEvent.InputRelation)
                    }

                    is InputBabiesInfoUiState.Loading -> {
                    }

                    is InputBabiesInfoUiState.ModifyBirthday -> {
                        viewModel.setEvent(InputBabiesInfoEvent.InputBirthDay)
                    }

                    is InputBabiesInfoUiState.ModifyHaveInviteCode -> {
                        viewModel.setEvent(InputBabiesInfoEvent.SelectHaveInviteCode)
                    }

                    is InputBabiesInfoUiState.ModifyName -> {
                        viewModel.setEvent(InputBabiesInfoEvent.InputText)
                    }
                }
            }
        }
    }

    private fun collectEvent() {
        repeatOnStarted {
            viewModel.eventFlow.collect { event ->
                childNavController.popBackStack()
                Log.d("eventPrint", "$event")
                when (event) {
                    is InputBabiesInfoEvent.InputEnd -> {
                        childNavController.navigate(R.id.action_global_inputBabyInfoEndFragment)
                    }

                    is InputBabiesInfoEvent.SelectHaveInviteCode -> {
                        childNavController.navigate(R.id.action_global_checkHaveInviteCodeFragment)
                    }

                    is InputBabiesInfoEvent.InputText -> {
                        childNavController.navigate(R.id.action_global_textInputBabyInfoFragment)
                    }

                    is InputBabiesInfoEvent.InputBirthDay -> {
                        childNavController.navigate(R.id.action_global_birthdaySelectFragment)
                    }

                    is InputBabiesInfoEvent.InputCheckMoreBaby -> {
                        childNavController.navigate(R.id.action_global_checkHaveInviteCodeFragment)
                    }

                    is InputBabiesInfoEvent.InputRelation -> {
                        childNavController.navigate(R.id.action_global_relationSelectFragment)
                    }
                }

            }
        }
    }

    private fun setNavController() {
        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.fcv_input_children_info) as NavHostFragment
        childNavController = navHostFragment.navController
    }

    private fun setRecyclerView() {
        val adapter = SignUpChatAdapter(object :
            SignUpChatAdapter.ChatEventListener {
            override fun onModifyClickListener(position: Int) {
                viewModel.modifyUserChat(position)
            }

            override fun onIconSelectedListener(profileIcon: ProfileIcon, idx: Int, position: Int) {
                TODO("Not yet implemented")
            }

        })
        binding.rvInputBabiesInfoChat.adapter = adapter
        binding.rvInputBabiesInfoChat.itemAnimator = null

        repeatOnStarted {
            viewModel.chatList.collect {
                adapter.submitList(it)
                binding.rvInputBabiesInfoChat.post {
                    binding.rvInputBabiesInfoChat.scrollToPosition(it.size - 1)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
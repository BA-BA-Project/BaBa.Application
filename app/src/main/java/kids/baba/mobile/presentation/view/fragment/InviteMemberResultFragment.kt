package kids.baba.mobile.presentation.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.databinding.FragmentMemberInviteResultBinding
import kids.baba.mobile.presentation.event.InviteResultEvent
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.view.activity.MainActivity
import kids.baba.mobile.presentation.viewmodel.InviteMemberResultViewModel

@AndroidEntryPoint
class InviteMemberResultFragment : Fragment() {

    private var _binding: FragmentMemberInviteResultBinding? = null
    private val binding get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }
    private val viewModel: InviteMemberResultViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectEvent()
        bindViewModel()
    }

    private fun collectEvent() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.eventFlow.collect {
                when (it) {
                    is InviteResultEvent.SuccessGetInvitationInfo -> {
                    }
                    is InviteResultEvent.SuccessAddMember -> {}
                    is InviteResultEvent.GoToBack -> {
                        findNavController().navigateUp()
                    }
                    is InviteResultEvent.GoToMyPage -> {
                        MainActivity.startActivity(requireActivity())
                        requireActivity().finish()
                    }
                    is InviteResultEvent.ShowSnackBar -> {

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMemberInviteResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun bindViewModel() {
        binding.viewmodel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

}
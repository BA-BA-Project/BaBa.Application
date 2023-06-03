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
import kids.baba.mobile.R
import kids.baba.mobile.databinding.FragmentInviteMemberBinding
import kids.baba.mobile.presentation.event.InviteMemberEvent
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.viewmodel.InviteMemberViewModel

@AndroidEntryPoint
class InviteMemberFragment : Fragment() {

    private var _binding: FragmentInviteMemberBinding? = null
    private val binding get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }
    private val viewModel: InviteMemberViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
        collectEvent()
    }

    private fun bindViewModel() {
        binding.viewmodel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun collectEvent() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.eventFlow.collect { event ->
                when (event) {
                    is InviteMemberEvent.GoToBack -> {
                        requireActivity().finish()
                    }
                    is InviteMemberEvent.InviteWithKakao -> {
                        findNavController().navigate(R.id.action_invite_member_fragment_to_invite_member_result_fragment)
                    }
                    is InviteMemberEvent.CopyInviteCode -> {
                        findNavController().navigate(R.id.action_invite_member_fragment_to_invite_member_result_fragment)
                    }
                    is InviteMemberEvent.ShowSnackBar -> {
                        showSnackBar(event.message)
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
        _binding = FragmentInviteMemberBinding.inflate(inflater, container, false)
        return binding.root
    }
}
package kids.baba.mobile.presentation.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.R
import kids.baba.mobile.databinding.FragmentSettingBinding
import kids.baba.mobile.presentation.event.SettingEvent
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.viewmodel.MyPageSettingViewModel

@AndroidEntryPoint
class SettingFragment : Fragment() {
    private var _binding: FragmentSettingBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }
    private val viewModel: MyPageSettingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        bindViewModel()
        return binding.root
    }

    private fun bindViewModel() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectEvent()
    }

    private fun collectEvent() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.eventFlow.collect { event ->
                when (event) {
                    is SettingEvent.GoToServiceInfo -> {
                        findNavController().navigate(R.id.action_setting_fragment_to_service_info_fragment)
                    }
                    is SettingEvent.GoToDeleteMember -> {
                        findNavController().navigate(R.id.action_setting_fragment_to_delete_member_fragment)
                    }
                    is SettingEvent.GoToAsk -> {
                        findNavController().navigate(R.id.action_setting_fragment_to_ask_fragment)
                    }
                    is SettingEvent.GoToCreator -> {
                        findNavController().navigate(R.id.action_setting_fragment_to_creator_fragment)
                    }
                    is SettingEvent.BackButtonClicked -> {
                        requireActivity().finish()
                    }
                }
            }
        }
    }


}
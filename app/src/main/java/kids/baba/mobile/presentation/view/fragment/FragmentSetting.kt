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
import kids.baba.mobile.presentation.viewmodel.MyPageSettingViewModel

@AndroidEntryPoint
class FragmentSetting : Fragment() {
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
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvServiceInfo.setOnClickListener {
            findNavController().navigate(R.id.action_setting_fragment_to_service_info_fragment)
        }
        binding.tvDeleteMember.setOnClickListener {
            findNavController().navigate(R.id.action_setting_fragment_to_delete_member_fragment)
        }
        binding.tvAsk.setOnClickListener {
            findNavController().navigate(R.id.action_setting_fragment_to_ask_fragment)
        }
        binding.tvCreator.setOnClickListener {
            findNavController().navigate(R.id.action_setting_fragment_to_creator_fragment)
        }
        binding.topAppBar.ivBackButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}
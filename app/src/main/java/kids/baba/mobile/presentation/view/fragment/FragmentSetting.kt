package kids.baba.mobile.presentation.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
            showFragment(FragmentServiceInfo())
        }
        binding.tvDeleteMember.setOnClickListener {
            showFragment(FragmentDeleteMember())
        }
        binding.tvAsk.setOnClickListener {
            showFragment(FragmentAsk())
        }
        binding.tvCreator.setOnClickListener {
            showFragment(FragmentCreator())
        }
    }

    private fun showFragment(fragment: Fragment) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
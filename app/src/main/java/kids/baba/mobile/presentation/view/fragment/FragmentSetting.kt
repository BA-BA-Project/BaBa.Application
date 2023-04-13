package kids.baba.mobile.presentation.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kids.baba.mobile.R
import kids.baba.mobile.databinding.FragmentSettingBinding

class FragmentSetting : Fragment() {
    private var _binding: FragmentSettingBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
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
        binding.topAppBar.tvTopTitle.text = "설정"
    }

    private fun showFragment(fragment: Fragment) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
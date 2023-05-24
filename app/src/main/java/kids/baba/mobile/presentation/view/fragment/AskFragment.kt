package kids.baba.mobile.presentation.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.R
import kids.baba.mobile.databinding.FragmentAskBinding
import kids.baba.mobile.presentation.view.DataBindingFunctionHolder

@AndroidEntryPoint
class AskFragment : Fragment() {
    private var _binding: FragmentAskBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAskBinding.inflate(inflater, container, false)
        binding.title = getString(R.string.ask)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.topAppBar.callback = object : DataBindingFunctionHolder {
            override fun click() {
                findNavController().navigateUp()
            }

        }
//        binding.topAppBar.ivBackButton.setOnClickListener {
//            findNavController().navigateUp()
//        }
    }
}
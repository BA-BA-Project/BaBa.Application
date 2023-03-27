package kids.baba.mobile.presentation.view.film

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.databinding.FragmentSelectCardBinding
import kids.baba.mobile.presentation.viewmodel.SelectCardViewModel
import javax.inject.Inject

@AndroidEntryPoint
class SelectCardFragment @Inject constructor(

) : Fragment() {

    private var _binding: FragmentSelectCardBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }

    val viewModel: SelectCardViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSelectCardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
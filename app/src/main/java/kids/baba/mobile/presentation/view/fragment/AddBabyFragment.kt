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
import kids.baba.mobile.databinding.FragmentAddbabyBinding
import kids.baba.mobile.presentation.viewmodel.AddBabyViewModel

@AndroidEntryPoint
class AddBabyFragment : Fragment() {

    private var _binding: FragmentAddbabyBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }
    private val viewModel: AddBabyViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddbabyBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.topAppBar.ivBackButton.setOnClickListener {
            requireActivity().finish()
        }
        binding.btnAddBaby.setOnClickListener {
            val name = binding.nameView.etInput.text.toString()
            val relation = binding.relationView.etInput.text.toString()
            val birthDay = binding.birthView.etInput.text.toString()
            viewModel.addBaby(name = name, relationName = relation, birthDay = birthDay)
            findNavController().navigate(R.id.action_add_baby_fragment_to_add_complete_fragment)
        }
    }
}
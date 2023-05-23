package kids.baba.mobile.presentation.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.databinding.FragmentAddGroupBinding
import kids.baba.mobile.presentation.viewmodel.AddGroupViewModel
import kids.baba.mobile.presentation.viewmodel.MyPageViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddGroupFragment : Fragment() {

    private var _binding: FragmentAddGroupBinding? = null
    private val binding get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }
    private val viewModel: AddGroupViewModel by viewModels()
    private val myPageViewModel: MyPageViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddGroupBinding.inflate(inflater, container, false)
        binding.viewmodel = viewModel
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setColorButton()
        binding.topAppBar.ivBackButton.setOnClickListener {
            requireActivity().finish()
        }
        binding.btnAdd.setOnClickListener {
            lifecycleScope.launch {
                val name = binding.nameView.etInput.text.toString()
                viewModel.addGroup(name, viewModel.color.value).join()
                myPageViewModel.loadGroups()
                requireActivity().finish()
            }
        }
    }

    private fun setColorButton() {
        binding.colorView.pink.setOnClickListener {
            viewModel.color.value = "#FFAEBA"
        }
        binding.colorView.red.setOnClickListener {
            viewModel.color.value = "#FF8698"
        }
        binding.colorView.blue.setOnClickListener {
            viewModel.color.value = "#97BEFF"
        }
        binding.colorView.darkGreen.setOnClickListener {
            viewModel.color.value = "#30BE9B"
        }
        binding.colorView.green.setOnClickListener {
            viewModel.color.value = "#9ED883"
        }
        binding.colorView.people.setOnClickListener {
            viewModel.color.value = "#98A2FF"
        }
        binding.colorView.violet.setOnClickListener {
            viewModel.color.value = "#BFA1FF"
        }
        binding.colorView.sky.setOnClickListener {
            viewModel.color.value = "#5BD2FF"
        }
        binding.colorView.mint.setOnClickListener {
            viewModel.color.value = "#81E0D5"
        }
        binding.colorView.skin.setOnClickListener {
            viewModel.color.value = "#FFD2A7"
        }
        binding.colorView.whiteSkin.setOnClickListener {
            viewModel.color.value = "#FFE3C8"
        }
        binding.colorView.yellow.setOnClickListener {
            viewModel.color.value = "#FFD400"
        }
    }

}
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
                viewModel.addGroup(name).join()
                myPageViewModel.loadGroups()
                requireActivity().finish()
            }
        }
    }

    private fun setColorButton() {
        mapOf(
            binding.colorView.pink to "#FFAEBA",
            binding.colorView.red to "#FF8698",
            binding.colorView.blue to "#97BEFF",
            binding.colorView.darkGreen to "#30BE9B",
            binding.colorView.green to "#9ED883",
            binding.colorView.people to "#98A2FF",
            binding.colorView.violet to "#BFA1FF",
            binding.colorView.sky to "#5BD2FF",
            binding.colorView.mint to "#81E0D5",
            binding.colorView.skin to "#FFD2A7",
            binding.colorView.whiteSkin to "#FFE3C8",
            binding.colorView.yellow to "#FFD400"
        ).forEach { (colorView, color) ->
            colorView.setOnClickListener {
                viewModel.color.value = color
            }
        }
    }

}
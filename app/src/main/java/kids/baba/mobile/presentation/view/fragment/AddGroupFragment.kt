package kids.baba.mobile.presentation.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.databinding.FragmentAddGroupBinding
import kids.baba.mobile.presentation.adapter.ColorAdapter
import kids.baba.mobile.presentation.event.AddGroupEvent
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.model.ColorModel
import kids.baba.mobile.presentation.model.ColorUiModel
import kids.baba.mobile.presentation.viewmodel.AddGroupViewModel

@AndroidEntryPoint
class AddGroupFragment : Fragment() {

    private var _binding: FragmentAddGroupBinding? = null
    private val binding get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }
    private val viewModel: AddGroupViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddGroupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()

        setColorButton()
        collectEvent()
    }

    private fun bindViewModel() {
        binding.viewmodel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun setColorButton() {
        val colors = mutableListOf<ColorUiModel>().apply {
            addAll(
                ColorModel
                    .values()
                    .map { ColorUiModel(it.name, it.colorCode) }
            )
        }
        val adapter = ColorAdapter { color ->
            viewModel.color.value = color.value
        }
        binding.colorView.colorContainer.adapter = adapter
        adapter.submitList(colors)
    }

    private fun collectEvent() {
        repeatOnStarted {
            viewModel.eventFlow.collect { event ->
                when (event) {
                    is AddGroupEvent.SuccessAddGroup -> {
                        requireActivity().finish()
                    }
                    is AddGroupEvent.ShowSnackBar -> {
                        showSnackBar(event.message)
                    }
                    is AddGroupEvent.GoToBack -> {
                        requireActivity().finish()
                    }
                }
            }
        }
    }

    private fun showSnackBar(@StringRes text: Int) {
        Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
    }
}
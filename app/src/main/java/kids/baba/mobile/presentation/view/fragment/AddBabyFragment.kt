package kids.baba.mobile.presentation.view.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.databinding.FragmentAddbabyBinding
import kids.baba.mobile.presentation.event.AddBabyEvent
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.viewmodel.AddBabyViewModel
import kids.baba.mobile.presentation.viewmodel.MyPageActivityViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class AddBabyFragment : Fragment() {

    private var _binding: FragmentAddbabyBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }

    private val viewModel: AddBabyViewModel by viewModels()
    private val activityViewModel: MyPageActivityViewModel by activityViewModels()

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

    private fun parseDate(dateString: String): String {
        val inputFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val date = inputFormat.parse(dateString) ?: ""
        return outputFormat.format(date)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, monthOfYear, dayOfMonth ->
                var date = ""
                date += year
                date += String.format("%02d", monthOfYear)
                date += String.format("%02d", dayOfMonth)
                viewModel.birthDay.value = parseDate(date)
                binding.birthView.etInput.setText(parseDate(date))
                Log.e("birth", viewModel.birthDay.value)
            },
            2023,
            1,
            1
        )

        binding.topAppBar.ivBackButton.setOnClickListener {
            requireActivity().finish()
        }

        binding.btnAddBaby.setOnClickListener {
            val name = binding.nameView.etInput.text.toString()
            val relation = binding.relationView.etInput.text.toString()
            val birthDay = binding.birthView.etInput.text.toString()
            viewModel.addBaby(name = name, relationName = relation, birthDay = birthDay)
        }
        binding.birthView.etInput.setOnClickListener {
            datePickerDialog.show()
        }
        collectEvent()
    }

    private fun collectEvent() {
        repeatOnStarted {
            viewModel.eventFlow.collect { event ->
                when (event) {
                    is AddBabyEvent.ShowSnackBar -> {
                        showSnackBar(event.message)
                    }
                    is AddBabyEvent.SuccessAddBaby -> {
                        activityViewModel.moveToCompleteAddBaby()
                    }
                }
            }
        }
    }

    private fun showSnackBar(@StringRes text: Int) {
        Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
    }
}
package kids.baba.mobile.presentation.view.fragment

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.R
import kids.baba.mobile.databinding.FragmentAddbabyBinding
import kids.baba.mobile.presentation.event.AddBabyEvent
import kids.baba.mobile.presentation.extension.repeatOnStarted
import kids.baba.mobile.presentation.viewmodel.AddBabyViewModel
import java.text.SimpleDateFormat
import java.util.*

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
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
        setCalendar()
        collectEvent()
    }

    private fun parseDate(year: Int, monthOfYear: Int, dayOfMonth: Int): String {
        var date = ""
        date += year
        date += String.format("%02d", monthOfYear)
        date += String.format("%02d", dayOfMonth)
        val inputFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dateString = inputFormat.parse(date) ?: ""
        return outputFormat.format(dateString)
    }

    private fun bindViewModel() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun setCalendar() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, monthOfYear, dayOfMonth ->
                binding.birthView.etInput.text = parseDate(year, monthOfYear + 1, dayOfMonth)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        viewModel.datePicker.value = datePickerDialog
    }

    private fun collectEvent() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.eventFlow.collect { event ->
                when (event) {
                    is AddBabyEvent.ShowSnackBar -> showSnackBar(event.message)

                    is AddBabyEvent.NameInputEnd -> {
                        keyboardDown(binding.nameView.etInput)
                        viewScroll(binding.relationView.etInput)
                    }

                    is AddBabyEvent.RelationInputEnd -> {
                        keyboardDown(binding.relationView.etInput)
                        viewScroll(binding.birthView.etInput)
                    }

                    is AddBabyEvent.SuccessAddBaby -> findNavController().navigate(R.id.add_complete_fragment)

                    is AddBabyEvent.BackButtonClicked -> requireActivity().finish()
                }
            }
        }
    }


    private fun showSnackBar(@StringRes text: Int) {
        Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
    }

    private fun keyboardDown(editText: EditText){
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(editText.windowToken, 0)
    }

    private fun viewScroll(targetView: View){
        targetView.requestFocus()
        binding.containerNestedView.post {
            binding.containerNestedView.smoothScrollTo(0, targetView.top)
        }
    }
}
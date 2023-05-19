package kids.baba.mobile.presentation.view.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.R
import kids.baba.mobile.databinding.FragmentAddbabyBinding
import kids.baba.mobile.presentation.view.activity.MyPageActivity
import kids.baba.mobile.presentation.viewmodel.AddBabyViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class AddBabyFragment : Fragment() {

    private var _binding: FragmentAddbabyBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }
    private val viewModel: AddBabyViewModel by viewModels()
    private lateinit var navController: NavController

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
        setNavController()
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
            MyPageActivity.instance.bottomNavOn()
            navController.navigateUp()
        }
        binding.btnAddBaby.setOnClickListener {
            val name = binding.nameView.etInput.text.toString()
            val relation = binding.relationView.etInput.text.toString()
            viewModel.addBaby(name = name, relationName = relation)
            navController.navigate(R.id.action_add_baby_fragment_to_add_complete_fragment)
        }
        binding.birthView.etInput.setOnClickListener {
            datePickerDialog.show()
        }
    }

    private fun setNavController() {
        val navHostFragment =
            requireActivity().supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
        navController = navHostFragment.navController
    }
}
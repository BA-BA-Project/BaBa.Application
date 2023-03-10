package kids.baba.mobile.presentation.view.signup

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import kids.baba.mobile.R
import kids.baba.mobile.databinding.FragmentBirthdaySelectBinding
import kids.baba.mobile.presentation.model.ChatItem
import kids.baba.mobile.presentation.model.UserChatType
import kids.baba.mobile.presentation.state.InputBabiesInfoUiState
import kids.baba.mobile.presentation.viewmodel.InputBabiesInfoViewModel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Calendar


class BirthdaySelectFragment : Fragment() {

    private var _binding: FragmentBirthdaySelectBinding? = null
    private val binding
        get() = checkNotNull(_binding) { "binding was accessed outside of view lifecycle" }

    val viewModel: InputBabiesInfoViewModel by viewModels(
        ownerProducer = {
            var parent = requireParentFragment()
            while (parent is NavHostFragment) {
                parent = parent.requireParentFragment()
            }
            parent
        }
    )

    private lateinit var selectedDate: LocalDate
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy - MM - dd ")

    private var selectedYear = 0
    private var selectedMonth = 0
    private var selectedDay = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBirthdaySelectBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBirthday.apply {
            selectedDate = LocalDate.now()
            val dateText = "${selectedDate.format(dateTimeFormatter)}${getDayOfWeek()}"
            text = dateText
            setOnClickListener {
                showDatePickerDialog()
            }

        }

    }

    private fun showDatePickerDialog() {

        val calendar = Calendar.getInstance()
        val cYear = calendar.get(Calendar.YEAR)
        val cMonth = calendar.get(Calendar.MONTH)
        val cDay = calendar.get(Calendar.DAY_OF_MONTH)


        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, monthOfYear, dayOfMonth ->
                selectedYear = year
                selectedMonth = monthOfYear + 1
                selectedDay = dayOfMonth

                selectedDate = LocalDate.of(selectedYear, selectedMonth, selectedDay)
                val dateText = "${selectedDate.format(dateTimeFormatter)}${getDayOfWeek()}"
                binding.btnBirthday.text = dateText
                when (val uiState = viewModel.uiState.value) {
                    is InputBabiesInfoUiState.InputBabyBirthDay -> {
                        uiState.babyInfo.birthday = selectedDate
                        viewModel.setBabyBirthday(
                            ChatItem.UserChatWithBabyInfoItem(
                                dateText,
                                UserChatType.BABY_BIRTH,
                                uiState.babyInfo,
                                isModifying = false
                            )
                        )
                    }

                    is InputBabiesInfoUiState.ModifyBirthday -> {
                        selectedDate = LocalDate.of(selectedYear, selectedMonth, selectedDay)
                        uiState.babyInfo.birthday = selectedDate
                        viewModel.modifyBabyInfo(
                            ChatItem.UserChatWithBabyInfoItem(
                                dateText,
                                UserChatType.BABY_BIRTH,
                                uiState.babyInfo,
                                isModifying = false
                            ),
                            uiState.position
                        )
                    }

                    else -> Unit
                }
            }, cYear, cMonth, cDay
        )


        val minDate = LocalDate.of(cYear - 2, cMonth, cDay).atStartOfDay(ZoneOffset.UTC).toInstant()
            .toEpochMilli()
        val maxDate = LocalDate.of(cYear + 2, cMonth, cDay).atStartOfDay(ZoneOffset.UTC).toInstant()
            .toEpochMilli()

        datePickerDialog.datePicker.minDate = minDate
        datePickerDialog.datePicker.maxDate = maxDate

        datePickerDialog.show()
    }

    private fun getDayOfWeek() = when (selectedDate.dayOfWeek) {
        DayOfWeek.MONDAY -> getString(R.string.monday)
        DayOfWeek.TUESDAY -> getString(R.string.tuesday)
        DayOfWeek.WEDNESDAY -> getString(R.string.wednesday)
        DayOfWeek.THURSDAY -> getString(R.string.thursday)
        DayOfWeek.FRIDAY -> getString(R.string.friday)
        DayOfWeek.SATURDAY -> getString(R.string.saturday)
        else -> getString(R.string.sunday)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
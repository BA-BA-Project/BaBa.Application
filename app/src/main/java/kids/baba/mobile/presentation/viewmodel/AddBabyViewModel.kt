package kids.baba.mobile.presentation.viewmodel

import android.app.DatePickerDialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.R
import kids.baba.mobile.domain.model.MyBaby
import kids.baba.mobile.domain.model.Result
import kids.baba.mobile.domain.usecase.AddOneMyBabyUseCase
import kids.baba.mobile.presentation.binding.ComposableInputViewData
import kids.baba.mobile.presentation.binding.ComposableInputWithDescViewData
import kids.baba.mobile.presentation.binding.ComposableTopViewData
import kids.baba.mobile.presentation.event.AddBabyEvent
import kids.baba.mobile.presentation.util.flow.MutableEventFlow
import kids.baba.mobile.presentation.util.flow.asEventFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddBabyViewModel @Inject constructor(
    private val addOneMyBabyUseCase: AddOneMyBabyUseCase
) : ViewModel() {

    private val _eventFlow = MutableEventFlow<AddBabyEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    private val birthDay = MutableStateFlow("")
    private val babyName = MutableStateFlow("")
    private val relation = MutableStateFlow("")

    val datePicker = MutableStateFlow<DatePickerDialog?>(null)

    private val _nameFocus = MutableStateFlow(false)
    val nameFocus = _nameFocus.asStateFlow()

    private val _relationFocus = MutableStateFlow(false)
    val relationFocus = _relationFocus.asStateFlow()

    val composableBabyName = ComposableInputViewData(
        text = babyName,
        maxLine = 1,
        maxLength = 6,
        onEditButtonClickEventListener = {
            viewModelScope.launch {
                _eventFlow.emit(AddBabyEvent.NameInputEvent(it))
            }
        }
    )

    val composableRelation = ComposableInputWithDescViewData(
        text = relation,
        onEditButtonClickEventListener = {
            viewModelScope.launch {
                _eventFlow.emit(AddBabyEvent.RelationInputEvent(it))
            }
        }
    )

    val composableBirthDay = ComposableInputViewData(
        text = birthDay,
        onEditButtonClickEventListener = {
            datePicker.value?.show()
        }, maxLine = 1,
        maxLength = 10 // 2023-01-01
    )

    val goToBack = ComposableTopViewData(
        onBackButtonClickEventListener = {
            viewModelScope.launch {
                _eventFlow.emit(AddBabyEvent.BackButtonClicked)
            }
        }
    )


    fun addBaby() {
        viewModelScope.launch {
            when (addOneMyBabyUseCase.add(
                baby = MyBaby(
                    name = babyName.value,
                    relationName = relation.value,
                    birthday = birthDay.value
                )
            )) {
                is Result.Success -> _eventFlow.emit(AddBabyEvent.SuccessAddBaby)
                is Result.NetworkError -> _eventFlow.emit(AddBabyEvent.ShowSnackBar(R.string.baba_network_failed))
                else -> _eventFlow.emit(AddBabyEvent.ShowSnackBar(R.string.already_have_same_name_or_format_error))
            }
        }
    }

    fun nameFocus(hasFocus: Boolean) = _nameFocus.update { hasFocus }

    fun relationFocus(hasFocus: Boolean) = _relationFocus.update { hasFocus }


}
package kids.baba.mobile.presentation.viewmodel

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
import kids.baba.mobile.presentation.model.AddBabyUiModel
import kids.baba.mobile.presentation.util.flow.MutableEventFlow
import kids.baba.mobile.presentation.util.flow.asEventFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddBabyViewModel @Inject constructor(
    private val addOneMyBabyUseCase: AddOneMyBabyUseCase
) : ViewModel() {

    private val _eventFlow = MutableEventFlow<AddBabyEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    val uiModel = MutableStateFlow(AddBabyUiModel())
    val birthDay = MutableStateFlow("")

    private val babyName = MutableStateFlow("")
    private val relation = MutableStateFlow("")


    val composableBabyName = ComposableInputViewData(
        text = babyName,
        onEditButtonClickEventListener = {}
    )

    val composableRelation = ComposableInputWithDescViewData(
        text = relation
    )

    val composableBirthDay = ComposableInputViewData(
        text = birthDay,
        onEditButtonClickEventListener = {}
    )

    val composableBackButton = ComposableTopViewData(
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
                    name = composableBabyName.text.toString(),
                    relationName = composableRelation.text.value,
                    birthday = composableBirthDay.text.toString()
                )
            )) {
                is Result.Success -> _eventFlow.emit(AddBabyEvent.SuccessAddBaby)
                is Result.NetworkError -> _eventFlow.emit(AddBabyEvent.ShowSnackBar(R.string.baba_network_failed))
                else -> _eventFlow.emit(AddBabyEvent.ShowSnackBar(R.string.unknown_error_msg))
            }
        }
    }


}
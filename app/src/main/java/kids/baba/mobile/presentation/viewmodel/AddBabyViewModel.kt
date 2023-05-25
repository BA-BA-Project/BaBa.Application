package kids.baba.mobile.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.R
import kids.baba.mobile.domain.model.MyBaby
import kids.baba.mobile.domain.model.Result
import kids.baba.mobile.domain.usecase.AddOneMyBabyUseCase
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

    fun addBaby(name: String, relationName: String, birthDay: String) = viewModelScope.launch {
        when (val result = addOneMyBabyUseCase.add(
            baby = MyBaby(
                name = name,
                relationName = relationName,
                birthday = birthDay
            )
        )) {
            is Result.Success -> {
                Log.e("AddBabyViewModel", "addBaby: Success")
                _eventFlow.emit(AddBabyEvent.SuccessAddBaby)
            }
            is Result.NetworkError -> {
                _eventFlow.emit(AddBabyEvent.ShowSnackBar(R.string.baba_network_failed))
            }
            else -> {
                _eventFlow.emit(AddBabyEvent.ShowSnackBar(R.string.unknown_error_msg))
            }
        }
    }
}
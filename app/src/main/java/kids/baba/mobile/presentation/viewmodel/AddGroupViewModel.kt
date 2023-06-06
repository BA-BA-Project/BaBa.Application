package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.R
import kids.baba.mobile.domain.model.MyPageGroup
import kids.baba.mobile.domain.model.Result
import kids.baba.mobile.domain.usecase.AddOneGroupUseCase
import kids.baba.mobile.presentation.binding.ComposableInputWithDescViewData
import kids.baba.mobile.presentation.binding.ComposableTopViewData
import kids.baba.mobile.presentation.event.AddGroupEvent
import kids.baba.mobile.presentation.model.ColorModel
import kids.baba.mobile.presentation.util.flow.MutableEventFlow
import kids.baba.mobile.presentation.util.flow.asEventFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddGroupViewModel @Inject constructor(
    private val addOneGroupUseCase: AddOneGroupUseCase
) : ViewModel() {

    val color = MutableStateFlow(ColorModel.PINK.colorCode)
    private val relationGroupName = MutableStateFlow("")

    private val _eventFlow = MutableEventFlow<AddGroupEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    val relationGroupNameViewData = ComposableInputWithDescViewData(
        text = relationGroupName
    )

    val goToBack = ComposableTopViewData(
        onBackButtonClickEventListener = {
            viewModelScope.launch {
                _eventFlow.emit(AddGroupEvent.GoToBack)
            }
        }
    )

    fun addGroupComplete() = viewModelScope.launch {
        when (addOneGroupUseCase.add(
            myPageGroup = MyPageGroup(
                relationGroup = relationGroupName.value,
                groupColor = color.value
            )
        )) {
            is Result.Success -> _eventFlow.emit(AddGroupEvent.SuccessAddGroup)
            is Result.NetworkError -> _eventFlow.emit(AddGroupEvent.ShowSnackBar(R.string.baba_network_failed))
            else -> _eventFlow.emit(AddGroupEvent.ShowSnackBar(R.string.baba_unknown_error))
        }

    }
}
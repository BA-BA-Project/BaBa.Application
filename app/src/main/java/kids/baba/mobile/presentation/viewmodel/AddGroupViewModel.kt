package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.R
import kids.baba.mobile.domain.model.MyPageGroup
import kids.baba.mobile.domain.usecase.AddOneGroupUseCase
import kids.baba.mobile.presentation.event.AddGroupEvent
import kids.baba.mobile.presentation.model.AddGroupUiModel
import kids.baba.mobile.presentation.util.flow.MutableEventFlow
import kids.baba.mobile.presentation.util.flow.asEventFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddGroupViewModel @Inject constructor(
    private val addOneGroupUseCase: AddOneGroupUseCase
) : ViewModel() {
    val uiModel = MutableStateFlow(AddGroupUiModel())
    val color = MutableStateFlow("")

    private val _eventFlow = MutableEventFlow<AddGroupEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    fun addGroup(relationGroup: String, iconColor: String) = viewModelScope.launch {

        when (addOneGroupUseCase.add(
            myPageGroup = MyPageGroup(
                relationGroup = relationGroup,
                iconColor = color.value
            )
        )) {
            is kids.baba.mobile.domain.model.Result.Success -> _eventFlow.emit(AddGroupEvent.SuccessAddGroup)
            is kids.baba.mobile.domain.model.Result.NetworkError -> _eventFlow.emit(AddGroupEvent.ShowSnackBar(R.string.baba_network_failed))
            else -> _eventFlow.emit(AddGroupEvent.ShowSnackBar(R.string.baba_unknown_error))
        }

    }
}
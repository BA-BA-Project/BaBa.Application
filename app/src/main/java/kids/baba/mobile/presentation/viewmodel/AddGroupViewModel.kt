package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.R
import kids.baba.mobile.domain.model.MyPageGroup
import kids.baba.mobile.domain.usecase.AddOneGroupUseCase
import kids.baba.mobile.presentation.binding.ComposableInputWithDescViewData
import kids.baba.mobile.presentation.binding.ComposableTopViewData
import kids.baba.mobile.presentation.event.AddGroupEvent
import kids.baba.mobile.presentation.model.AddGroupUiModel
import kids.baba.mobile.presentation.util.flow.MutableEventFlow
import kids.baba.mobile.presentation.util.flow.asEventFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddGroupViewModel @Inject constructor(
    private val addOneGroupUseCase: AddOneGroupUseCase
) : ViewModel() {
    val uiModel = MutableStateFlow(AddGroupUiModel())
    val color = MutableStateFlow("#81E0D5")

    private val _eventFlow = MutableEventFlow<AddGroupEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    private val relationGroupName = MutableStateFlow("")


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
                iconColor = color.value
            )
        )) {
            is kids.baba.mobile.domain.model.Result.Success -> _eventFlow.emit(AddGroupEvent.SuccessAddGroup)
            is kids.baba.mobile.domain.model.Result.NetworkError -> _eventFlow.emit(AddGroupEvent.ShowSnackBar(R.string.baba_network_failed))
            else -> _eventFlow.emit(AddGroupEvent.ShowSnackBar(R.string.baba_unknown_error))
        }

    }
}
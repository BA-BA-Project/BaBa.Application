package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.presentation.binding.ComposableTopViewData
import kids.baba.mobile.presentation.event.AddBabyEvent
import kids.baba.mobile.presentation.model.AddCompleteUiModel
import kids.baba.mobile.presentation.util.flow.MutableEventFlow
import kids.baba.mobile.presentation.util.flow.asEventFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCompleteViewModel @Inject constructor() : ViewModel() {
    val uiModel = MutableStateFlow(AddCompleteUiModel())

    private val _eventFlow = MutableEventFlow<AddBabyEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    val goToBack = ComposableTopViewData(
        onBackButtonClickEventListener = {
            viewModelScope.launch {
                _eventFlow.emit(AddBabyEvent.BackButtonClicked)
            }
        }
    )

    fun completeAddBaby() {
        viewModelScope.launch {
            _eventFlow.emit(AddBabyEvent.SuccessAddBaby)
        }
    }


}
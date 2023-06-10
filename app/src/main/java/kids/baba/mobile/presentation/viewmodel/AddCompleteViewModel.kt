package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.presentation.binding.ComposableTopViewData
import kids.baba.mobile.presentation.event.AddBabyCompleteEvent
import kids.baba.mobile.presentation.util.flow.MutableEventFlow
import kids.baba.mobile.presentation.util.flow.asEventFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCompleteViewModel @Inject constructor() : ViewModel() {

    private val _eventFlow = MutableEventFlow<AddBabyCompleteEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    val goToBack = ComposableTopViewData(
        onBackButtonClickEventListener = {
            viewModelScope.launch {
                _eventFlow.emit(AddBabyCompleteEvent.BackButtonClicked)
            }
        }
    )

    fun completeAddBaby() {
        viewModelScope.launch {
            _eventFlow.emit(AddBabyCompleteEvent.SuccessAddBaby)
        }
    }


}
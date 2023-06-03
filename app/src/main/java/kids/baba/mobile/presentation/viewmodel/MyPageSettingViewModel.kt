package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.presentation.binding.ComposableTopViewData
import kids.baba.mobile.presentation.event.SettingEvent
import kids.baba.mobile.presentation.util.flow.MutableEventFlow
import kids.baba.mobile.presentation.util.flow.asEventFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageSettingViewModel @Inject constructor(): ViewModel() {
    val title = MutableStateFlow("설정")

    private val _eventFlow = MutableEventFlow<SettingEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    val composableBackButton = ComposableTopViewData(
        onBackButtonClickEventListener = {
            viewModelScope.launch {
                _eventFlow.emit(SettingEvent.BackButtonClicked)
            }
        }
    )

    fun goToServiceInfo() = viewModelScope.launch {
        _eventFlow.emit(SettingEvent.GoToServiceInfo)
    }

    fun goToDeleteMember() = viewModelScope.launch {
        _eventFlow.emit(SettingEvent.GoToDeleteMember)
    }
    fun goToAsk() = viewModelScope.launch {
        _eventFlow.emit(SettingEvent.GoToAsk)
    }
    fun goToCreator() = viewModelScope.launch {
        _eventFlow.emit(SettingEvent.GoToCreator)
    }

}
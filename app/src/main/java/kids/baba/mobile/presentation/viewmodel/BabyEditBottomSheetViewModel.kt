package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.core.utils.EncryptedPrefs
import kids.baba.mobile.presentation.binding.ComposableAddButtonViewData
import kids.baba.mobile.presentation.binding.ComposableNameViewData
import kids.baba.mobile.presentation.event.BabyGroupEditEvent
import kids.baba.mobile.presentation.model.BabyEditBottomSheetUiModel
import kids.baba.mobile.presentation.util.flow.MutableEventFlow
import kids.baba.mobile.presentation.util.flow.asEventFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BabyEditBottomSheetViewModel @Inject constructor() : ViewModel() {
    val uiModel = MutableStateFlow(BabyEditBottomSheetUiModel())

    private val nameViewLiveData: MutableStateFlow<String> =
        if (EncryptedPrefs.getString("babyGroupTitle").isEmpty()) {
            MutableStateFlow("아이들")
        } else {
            MutableStateFlow(EncryptedPrefs.getString("babyGroupTitle"))
        }

    private val _eventFlow = MutableEventFlow<BabyGroupEditEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    val composableNameViewData = ComposableNameViewData(
        initialText = nameViewLiveData.value,
        text = nameViewLiveData,
        onEditButtonClickEventListener = {
            viewModelScope.launch {
                _eventFlow.emit(BabyGroupEditEvent.SuccessBabyGroupEdit(nameViewLiveData.value))
                EncryptedPrefs.putString("babyGroupTitle", nameViewLiveData.value)

            }
        },
        maxLength = 10,
    )

    val goToAddBaby = ComposableAddButtonViewData(
        onAddButtonClickEventListener = {
            viewModelScope.launch {
                _eventFlow.emit(BabyGroupEditEvent.GoToAddBaby)
            }
        }
    )

    val goToInputInviteCode = ComposableAddButtonViewData(
        onAddButtonClickEventListener = {
            viewModelScope.launch {
                _eventFlow.emit(BabyGroupEditEvent.GoToInputInviteCode)
            }
        }
    )


}
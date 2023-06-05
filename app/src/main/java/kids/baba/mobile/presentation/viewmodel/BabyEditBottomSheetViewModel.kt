package kids.baba.mobile.presentation.viewmodel

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.R
import kids.baba.mobile.core.constant.PrefsKey.BABY_GROUP_TITLE_KEY
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
class BabyEditBottomSheetViewModel @Inject constructor(
    resources: Resources
) : ViewModel() {
    val uiModel = MutableStateFlow(BabyEditBottomSheetUiModel())

    private val _eventFlow = MutableEventFlow<BabyGroupEditEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    private val nameViewLiveData: MutableStateFlow<String> =
        if (EncryptedPrefs.getString(BABY_GROUP_TITLE_KEY).isEmpty()) {
            MutableStateFlow(resources.getString(R.string.babys))
        } else {
            MutableStateFlow(EncryptedPrefs.getString(BABY_GROUP_TITLE_KEY))
        }

    val composableNameViewData = ComposableNameViewData(
        initialText = nameViewLiveData.value,
        text = nameViewLiveData,
        onEditButtonClickEventListener = {
            viewModelScope.launch {
                _eventFlow.emit(BabyGroupEditEvent.SuccessBabyGroupEdit(nameViewLiveData.value))
                EncryptedPrefs.putString(BABY_GROUP_TITLE_KEY, nameViewLiveData.value)
            }
        }
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
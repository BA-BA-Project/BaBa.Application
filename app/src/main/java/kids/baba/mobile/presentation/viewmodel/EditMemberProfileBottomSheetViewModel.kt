package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.R
import kids.baba.mobile.domain.model.Profile
import kids.baba.mobile.domain.model.Result
import kids.baba.mobile.domain.usecase.EditProfileUseCase
import kids.baba.mobile.presentation.event.EditMemberProfileEvent
import kids.baba.mobile.presentation.model.EditMemberProfileBottomSheetUiModel
import kids.baba.mobile.presentation.util.flow.MutableEventFlow
import kids.baba.mobile.presentation.util.flow.asEventFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditMemberProfileBottomSheetViewModel @Inject constructor(
    private val editProfileUseCase: EditProfileUseCase
) : ViewModel() {

    private val _eventFlow = MutableEventFlow<EditMemberProfileEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    val uiModel = MutableStateFlow(EditMemberProfileBottomSheetUiModel())

    fun edit(
        profile: Profile
    ) = viewModelScope.launch {
        when (editProfileUseCase.edit(profile = profile)) {
            is Result.Success -> _eventFlow.emit(EditMemberProfileEvent.SuccessEditMemberProfile)

            is Result.NetworkError -> _eventFlow.emit(EditMemberProfileEvent.ShowSnackBar(R.string.baba_network_failed))

            else -> _eventFlow.emit(EditMemberProfileEvent.ShowSnackBar(R.string.unknown_error_msg))

        }
    }
}
package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.R
import kids.baba.mobile.domain.model.Profile
import kids.baba.mobile.domain.model.Result
import kids.baba.mobile.domain.usecase.EditProfileUseCase
import kids.baba.mobile.presentation.binding.ComposableInputViewData
import kids.baba.mobile.presentation.binding.ComposableNameViewData
import kids.baba.mobile.presentation.event.EditMemberProfileEvent
import kids.baba.mobile.presentation.mapper.getUserProfileIconName
import kids.baba.mobile.presentation.model.ColorUiModel
import kids.baba.mobile.presentation.model.EditMemberProfileBottomSheetUiModel
import kids.baba.mobile.presentation.model.UserIconUiModel
import kids.baba.mobile.presentation.util.flow.MutableEventFlow
import kids.baba.mobile.presentation.util.flow.asEventFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditMemberProfileBottomSheetViewModel @Inject constructor(
    private val editProfileUseCase: EditProfileUseCase
) : ViewModel() {

    private val _eventFlow = MutableEventFlow<EditMemberProfileEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    val uiModel = MutableStateFlow(EditMemberProfileBottomSheetUiModel())

    private val _icon = MutableStateFlow("PROFILE_G_1")
    val icon = _icon.asStateFlow()
    private val _color = MutableStateFlow("FF3481FF")
    val color = _color.asStateFlow()

    private val nameViewState: MutableStateFlow<String> = MutableStateFlow("")
    private val introductionViewState: MutableStateFlow<String> = MutableStateFlow("")

    val composableNameViewData = ComposableNameViewData(
        initialText = "",
        text = nameViewState,
        onEditButtonClickEventListener = {}
    )

    val composableIntroductionViewData = ComposableInputViewData(
        text = introductionViewState,
        onEditButtonClickEventListener = {
            viewModelScope.launch {
                when (editProfileUseCase.edit(
                    profile = Profile(
                        name = nameViewState.value,
                        introduction = introductionViewState.value,
                        iconName = icon.value,
                        iconColor = color.value
                    )
                )) {
                    is Result.Success -> {
                        _eventFlow.emit(EditMemberProfileEvent.SuccessEditMemberProfile)
                    }
                    is Result.NetworkError -> {
                        _eventFlow.emit(EditMemberProfileEvent.ShowSnackBar(R.string.baba_network_failed))
                    }
                    else -> {
                        _eventFlow.emit(EditMemberProfileEvent.ShowSnackBar(R.string.unknown_error_msg))
                    }
                }
            }
        },
        maxLength = 20,
        maxLine = 1
    )


    fun setIcon(icon: UserIconUiModel) {
        _icon.value = getUserProfileIconName(icon.userProfileIconUiModel)
    }

    fun setColor(color: ColorUiModel) {
        _color.value = color.value
    }


}
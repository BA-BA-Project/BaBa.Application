package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
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
import kids.baba.mobile.presentation.model.*
import kids.baba.mobile.presentation.util.flow.MutableEventFlow
import kids.baba.mobile.presentation.util.flow.asEventFlow
import kids.baba.mobile.presentation.view.fragment.MyPageFragment.Companion.MEMBER_UI_MODEL
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditMemberProfileBottomSheetViewModel @Inject constructor(
    private val editProfileUseCase: EditProfileUseCase,
    saveStateHandle: SavedStateHandle
) : ViewModel() {

    private val _eventFlow = MutableEventFlow<EditMemberProfileEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    val myInfo = saveStateHandle.get<MemberUiModel>(MEMBER_UI_MODEL)

    val nameViewState: MutableStateFlow<String> = MutableStateFlow(myInfo?.name ?: "")
    val introductionViewState: MutableStateFlow<String> = MutableStateFlow(myInfo?.introduction ?: "")
    val iconState = MutableStateFlow(
        myInfo?.userIconUiModel?.userProfileIconUiModel?.name ?: UserProfileIconUiModel.PROFILE_M_1.name
    )
    val colorState = MutableStateFlow(myInfo?.userIconUiModel?.iconColor ?: ColorModel.PINK.colorCode)

    val composableNameViewData = ComposableNameViewData(
        initialText = myInfo?.name ?: "",
        enabled = false,
        text = nameViewState,
        maxLength = 6
    )

    val composableIntroductionViewData = ComposableInputViewData(
        initialText = myInfo?.introduction ?: "",
        enabled = false,
        text = introductionViewState,
        maxLength = 20,
        maxLine = 2
    )

    fun edit() {
        viewModelScope.launch {
            editMyInfo()
        }
    }

    private suspend fun editMyInfo() {
        when (editProfileUseCase.edit(
            profile = Profile(
                name = nameViewState.value,
                introduction = introductionViewState.value,
                iconName = iconState.value,
                iconColor = colorState.value
            )
        )) {
            is Result.Success -> {
                _eventFlow.emit(EditMemberProfileEvent.SuccessEditMemberProfile)
            }
            is Result.NetworkError -> {
                _eventFlow.emit(EditMemberProfileEvent.ShowSnackBar(R.string.baba_network_failed))
            }
            else -> {
                _eventFlow.emit(EditMemberProfileEvent.ShowSnackBar(R.string.invalid_format_error))
            }
        }
    }


    fun setIcon(icon: UserIconUiModel) {
        iconState.value = getUserProfileIconName(icon.userProfileIconUiModel)
    }

    fun setColor(color: ColorUiModel) {
        colorState.value = color.value
    }


}
package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.domain.model.Profile
import kids.baba.mobile.domain.usecase.EditProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditMemberProfileBottomSheetViewModel @Inject constructor(
    private val editProfileUseCase: EditProfileUseCase
) : ViewModel() {
    val name = MutableStateFlow("본인 이름")
    val intro = MutableStateFlow("내 소개")
    val bgColor = MutableStateFlow("배경 컬러")
    val button = MutableStateFlow("완료")

    fun edit(
        name: String,
        introduction: String,
        iconName: String,
        iconColor: String
    ) = viewModelScope.launch {
        editProfileUseCase.edit(
            profile = Profile(
                name = name,
                introduction = introduction,
                iconName = iconName,
                iconColor = iconColor
            )
        )
    }
}
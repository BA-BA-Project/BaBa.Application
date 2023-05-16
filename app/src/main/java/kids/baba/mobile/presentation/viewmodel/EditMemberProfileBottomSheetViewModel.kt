package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.domain.model.Profile
import kids.baba.mobile.domain.usecase.EditProfileUseCase
import kids.baba.mobile.presentation.model.EditMemberProfileBottomSheetUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditMemberProfileBottomSheetViewModel @Inject constructor(
    private val editProfileUseCase: EditProfileUseCase
) : ViewModel() {
    val uiModel = MutableStateFlow(EditMemberProfileBottomSheetUiModel())
    fun edit(
        profile: Profile
    ) = viewModelScope.launch {
        editProfileUseCase.edit(
            profile = profile
        )
    }
}
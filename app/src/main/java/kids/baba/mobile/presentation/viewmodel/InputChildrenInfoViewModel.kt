package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.presentation.model.UserProfile
import javax.inject.Inject

@HiltViewModel
class InputChildrenInfoViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val userProfile = savedStateHandle.get<UserProfile>(KEY_USER_PROFILE)

    companion object {
        const val KEY_USER_PROFILE = "userProfile"
    }

}
package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.presentation.model.DeleteMemberUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class DeleteMemberViewModel @Inject constructor() : ViewModel() {
    val uiModel = MutableStateFlow(DeleteMemberUiModel())
}
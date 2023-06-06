package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.presentation.model.DeleteReason
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class DeleteMemberViewModel @Inject constructor() : ViewModel() {
    private val _deleteReasonList = MutableStateFlow<List<DeleteReason>>(emptyList())
    val deleteReasonList = _deleteReasonList.asStateFlow()
}
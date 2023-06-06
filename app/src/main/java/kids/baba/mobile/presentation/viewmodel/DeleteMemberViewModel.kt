package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.presentation.model.DeleteReason
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class DeleteMemberViewModel @Inject constructor() : ViewModel() {
    private val _deleteReasonList = MutableStateFlow<List<DeleteReason>>(emptyList())
    val deleteReasonList = _deleteReasonList.asStateFlow()


    fun itemClick(deleteReason: DeleteReason){
        _deleteReasonList.update { reasonList ->
            reasonList.map {
                if(deleteReason == it){
                    deleteReason.copy(isChecked = deleteReason.isChecked.not())
                } else {
                    it
                }
            }
        }
    }
}
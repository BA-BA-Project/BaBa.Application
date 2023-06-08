package kids.baba.mobile.presentation.viewmodel

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.R
import kids.baba.mobile.presentation.model.DeleteReason
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class DeleteMemberViewModel @Inject constructor(
    resources: Resources
) : ViewModel() {

    private val _deleteReasonList = MutableStateFlow(
        resources.getStringArray(R.array.account_delete_reason).map { DeleteReason(reason = it) }
    )
    val deleteReasonList = _deleteReasonList.asStateFlow()

    private val _isAnyChecked = MutableStateFlow(false)
    val isAnyChecked = _isAnyChecked.asStateFlow()


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
        checkAnyChecked()
    }

    private fun checkAnyChecked() = _isAnyChecked.update { deleteReasonList.value.any { it.isChecked } }





}
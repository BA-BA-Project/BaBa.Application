package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.domain.model.GroupInfo
import kids.baba.mobile.domain.usecase.DeleteOneGroupUseCase
import kids.baba.mobile.domain.usecase.PatchOneGroupUseCase
import kids.baba.mobile.presentation.event.EditGroupSheetEvent
import kids.baba.mobile.presentation.model.EditGroupBottomSheetUiModel
import kids.baba.mobile.presentation.util.flow.MutableEventFlow
import kids.baba.mobile.presentation.view.FunctionHolder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditGroupBottomSheetViewModel @Inject constructor(
    private val patchOneGroupUseCase: PatchOneGroupUseCase,
    private val deleteOneGroupUseCase: DeleteOneGroupUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val uiModel = MutableStateFlow(EditGroupBottomSheetUiModel())
    val groupName = MutableStateFlow<String?>(savedStateHandle["groupName"])
    var itemClick = {}
    var getText: () -> String = { "" }
    var dismiss: () -> Unit = {}
    val color = MutableStateFlow("")

    private val isFamily = MutableStateFlow<Boolean?>(savedStateHandle["family"])
    private val query = MutableStateFlow("")
    private val _event = MutableEventFlow<EditGroupSheetEvent>()
    val event = _event

    init {
        query.value = groupName.value ?: ""
        uiModel.value.permissionDesc = if (isFamily.value == true) "있음" else "없음"
    }

    val patch = object : FunctionHolder {
        override fun click() {
            viewModelScope.launch {
                if (getText() == "") return@launch
                patchOneGroupUseCase.patch(group = GroupInfo(relationGroup = getText()), groupName = query.value)
                itemClick()
                dismiss()
            }
        }
    }

    val delete = object : FunctionHolder {
        override fun click() {
            viewModelScope.launch {
                deleteOneGroupUseCase.delete(groupName = query.value)
                itemClick()
                dismiss()
            }
        }
    }

    val goToAddMemberPage = object : FunctionHolder {
        override fun click() {
            viewModelScope.launch {
                _event.emit(EditGroupSheetEvent.GoToAddMemberPage)
            }
        }
    }
}
package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.domain.model.GroupInfo
import kids.baba.mobile.domain.usecase.DeleteOneGroupUseCase
import kids.baba.mobile.domain.usecase.PatchOneGroupUseCase
import kids.baba.mobile.presentation.model.EditGroupBottomSheetUiModel
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
    private val isFamily = MutableStateFlow<Boolean?>(savedStateHandle["family"])
    private val query = MutableStateFlow("")
    val patchGroup = MutableStateFlow {}

    init {
        query.value = groupName.value ?: ""
        uiModel.value.permissionDesc = if (isFamily.value == true) "있음" else "없음"
    }

    fun patch(name: String) = viewModelScope.launch {
        patchOneGroupUseCase.patch(group = GroupInfo(relationGroup = name), groupName = query.value)
    }

    fun delete() = viewModelScope.launch {
        deleteOneGroupUseCase.delete(groupName = query.value)
    }
}
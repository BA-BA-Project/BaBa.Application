package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.domain.model.MyPageGroup
import kids.baba.mobile.domain.usecase.AddOneGroupUseCase
import kids.baba.mobile.presentation.model.AddGroupUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddGroupViewModel @Inject constructor(
    private val addOneGroupUseCase: AddOneGroupUseCase
) : ViewModel() {
    val uiModel = MutableStateFlow(AddGroupUiModel())

    fun addGroup(relationGroup: String, iconColor: String) = viewModelScope.launch {
        addOneGroupUseCase.add(
            myPageGroup = MyPageGroup(
                relationGroup = relationGroup,
                iconColor = iconColor
            )
        )
    }
}
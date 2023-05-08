package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.domain.usecase.EditBabyNameUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BabyEditProfileBottomSheetViewModel @Inject constructor(
    private val editBabyNameUseCase: EditBabyNameUseCase
) : ViewModel() {
    val name = MutableStateFlow("아이 이름")
    val button = MutableStateFlow("편집")

    fun edit(babyId: String, name: String) = viewModelScope.launch {
        editBabyNameUseCase.edit(babyId, name)
    }
}
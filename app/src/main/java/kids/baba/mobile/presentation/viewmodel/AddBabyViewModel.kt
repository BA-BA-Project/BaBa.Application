package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.domain.model.MyBaby
import kids.baba.mobile.domain.usecase.AddOneMyBabyUseCase
import kids.baba.mobile.presentation.model.AddBabyUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddBabyViewModel @Inject constructor(
    private val addOneMyBabyUseCase: AddOneMyBabyUseCase
) : ViewModel() {
    val uiModel = MutableStateFlow(AddBabyUiModel())
    val birthDay = MutableStateFlow("")
    fun addBaby(name: String, relationName: String) = viewModelScope.launch {
        addOneMyBabyUseCase.add(
            baby = MyBaby(
                name = name,
                relationName = relationName,
                birthday = birthDay.value
            )
        )
    }
}
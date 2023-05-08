package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.domain.usecase.GetBabyProfileUseCase
import kids.baba.mobile.presentation.state.BabyDetailUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BabyDetailViewModel @Inject constructor(
    private val getBabyProfileUseCase: GetBabyProfileUseCase
) : ViewModel() {
    val babyName = MutableStateFlow("손제인")
    val babyBirthday = MutableStateFlow("2023.4.10")
    val familyGroupTitle = MutableStateFlow("가족 그룹 이름")
    val familyGroupDesc = MutableStateFlow("모든 그룹과 소식을 공유할 수 있어요")
    val myGroupTitle = MutableStateFlow("내가 속한 그룹 이름")
    val myGroupDesc = MutableStateFlow("[가족 그룹], [내가 속한 그룹]의 소식만 볼 수 있어요")

    private val _uiState = MutableStateFlow<BabyDetailUiState>(BabyDetailUiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun load(babyId: String) = viewModelScope.launch {
        getBabyProfileUseCase.get(babyId).catch {

        }.collect {
            _uiState.value = BabyDetailUiState.Success(it)
        }
    }
}
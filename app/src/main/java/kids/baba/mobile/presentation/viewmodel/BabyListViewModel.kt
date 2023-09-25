package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.R
import kids.baba.mobile.domain.model.ApiResult
import kids.baba.mobile.domain.usecase.GetBabiesUseCase
import kids.baba.mobile.presentation.event.BabyListEvent
import kids.baba.mobile.presentation.mapper.toPresentation
import kids.baba.mobile.presentation.model.BabyUiModel
import kids.baba.mobile.presentation.util.flow.MutableEventFlow
import kids.baba.mobile.presentation.util.flow.asEventFlow
import kids.baba.mobile.presentation.view.bottomsheet.BabyListBottomSheet.Companion.SELECTED_BABY_ID_KEY
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BabyListViewModel @Inject constructor(
    private val getBabiesUseCase: GetBabiesUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _babyList = MutableStateFlow<List<BabyUiModel>>(emptyList())
    val babyList = _babyList.asStateFlow()

    private val _eventFlow = MutableEventFlow<BabyListEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    init {
        getBabies()
    }

    private fun getBabies() {
        val selectedBabyId: String? = savedStateHandle[SELECTED_BABY_ID_KEY]
        viewModelScope.launch {
            when (val result = getBabiesUseCase()) {
                is ApiResult.Success -> {
                    val babies = result.data.toPresentation()
                    _babyList.value = babies.myBaby + babies.othersBaby

                    if (selectedBabyId == null) {
                        _babyList.value.mapIndexed { idx, baby ->
                            if (idx == 0) {
                                baby.copy(selected = true)
                            } else {
                                baby
                            }
                        }
                    } else {
                        _babyList.value = _babyList.value.map {
                            if (it.babyId == selectedBabyId) {
                                it.copy(selected = true)
                            } else {
                                it
                            }
                        }
                    }
                }

                is ApiResult.NetworkError -> _eventFlow.emit(BabyListEvent.ShowSnackBar(R.string.baba_network_failed))
                else -> _eventFlow.emit(BabyListEvent.ShowSnackBar(R.string.baba_get_babies_failed))
            }
        }
    }

    fun moveBabyManagement() = viewModelScope.launch {
        _eventFlow.emit(BabyListEvent.MoveBabyManagement)
    }

}
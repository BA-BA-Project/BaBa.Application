package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.presentation.model.BabyUiModel
import kids.baba.mobile.presentation.view.BabyListBottomSheet.Companion.SELECTED_BABY_KEY
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class BabyListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _babyList = MutableStateFlow<List<BabyUiModel>>(emptyList())
    val babyList = _babyList.asStateFlow()
    init {
        getBabies()
    }

    private fun getBabies() {
        val selectedBaby: BabyUiModel? = savedStateHandle[SELECTED_BABY_KEY]
        _babyList.value = List(100) {idx ->
            BabyUiModel(babyId = "$idx", groupColor = "#008000", name = "앙쥬${idx}", false )
        }
        if (selectedBaby == null) {
            _babyList.value.mapIndexed{idx, baby ->
                if(idx == 0){
                    baby.copy(selected = true)
                } else{
                    baby
                }
            }
        } else {
            _babyList.value = _babyList.value.map {
                if (it.babyId == selectedBaby.babyId) {
                    it.copy(selected = true)
                } else {
                    it
                }
            }
        }

    }

}
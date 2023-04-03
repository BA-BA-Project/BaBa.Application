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

    private val _myBabyList = MutableStateFlow<List<BabyUiModel>>(emptyList())
    val myBabyList = _myBabyList.asStateFlow()

    private val _othersBabyList = MutableStateFlow<List<BabyUiModel>>(emptyList())
    val othersBabyList = _othersBabyList.asStateFlow()


    init {
        getBabies()
    }

    private fun getBabies() {
        val selectedBaby: BabyUiModel? = savedStateHandle[SELECTED_BABY_KEY]
        _myBabyList.value = listOf(
            BabyUiModel(babyId = "1", groupColor = "#FF0000", name = "앙쥬1", false),
            BabyUiModel(babyId = "2", groupColor = "#FF8C00", name = "앙쥬2", false),
            BabyUiModel(babyId = "3", groupColor = "#FFFF00", name = "앙쥬3", false),
        )
        _othersBabyList.value = listOf(
            BabyUiModel(babyId = "4", groupColor = "#008000", name = "앙쥬4", false),
            BabyUiModel(babyId = "5", groupColor = "#0000FF", name = "앙쥬5", false),
            BabyUiModel(babyId = "6", groupColor = "#4B0082", name = "앙쥬6", false),
            BabyUiModel(babyId = "7", groupColor = "#800080", name = "앙쥬7", false),
        )
        if (selectedBaby == null) {
            _myBabyList.value.map{}
        } else {
            _myBabyList.value = _myBabyList.value.map {
                if (it.babyId == selectedBaby.babyId) {
                    it.copy(selected = true)
                } else {
                    it
                }
            }

            _othersBabyList.value = _othersBabyList.value.map {
                if (it.babyId == selectedBaby.babyId) {
                    it.copy(selected = true)
                } else {
                    it
                }
            }
        }

    }

}
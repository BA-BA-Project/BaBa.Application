package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.presentation.model.BabyUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class BabyListViewModel @Inject constructor() : ViewModel() {

    private val _myBabyList = MutableStateFlow<List<BabyUiModel>>(emptyList())
    val myBabyList = _myBabyList.asStateFlow()

    private val _othersBabyList = MutableStateFlow<List<BabyUiModel>>(emptyList())
    val othersBabyList = _othersBabyList.asStateFlow()

    private val _selectedBaby = MutableStateFlow<BabyUiModel?>(null)
    val selectedBaby = _selectedBaby.asStateFlow()

    init {
        getBabies()
    }

    private fun getBabies() {
        _myBabyList.value = listOf(
            BabyUiModel(babyId = "1", groupColor = "#FF0000", name = "앙쥬1", true),
            BabyUiModel(babyId = "2", groupColor = "#FF8C00", name = "앙쥬2", true),
            BabyUiModel(babyId = "3", groupColor = "#FFFF00", name = "앙쥬3", true),
        )
        _othersBabyList.value = listOf(
            BabyUiModel(babyId = "4", groupColor = "#008000", name = "앙쥬4", true),
            BabyUiModel(babyId = "5", groupColor = "#0000FF", name = "앙쥬5", true),
            BabyUiModel(babyId = "6", groupColor = "#4B0082", name = "앙쥬6", true),
            BabyUiModel(babyId = "7", groupColor = "#800080", name = "앙쥬7", true),
        )
        _selectedBaby.value = _myBabyList.value.first()
    }

}
package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.presentation.model.BabyEditBottomSheetUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class BabyEditBottomSheetViewModel @Inject constructor() : ViewModel() {
    val uiModel = MutableStateFlow(BabyEditBottomSheetUiModel())
}
package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.presentation.model.AddCompleteUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class AddCompleteViewModel @Inject constructor() : ViewModel() {
    val uiModel = MutableStateFlow(AddCompleteUiModel())
}
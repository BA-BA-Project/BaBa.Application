package kids.baba.mobile.presentation.viewmodel

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.presentation.model.BabyEditBottomSheetUiModel
import kids.baba.mobile.presentation.view.FunctionHolder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BabyEditBottomSheetViewModel @Inject constructor() : ViewModel() {
    val uiModel = MutableStateFlow(BabyEditBottomSheetUiModel())
    var itemClick: (String) -> Unit = {}
    var dismiss: () -> Unit = {}
    var getText: () -> String = {""}

    val editBabyGroupName = object : FunctionHolder{
        override fun click() {
            viewModelScope.launch {
                itemClick(getText())
                dismiss()
            }
        }
    }
}
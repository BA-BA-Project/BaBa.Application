package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class BabyEditProfileBottomSheetViewModel @Inject constructor() : ViewModel() {
    val name = MutableStateFlow("아이 이름")
    val button = MutableStateFlow("편집")
}
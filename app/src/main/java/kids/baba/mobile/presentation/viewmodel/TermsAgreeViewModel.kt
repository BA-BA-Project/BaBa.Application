package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.presentation.model.TermsData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class TermsAgreeViewModel @Inject constructor() : ViewModel() {
    private val _isAllChecked = MutableStateFlow(false)
    val isAllChecked = _isAllChecked.asStateFlow()

    private val _termsList = MutableStateFlow(
        listOf(
            TermsData(true, "이용약관 동의", false,"https://sites.google.com/view/baba-agree/%EC%9D%B4%EC%9A%A9%EC%95%BD%EA%B4%80?authuser=4"),
            TermsData(true, "개인정보 수집 및 이용 동의", false, "https://sites.google.com/view/baba-agree/%EA%B0%9C%EC%9D%B8%EC%A0%95%EB%B3%B4%EC%B2%98%EB%A6%AC%EB%B0%A9%EC%B9%A8?authuser=4")
        )
    )
    val termsList = _termsList.asStateFlow()


    fun changeTermsAgree(termsData: TermsData, checked: Boolean) {
        var checkedCnt = 0
        _termsList.value = _termsList.value.map {
            if (it.content == termsData.content) {
                val newTermsData = it.copy(isChecked = checked)
                if (newTermsData.isChecked) checkedCnt += 1
                newTermsData
            } else {
                if (it.isChecked) checkedCnt += 1
                it
            }
        }
        _isAllChecked.value = checkedCnt == _termsList.value.size
    }

    fun isAllAgreeChecked() {
        _isAllChecked.value = _isAllChecked.value.not()
        _termsList.value = _termsList.value.map { it.copy(isChecked = _isAllChecked.value) }
    }
}
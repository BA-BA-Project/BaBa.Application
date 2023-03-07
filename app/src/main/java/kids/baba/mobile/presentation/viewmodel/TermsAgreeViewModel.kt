package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.R
import kids.baba.mobile.domain.usecase.GetTermsListUseCase
import kids.baba.mobile.presentation.event.TermsAgreeEvent
import kids.baba.mobile.presentation.mapper.toPresentation
import kids.baba.mobile.presentation.model.TermsUiModel
import kids.baba.mobile.presentation.util.flow.MutableEventFlow
import kids.baba.mobile.presentation.util.flow.asEventFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TermsAgreeViewModel @Inject constructor(
    private val getTermsListUseCase: GetTermsListUseCase
) : ViewModel() {
    private val _isAllChecked = MutableStateFlow(false)
    val isAllChecked = _isAllChecked.asStateFlow()

    private val tempTermsList =
        listOf(
            TermsUiModel(
                true,
                "이용약관 동의",
                false,
                "https://sites.google.com/view/baba-agree/%EC%9D%B4%EC%9A%A9%EC%95%BD%EA%B4%80?authuser=4"
            ),
            TermsUiModel(
                true,
                "개인정보 수집 및 이용 동의",
                false,
                "https://sites.google.com/view/baba-agree/%EA%B0%9C%EC%9D%B8%EC%A0%95%EB%B3%B4%EC%B2%98%EB%A6%AC%EB%B0%A9%EC%B9%A8?authuser=4"
            )
        )

    private val _termsList = MutableStateFlow(tempTermsList)
    val termsList = _termsList.asStateFlow()

    private val _eventFlow = MutableEventFlow<TermsAgreeEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    init {
        viewModelScope.launch {
            getTermsListUseCase("").onSuccess { termsList ->
                _termsList.value = termsList.map { it.toPresentation() }
            }.onFailure {
                _eventFlow.emit(TermsAgreeEvent.ShowSnackBar(R.string.baba_terms_loading_failed))
            }
        }
    }

    fun changeTermsAgree(termsData: TermsUiModel, checked: Boolean) {
        var checkedCnt = 0
        _termsList.value = _termsList.value.map {
            if (it.name == termsData.name) {
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
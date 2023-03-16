package kids.baba.mobile.presentation.viewmodel

import android.accounts.NetworkErrorException
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.R
import kids.baba.mobile.domain.usecase.GetSignTokenUseCase
import kids.baba.mobile.domain.usecase.GetTermsListUseCase
import kids.baba.mobile.presentation.event.TermsAgreeEvent
import kids.baba.mobile.presentation.mapper.toDomain
import kids.baba.mobile.presentation.model.TermsUiModel
import kids.baba.mobile.presentation.util.flow.MutableEventFlow
import kids.baba.mobile.presentation.util.flow.asEventFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TermsAgreeViewModel @Inject constructor(
    private val getTermsListUseCase: GetTermsListUseCase,
    private val getSignTokenUseCase: GetSignTokenUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _isAllChecked = MutableStateFlow(false)
    val isAllChecked = _isAllChecked.asStateFlow()

    private val _signToken = MutableStateFlow("")
    val signToken = _signToken.asStateFlow()

    private val socialToken = savedStateHandle[KEY_SOCIAL_TOKEN] ?: ""

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
            getTermsListUseCase(socialToken).catch {
                if (it is NetworkErrorException) {
                    _eventFlow.emit(TermsAgreeEvent.ShowSnackBar(R.string.baba_network_failed))
                } else {
                    _eventFlow.emit(TermsAgreeEvent.ShowSnackBar(R.string.baba_terms_loading_failed))
                }
            }.collect {
                _termsList.value = it
            }
        }
    }

    fun changeTermsAgree(termsData: TermsUiModel, checked: Boolean) {
        var checkedCnt = 0
        _termsList.value = _termsList.value.map {
            if (it.name == termsData.name) {
                val newTermsData = it.copy(selected = checked)
                if (newTermsData.selected) checkedCnt += 1
                newTermsData
            } else {
                if (it.selected) checkedCnt += 1
                it
            }
        }
        _isAllChecked.value = checkedCnt == _termsList.value.size
    }

    fun isAllAgreeChecked() {
        _isAllChecked.value = _isAllChecked.value.not()
        _termsList.value = _termsList.value.map { it.copy(selected = _isAllChecked.value) }
    }

    fun getSignToken() {
        viewModelScope.launch {
             getSignTokenUseCase(
                socialToken = socialToken,
                termsData = termsList.value.map {
                    it.toDomain()
                }
            ).catch {
                if(it is NetworkErrorException){
                    _eventFlow.emit(TermsAgreeEvent.ShowSnackBar(R.string.baba_network_failed))
                } else {
                    _signToken.emit("testSignToken") //임시로 사용할 signToken api구현이 완료되면 수정하기
//                _eventFlow.emit(TermsAgreeEvent.ShowSnackBar(R.string.baba_terms_agree_failed))
                }
               }.collect {
                 _signToken.emit(it)
            }
        }
    }

    companion object {
        const val KEY_SOCIAL_TOKEN = "socialToken"
    }
}
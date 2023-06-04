package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.R
import kids.baba.mobile.core.utils.EncryptedPrefs
import kids.baba.mobile.domain.model.Result
import kids.baba.mobile.domain.model.getThrowableOrNull
import kids.baba.mobile.domain.usecase.GetBabiesUseCase
import kids.baba.mobile.domain.usecase.GetMemberUseCase
import kids.baba.mobile.domain.usecase.GetMyPageGroupUseCase
import kids.baba.mobile.presentation.event.MyPageEvent
import kids.baba.mobile.presentation.mapper.toPresentation
import kids.baba.mobile.presentation.util.flow.MutableEventFlow
import kids.baba.mobile.presentation.util.flow.asEventFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val getMyPageGroupUseCase: GetMyPageGroupUseCase,
    private val getBabiesUseCase: GetBabiesUseCase,
    private val getMemberUseCase: GetMemberUseCase
) : ViewModel() {
    val groupAddButton = MutableStateFlow("+ 그룹만들기")

    private val _eventFlow = MutableEventFlow<MyPageEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    private val _babyGroupTitle = MutableStateFlow(
        EncryptedPrefs.getString("babyGroupTitle").ifEmpty {
            "아이들"
        }
    )
    val babyGroupTitle = _babyGroupTitle.asStateFlow()

    fun loadGroups() = viewModelScope.launch {
        when (val result = getMyPageGroupUseCase.get()) {
            is Result.Success -> {
                _eventFlow.emit(MyPageEvent.LoadMember(result.data.groups))
            }
            is Result.NetworkError -> {
                _eventFlow.emit(MyPageEvent.ShowSnackBar(R.string.baba_network_failed))
            }
            else -> {
                _eventFlow.emit(MyPageEvent.ShowSnackBar(R.string.unknown_error_msg))
            }
        }
    }

    fun loadBabies() = viewModelScope.launch {
        when (val result = getBabiesUseCase()) {
            is Result.Success -> {
                val babies = result.data
                _eventFlow.emit(MyPageEvent.LoadBabies(babies.myBaby + babies.others))
            }
            is Result.NetworkError -> {
                _eventFlow.emit(MyPageEvent.Error(result.throwable))
            }
            else -> {
                _eventFlow.emit(MyPageEvent.Error(Throwable("error")))
            }
        }
    }

    fun getMyInfo() = viewModelScope.launch {
        when (val result = getMemberUseCase.getMeNoPref()) {
            is Result.Success -> {
                _eventFlow.emit(MyPageEvent.LoadMyInfo(result.data.toPresentation()))
            }
            is Result.NetworkError -> {
                _eventFlow.emit(MyPageEvent.Error(result.throwable))
            }
            else -> {
                val throwable = result.getThrowableOrNull()
                if (throwable != null) {
                    _eventFlow.emit(MyPageEvent.Error(throwable))
                }
            }
        }
    }

    fun addGroup() = viewModelScope.launch {
        _eventFlow.emit(MyPageEvent.AddGroup)
    }

    fun onClickSetting() = viewModelScope.launch {
        _eventFlow.emit(MyPageEvent.Setting)
    }

    fun refreshBabyGroupTitle(babyGroupTitle: String) = viewModelScope.launch {
        _babyGroupTitle.value = babyGroupTitle
    }
}
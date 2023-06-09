package kids.baba.mobile.presentation.viewmodel

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.R
import kids.baba.mobile.core.constant.PrefsKey.BABY_GROUP_TITLE_KEY
import kids.baba.mobile.core.utils.EncryptedPrefs
import kids.baba.mobile.domain.model.Result
import kids.baba.mobile.domain.model.getThrowableOrNull
import kids.baba.mobile.domain.usecase.GetBabiesUseCase
import kids.baba.mobile.domain.usecase.GetMemberUseCase
import kids.baba.mobile.domain.usecase.GetMyPageGroupUseCase
import kids.baba.mobile.presentation.event.MyPageEvent
import kids.baba.mobile.presentation.mapper.toPresentation
import kids.baba.mobile.presentation.mapper.toUiModel
import kids.baba.mobile.presentation.model.MemberUiModel
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
    private val getMemberUseCase: GetMemberUseCase,
    resources: Resources
) : ViewModel() {

    private val _eventFlow = MutableEventFlow<MyPageEvent>()
    val eventFlow = _eventFlow.asEventFlow()

    private val _babyGroupTitle = MutableStateFlow(
        EncryptedPrefs.getString(BABY_GROUP_TITLE_KEY).ifEmpty {
            resources.getString(R.string.babys)
        }
    )
    val babyGroupTitle = _babyGroupTitle.asStateFlow()

    private val _myInfoState = MutableStateFlow<MemberUiModel?>(null)
    private val myInfoState = _myInfoState.asStateFlow()

    fun loadGroups() = viewModelScope.launch {
        when (val result = getMyPageGroupUseCase.get()) {
            is Result.Success -> {
                _eventFlow.emit(MyPageEvent.LoadGroups(result.data.groups.toUiModel()))
            }
            is Result.NetworkError -> {
                _eventFlow.emit(MyPageEvent.ShowSnackBar(R.string.baba_network_failed))
            }
            else -> {
                _eventFlow.emit(MyPageEvent.ShowSnackBar(R.string.load_group_error_message))
            }
        }
    }

    fun loadBabies() = viewModelScope.launch {
        when (val result = getBabiesUseCase()) {
            is Result.Success -> {
                val babies = result.data
                _eventFlow.emit(MyPageEvent.LoadBabies((babies.myBaby + babies.others).map { it.toPresentation() }))
            }
            is Result.NetworkError -> _eventFlow.emit(MyPageEvent.ShowSnackBar(R.string.baba_network_failed))

            else -> _eventFlow.emit(MyPageEvent.ShowSnackBar(R.string.load_baby_error_message))

        }
    }

    fun getMyInfo() = viewModelScope.launch {
        when (val result = getMemberUseCase.getMeNoPref()) {
            is Result.Success -> {
                _myInfoState.value = result.data.toPresentation()
                _eventFlow.emit(MyPageEvent.LoadMyInfo(result.data.toPresentation()))
            }
            is Result.NetworkError -> _eventFlow.emit(MyPageEvent.ShowSnackBar(R.string.baba_network_failed))

            else -> {
                val throwable = result.getThrowableOrNull()
                if (throwable != null) {
                    _eventFlow.emit(MyPageEvent.ShowSnackBar(R.string.load_my_info_error_message))
                }
            }
        }
    }

    fun showEditMemberBottomSheet() = viewModelScope.launch {
        _eventFlow.emit(MyPageEvent.ShowEditMemberBottomSheet(myInfoState.value ?: return@launch))
    }

    fun showEditBabyGroupBottomSheet() = viewModelScope.launch {
        _eventFlow.emit(MyPageEvent.ShowEditBabyGroupBottomSheet)
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
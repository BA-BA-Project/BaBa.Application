package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.domain.usecase.GetOneAlbumUseCase
import kids.baba.mobile.domain.usecase.GetOneBabyUseCase
import kids.baba.mobile.presentation.state.GrowthAlbumState
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GrowthAlbumViewModel @Inject constructor(
    private val getOneAlbumUseCase: GetOneAlbumUseCase,
    private val getOneBabyUseCase: GetOneBabyUseCase
) : ViewModel() {

    private val _growthAlbumState =
        MutableStateFlow<GrowthAlbumState>(GrowthAlbumState.UnInitialized)
    val growthAlbumState = _growthAlbumState.asStateFlow()

    private val _motionLayoutTransition = MutableLiveData<Unit>()
    val motionLayoutTransition = _motionLayoutTransition

    val pickDate = MutableLiveData<Unit>()
    fun loadAlbum(id: Int) = viewModelScope.launch {
        _growthAlbumState.value = GrowthAlbumState.Loading
        getOneAlbumUseCase.getOneAlbum(id).catch {
            _growthAlbumState.value = GrowthAlbumState.Error(it)
        }.collect {
            _growthAlbumState.value = GrowthAlbumState.SuccessAlbum(it.album)
        }
    }

    fun loadBaby() = viewModelScope.launch {
        _growthAlbumState.value = GrowthAlbumState.Loading
        getOneBabyUseCase.getOneBaby().catch {
            _growthAlbumState.value = GrowthAlbumState.Error(it)
        }.collect {
            _growthAlbumState.value = GrowthAlbumState.SuccessBaby(it.myBaby)
            _growthAlbumState.value = GrowthAlbumState.SuccessBaby(it.others)
        }
    }

    fun changeBaby() {
        _motionLayoutTransition.value = Unit
    }

    fun pickDate(){
        pickDate.value = Unit
    }
}
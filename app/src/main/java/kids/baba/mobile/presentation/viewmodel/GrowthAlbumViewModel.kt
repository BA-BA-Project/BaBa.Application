package kids.baba.mobile.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.domain.usecase.GetAlbumsFromBabyIdUseCase
import kids.baba.mobile.domain.usecase.GetOneBabyUseCase
import kids.baba.mobile.presentation.state.GrowthAlbumState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GrowthAlbumViewModel @Inject constructor(
    private val getAlbumsFromBabyIdUseCase: GetAlbumsFromBabyIdUseCase,
    private val getOneBabyUseCase: GetOneBabyUseCase
) : ViewModel() {
    private val _growthAlbumState =
        MutableStateFlow<GrowthAlbumState>(GrowthAlbumState.UnInitialized)
    val growthAlbumState = _growthAlbumState.asStateFlow()

    fun loadAlbum(id: String, year: Int, month: Int, token: String) = viewModelScope.launch {
        _growthAlbumState.value = GrowthAlbumState.Loading
        getAlbumsFromBabyIdUseCase.getOneAlbum(id, year, month, token).catch {
            _growthAlbumState.value = GrowthAlbumState.Error(it)
        }.collect {
            Log.e("123", "${it}")
            _growthAlbumState.value = GrowthAlbumState.SuccessAlbum(it.album)
        }
    }

    fun loadBaby(token: String) = viewModelScope.launch {
        _growthAlbumState.value = GrowthAlbumState.Loading
        getOneBabyUseCase.getOneBaby(token).catch {
            _growthAlbumState.value = GrowthAlbumState.Error(it)
        }.collect {
            _growthAlbumState.value = GrowthAlbumState.SuccessBaby(it.myBaby)
            _growthAlbumState.value = GrowthAlbumState.SuccessBaby(it.others)
        }
    }
}
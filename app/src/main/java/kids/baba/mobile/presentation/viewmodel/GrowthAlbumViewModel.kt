package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.domain.usecase.GetOneAlbumUseCase
import kids.baba.mobile.domain.usecase.GetOneBabyUseCase
import kids.baba.mobile.presentation.state.GrowthAlbumState
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GrowthAlbumViewModel @Inject constructor(
    private val getOneAlbumUseCase: GetOneAlbumUseCase,
    private val getOneBabyUseCase: GetOneBabyUseCase
) : ViewModel() {
    private val _growthAlbumState = MutableStateFlow<GrowthAlbumState>(GrowthAlbumState.UnInitialized)
    val growthAlbumState = _growthAlbumState.asStateFlow()

    fun loadAlbum(id: Int) = viewModelScope.async {
        _growthAlbumState.value = GrowthAlbumState.Loading
        _growthAlbumState.value = GrowthAlbumState.Success
        getOneAlbumUseCase.getOneAlbum(id)
    }

    fun loadBaby() = viewModelScope.async {
        _growthAlbumState.value = GrowthAlbumState.Loading
        _growthAlbumState.value = GrowthAlbumState.Success
        getOneBabyUseCase.getOneBaby()
    }
}
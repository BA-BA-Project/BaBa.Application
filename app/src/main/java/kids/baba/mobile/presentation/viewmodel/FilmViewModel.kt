package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.domain.repository.CustomCameraRepository
import javax.inject.Inject

@HiltViewModel
class FilmViewModel @Inject constructor(
    private val getSavedImgUseCase: GetSavedImgUseCase
) : ViewModel() {



}
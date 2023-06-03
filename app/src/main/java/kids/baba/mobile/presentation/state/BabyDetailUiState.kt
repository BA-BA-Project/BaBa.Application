package kids.baba.mobile.presentation.state

import kids.baba.mobile.domain.model.BabyProfileResponse

sealed class BabyDetailUiState {
    object Idle : BabyDetailUiState()

    object Loading : BabyDetailUiState()

    data class Success(val data: BabyProfileResponse) : BabyDetailUiState()
}

package kids.baba.mobile.presentation.event

import androidx.annotation.StringRes
import kids.baba.mobile.domain.model.BabyProfileResponse

sealed class BabyDetailEvent {
    data class SuccessBabyDetail(val baby: BabyProfileResponse) : BabyDetailEvent()
    object SuccessDeleteBaby : BabyDetailEvent()
    data class ShowSnackBar(@StringRes val message: Int) : BabyDetailEvent()
    object BackButtonClicked : BabyDetailEvent()
}

package kids.baba.mobile.domain.model

import kids.baba.mobile.presentation.model.BabyInfo

data class SignUpRequestWithBabiesInfo(
    val name: String,
    val iconName: String,
    val relationName: String,
    val babies: List<BabyInfo>
)

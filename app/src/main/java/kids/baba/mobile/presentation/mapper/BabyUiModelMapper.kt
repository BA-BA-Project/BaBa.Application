package kids.baba.mobile.presentation.mapper

import kids.baba.mobile.domain.model.Baby
import kids.baba.mobile.domain.model.BabyResponse
import kids.baba.mobile.presentation.model.BabiesUiModel
import kids.baba.mobile.presentation.model.BabyUiModel

fun BabyResponse.toPresentation() =
    BabiesUiModel(
        myBaby = myBaby.map { it.toPresentation(true) },
        othersBaby = others.map { it.toPresentation(false) }
    )

fun Baby.toPresentation(isMyBaby: Boolean) =
    BabyUiModel(
        babyId = this.babyId,
        groupColor = this.groupColor,
        name = this.name,
        selected = false,
        isMyBaby = isMyBaby
    )

fun BabyUiModel.toDomain() =
    Baby(
        babyId = this.babyId,
        groupColor = this.groupColor,
        name = this.name,
    )

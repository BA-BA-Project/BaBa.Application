package kids.baba.mobile.presentation.mapper

import kids.baba.mobile.domain.model.Baby
import kids.baba.mobile.domain.model.BabyResponse
import kids.baba.mobile.presentation.model.BabiesUiModel
import kids.baba.mobile.presentation.model.BabyUiModel

fun BabyResponse.toPresentation() =
    BabiesUiModel(
        myBaby = myBaby.toPresentation(),
        othersBaby = others.toPresentation()
    )

fun List<Baby>.toPresentation() = this.map {
    BabyUiModel(
        babyId = it.babyId,
        groupColor = it.groupColor,
        name = it.name,
        selected = false
    )
}
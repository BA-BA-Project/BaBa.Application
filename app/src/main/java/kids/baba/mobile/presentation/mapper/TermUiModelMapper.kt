package kids.baba.mobile.presentation.mapper

import kids.baba.mobile.domain.model.TermsData
import kids.baba.mobile.domain.model.TermsDataForSignToken
import kids.baba.mobile.presentation.model.TermsUiModel

fun TermsData.toPresentation(): TermsUiModel {
    return TermsUiModel(
        required = required,
        name = name,
        selected = false,
        url = url
    )
}

fun TermsUiModel.toDomain(): TermsDataForSignToken{
    return TermsDataForSignToken(
        name = name,
        selected = selected
    )
}
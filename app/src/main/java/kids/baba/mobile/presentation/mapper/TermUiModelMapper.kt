package kids.baba.mobile.presentation.mapper

import kids.baba.mobile.domain.model.TermsData
import kids.baba.mobile.presentation.model.TermsUiModel

fun TermsData.toPresentation(): TermsUiModel {
    return TermsUiModel(
        required = required,
        name = content,
        isChecked = false,
        url = url
    )
}
package kids.baba.mobile.presentation.model

data class TermsData(
    val required: Boolean,
    val content: String,
    var isChecked: Boolean,
    val detailUrl: String
)

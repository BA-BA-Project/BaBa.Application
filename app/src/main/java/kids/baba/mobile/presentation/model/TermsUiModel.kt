package kids.baba.mobile.presentation.model

data class TermsUiModel(
    val required: Boolean,
    val name: String,
    var selected: Boolean,
    val url: String
)
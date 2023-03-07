package kids.baba.mobile.presentation.model

data class TermsUiModel(
    val required: Boolean,
    val name: String,
    var isChecked: Boolean,
    val url: String
)
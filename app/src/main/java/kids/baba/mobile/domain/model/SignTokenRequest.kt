package kids.baba.mobile.domain.model

data class SignTokenRequest(
    val socialToken: String,
    val terms: List<TermsDataForSignToken>
)
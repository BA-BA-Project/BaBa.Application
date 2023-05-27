package kids.baba.mobile.domain.model

data class BabiesInfoResponse(
    val babies : List<BabyName>,
    val relationName: String,
    val relationGroup: String
)

data class BabyName(
    val babyName : String
)

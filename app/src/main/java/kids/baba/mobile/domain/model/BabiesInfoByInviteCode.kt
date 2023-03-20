package kids.baba.mobile.domain.model

data class BabiesInfoResponse(
    val babies : List<BabyName>,
    val relationName: String
)

data class BabyName(
    val babyName : String
)

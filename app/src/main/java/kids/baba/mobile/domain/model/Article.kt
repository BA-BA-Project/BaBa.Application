package kids.baba.mobile.domain.model

import java.time.LocalDate

data class Article(
    val date: LocalDate/*String*/,
    val title: String,
    val photo: String,
    val cardStyle: String
)
//"date" : "2023-01-01",
//"title" : "빵긋빵긋",
//"photo" : multipart,
//"cardStyle" : "CARD_STYLE_1"
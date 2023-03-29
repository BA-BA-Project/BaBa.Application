package kids.baba.mobile.domain.model

import java.io.File

data class Article(
    val date: String,
    val title: String,
    val photo: File,
    val cardStyle: String
)
//"date" : "2023-01-01",
//"title" : "빵긋빵긋",
//"photo" : multipart,
//"cardStyle" : "CARD_STYLE_1"
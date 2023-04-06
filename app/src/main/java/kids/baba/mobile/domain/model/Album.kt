package kids.baba.mobile.domain.model

import kids.baba.mobile.presentation.model.AlbumUiModel
import java.time.LocalDate

data class Album(
    val contentId: Int = -1,
    val name: String = "",
    val relation: String = "",
    val date: String = "",
    val title: String = "",
    val like: Boolean = false,
    val photo: String = "",
    val cardStyle: String = ""
)

//"contentId" : 1,
//"name" : "손제인", // 올린 사람
//"relation": "엄마", // 올린 사람과 아기와의 관계
//"date" : "2023-01-01",
//"title" : "빵긋빵긋",
//"like" : true ( or false)
//"photo" : "www.naver.com",
//"cardStyle" : "CARD_STYLE_1"
package kids.baba.mobile.presentation.model

import kotlinx.parcelize.Parcelize

@Parcelize
data class BabyUiModel(
    val babyId : String = "",
    val groupColor: String = "#FF0000" ,
    val name: String = "",
    val selected: Boolean = false,
    val isMyBaby: Boolean = false
): GroupMember()
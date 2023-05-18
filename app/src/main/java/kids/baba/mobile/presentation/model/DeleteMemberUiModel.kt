package kids.baba.mobile.presentation.model

data class DeleteMemberUiModel(
    val topAppBarTitle: String = "계정 삭제",
    val bannerTitle: String = "계정을 삭제하려는 이유가\n 무엇인가요?",
    val bannerDesc: String = "서비스 개선을 위해 선택해주세요.",
    val forDataDeleteContent: String = "데이터 삭제를 위해",
    val uncomfortableContent: String = "불편한 사용성으로 인해",
    val frequentErrorsContent: String = "잦은 오류로 인해",
    val notUseContent: String = "낮은 사용 빈도로 인해",
    val etcContent: String = "기타"
)

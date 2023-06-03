package kids.baba.mobile.presentation.event

sealed class DeepLinkEvent{
    object RequestLogin: DeepLinkEvent()

    object GoToInviteResultPage: DeepLinkEvent()

    object Failure: DeepLinkEvent()

    object NetworkError: DeepLinkEvent()
}

package kids.baba.mobile.presentation.state

sealed class PostAlbumState {
    object UnInitialized : PostAlbumState()
    object Success : PostAlbumState()
    data class Error(val t: Throwable) : PostAlbumState()
}


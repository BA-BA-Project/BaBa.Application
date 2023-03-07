package kids.baba.mobile.presentation.state

sealed class GrowthAlbumState {

    object UnInitialized : GrowthAlbumState()

    object Loading : GrowthAlbumState()

    //성공시 아이들의 데이터를 불러올 수 있도록 처리
    object Success : GrowthAlbumState()

    object Error :  GrowthAlbumState()
}
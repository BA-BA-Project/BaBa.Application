package kids.baba.mobile.presentation.state

import kids.baba.mobile.domain.model.Album
import kids.baba.mobile.domain.model.Baby
import kids.baba.mobile.domain.model.Comment
import kids.baba.mobile.domain.model.LikeDetailResponse

sealed class GrowthAlbumState {

    object UnInitialized : GrowthAlbumState()

    object Loading : GrowthAlbumState()

    data class SuccessAlbum(val data: List<Album>) : GrowthAlbumState()

    data class SuccessBaby(val data: List<Baby>) : GrowthAlbumState()

    object Post : GrowthAlbumState()

    object AddComment : GrowthAlbumState()

    data class LoadComments(val comments: List<Comment>) : GrowthAlbumState()

    data class GetLikeDetail(val data: LikeDetailResponse) : GrowthAlbumState()
    data class Like(val data: Boolean) : GrowthAlbumState()

    data class Error(val t: Throwable) : GrowthAlbumState()
    object PickDate : GrowthAlbumState()

    object ChangeBaby : GrowthAlbumState()
}
package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.presentation.model.AlbumDetailUiModel
import kids.baba.mobile.presentation.model.AlbumUiModel
import kids.baba.mobile.presentation.model.CommentUiModel
import kids.baba.mobile.presentation.model.UserIconUiModel
import kids.baba.mobile.presentation.model.UserProfileIconUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class AlbumDetailViewModel @Inject constructor(
) : ViewModel() {

    val albumDetail = MutableStateFlow<AlbumDetailUiModel?>(null)
    val album = MutableStateFlow<AlbumUiModel?>(null)

    private val _isPhotoExpended: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val isPhotoExpended = _isPhotoExpended.asStateFlow()
    init {
        getAlbumDetail()
    }

    private fun getAlbumDetail() {
        val tempAlbum = AlbumUiModel(
            contentId = 111,
            name = "박재희",
            relation = "엄마",
            date = "21-09-28",
            title = "앨범 테스트",
            like = false,
            photo = "https://www.shutterstock.com/image-photo/cute-little-african-american-infant-600w-1937038210.jpg",
            cardStyle = "test"
        )
        val tempAlbumDetail = AlbumDetailUiModel(
            likeCount = 3,
            likeUsers = listOf(
                UserIconUiModel(UserProfileIconUiModel.PROFILE_G_1, "#FFA500"),
                UserIconUiModel(UserProfileIconUiModel.PROFILE_G_2, "#BACEE0"),
                UserIconUiModel(UserProfileIconUiModel.PROFILE_G_3, "#629755")
            ),
            commentCount = 2,
            comments = listOf(
                CommentUiModel(
                    commentId = 1,
                    memberId = "111",
                    name = "이호성",
                    relation = "친구",
                    profileIcon = UserProfileIconUiModel.PROFILE_G_1,
                    iconColor = "#629755",
                    tag = "",
                    comment = "댓글 테스트",
                    createdAt = LocalDateTime.now()
                ),
                CommentUiModel(
                    commentId = 2,
                    memberId = "222",
                    name = "박재희",
                    relation = "엄마",
                    profileIcon = UserProfileIconUiModel.PROFILE_G_3,
                    iconColor = "#FFA500",
                    tag = "이호성",
                    comment = "댓글 테스트2",
                    createdAt = LocalDateTime.now()
                ),
                CommentUiModel(
                    commentId = 3,
                    memberId = "222",
                    name = "박재희",
                    relation = "엄마",
                    profileIcon = UserProfileIconUiModel.PROFILE_G_3,
                    iconColor = "#FFA500",
                    tag = "이호성",
                    comment = "댓글 테스트2",
                    createdAt = LocalDateTime.now()
                ),
                CommentUiModel(
                    commentId = 4,
                    memberId = "222",
                    name = "박재희",
                    relation = "엄마",
                    profileIcon = UserProfileIconUiModel.PROFILE_G_3,
                    iconColor = "#FFA500",
                    tag = "이호성",
                    comment = "댓글 테스트2",
                    createdAt = LocalDateTime.now()
                ),
                CommentUiModel(
                    commentId = 6,
                    memberId = "222",
                    name = "박재희",
                    relation = "엄마",
                    profileIcon = UserProfileIconUiModel.PROFILE_G_3,
                    iconColor = "#FFA500",
                    tag = "이호성",
                    comment = "댓글 테스트2",
                    createdAt = LocalDateTime.now()
                ),
                CommentUiModel(
                    commentId = 7,
                    memberId = "222",
                    name = "박재희",
                    relation = "엄마",
                    profileIcon = UserProfileIconUiModel.PROFILE_G_3,
                    iconColor = "#FFA500",
                    tag = "이호성",
                    comment = "댓글 테스트2",
                    createdAt = LocalDateTime.now()
                ),
                CommentUiModel(
                    commentId = 8,
                    memberId = "222",
                    name = "박재희",
                    relation = "엄마",
                    profileIcon = UserProfileIconUiModel.PROFILE_G_3,
                    iconColor = "#FFA500",
                    tag = "이호성",
                    comment = "댓글 테스트2",
                    createdAt = LocalDateTime.now()
                ),
                CommentUiModel(
                    commentId = 9,
                    memberId = "222",
                    name = "박재희",
                    relation = "엄마",
                    profileIcon = UserProfileIconUiModel.PROFILE_G_3,
                    iconColor = "#FFA500",
                    tag = "이호성",
                    comment = "댓글 테스트2",
                    createdAt = LocalDateTime.now()
                ),
                CommentUiModel(
                    commentId = 10,
                    memberId = "222",
                    name = "박재희",
                    relation = "엄마",
                    profileIcon = UserProfileIconUiModel.PROFILE_G_3,
                    iconColor = "#FFA500",
                    tag = "이호성",
                    comment = "댓글 테스트2",
                    createdAt = LocalDateTime.now()
                ),
                CommentUiModel(
                    commentId = 11,
                    memberId = "222",
                    name = "박재희",
                    relation = "엄마",
                    profileIcon = UserProfileIconUiModel.PROFILE_G_3,
                    iconColor = "#FFA500",
                    tag = "이호성",
                    comment = "댓글 테스트2",
                    createdAt = LocalDateTime.now()
                )

            )
        )
        albumDetail.value = tempAlbumDetail
        album.value = tempAlbum
    }

    fun setExpended(expended: Boolean) {
        if (expended != _isPhotoExpended.value) {
            _isPhotoExpended.value = expended
        }
    }
}
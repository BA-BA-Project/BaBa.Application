package kids.baba.mobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.R
import kids.baba.mobile.presentation.model.AlbumDetailUiModel
import kids.baba.mobile.presentation.model.CommentUiModel
import kids.baba.mobile.presentation.model.UserIconUiModel
import kids.baba.mobile.presentation.state.AlbumDetailUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class AlbumDetailViewModel @Inject constructor(
)  : ViewModel(){
    private val _albumDetailUiState:MutableStateFlow<AlbumDetailUiState> = MutableStateFlow(AlbumDetailUiState.Loading)
    val albumDetailUiState = _albumDetailUiState.asStateFlow()

    init {
        getAlbumDetail()
    }

    private fun getAlbumDetail(){
        val tempAlbumDetail = AlbumDetailUiModel(
            likeCount = 3,
            likeUsers = listOf(
                UserIconUiModel(R.drawable.profile_g_1, "#FFA500"),
                UserIconUiModel(R.drawable.profile_g_2, "#BACEE0"),
                UserIconUiModel(R.drawable.profile_g_3, "#629755")
            ),
            commentCount = 2,
            comments = listOf(
                CommentUiModel(
                    commentId = 1,
                    memberId = "111",
                    name = "이호성",
                    relation = "친구",
                    iconName = R.drawable.profile_m_5,
                    iconColor = "FFA500",
                    tag = "",
                    comment = "댓글 테스트",
                    createdAt = LocalDateTime.now()
                ),
                CommentUiModel(
                    commentId = 2,
                    memberId = "222",
                    name = "박재희",
                    relation = "엄마",
                    iconName = R.drawable.profile_w_1,
                    iconColor = "FFA500",
                    tag = "",
                    comment = "댓글 테스트2",
                    createdAt = LocalDateTime.now()
                )
            )
        )
        _albumDetailUiState.value = AlbumDetailUiState.Success(tempAlbumDetail)
    }
}
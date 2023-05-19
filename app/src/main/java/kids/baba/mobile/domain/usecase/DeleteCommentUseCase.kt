package kids.baba.mobile.domain.usecase

import kids.baba.mobile.domain.repository.AlbumRepository
import javax.inject.Inject

class DeleteCommentUseCase @Inject constructor(private val repository: AlbumRepository) {

    suspend operator fun invoke(id: String, contentId: String, commentId: String,) =
        repository.deleteComment(id, contentId, commentId)
}
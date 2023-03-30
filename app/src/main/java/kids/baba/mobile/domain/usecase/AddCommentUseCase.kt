package kids.baba.mobile.domain.usecase

import kids.baba.mobile.domain.model.CommentInput
import kids.baba.mobile.domain.repository.AlbumRepository
import javax.inject.Inject

class AddCommentUseCase @Inject constructor(private val repository: AlbumRepository) {
    suspend fun add(id: String, contentId: String, commentInput: CommentInput) =
        repository.addComment(id, contentId, commentInput)
}
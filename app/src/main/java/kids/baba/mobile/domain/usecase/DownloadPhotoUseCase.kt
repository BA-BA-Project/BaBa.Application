package kids.baba.mobile.domain.usecase

import kids.baba.mobile.domain.repository.FileRepository
import javax.inject.Inject

class DownloadPhotoUseCase @Inject constructor(private val repository: FileRepository) {
    suspend operator fun invoke(fileUrl: String, fileName: String) = repository.saveFile(fileUrl, fileName)
}
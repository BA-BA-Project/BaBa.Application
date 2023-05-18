package kids.baba.mobile.presentation.state

import kids.baba.mobile.presentation.model.AlbumUiModel
import kids.baba.mobile.presentation.model.BabyUiModel
import java.time.LocalDate

data class GrowthAlbumState(
    val growthAlbumList: List<AlbumUiModel> = emptyList(),
    val selectedDate: LocalDate = LocalDate.now(),
    val selectedAlbum: AlbumUiModel = AlbumUiModel(date = LocalDate.now()),
    val selectedBaby: BabyUiModel = BabyUiModel()
)
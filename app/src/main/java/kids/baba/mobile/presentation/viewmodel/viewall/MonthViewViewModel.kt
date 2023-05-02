package kids.baba.mobile.presentation.viewmodel.viewall

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.presentation.model.AlbumUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MonthViewViewModel @Inject constructor() : ViewModel() {


    private val _monthAlbumListState: MutableStateFlow<List<AlbumUiModel>> = MutableStateFlow(listOf())
    val monthAlbumListState = _monthAlbumListState.asStateFlow()

    init {
        loadAlbum()
    }

    private fun loadAlbum() = viewModelScope.launch {
        // 아기 더미 데이터
        _monthAlbumListState.value = listOf(
            AlbumUiModel(100, "name1", "엄마", LocalDate.of(2023, 1, 1), "title1", false, "https://i.pravatar.cc/300", "CARD_BASIC_1", true),
            AlbumUiModel(101, "name2", "아빠", LocalDate.of(2023, 1, 2), "title2", true,"https://i.pravatar.cc/301", "CARD_CLOUD_1", true),
            AlbumUiModel(102, "name3", "엄마", LocalDate.of(2023, 2, 3), "title3", false, "https://i.pravatar.cc/302", "CARD_TOY_1", true),
            AlbumUiModel(103, "name4", "아빠", LocalDate.of(2023, 2, 4), "title4", true,"https://i.pravatar.cc/303", "CARD_BASIC_1", false),
            AlbumUiModel(104, "name1", "엄마", LocalDate.of(2023, 3, 5), "title5", false, "https://i.pravatar.cc/304", "CARD_SNOWFLOWER_2", true),
            AlbumUiModel(105, "name2", "엄마", LocalDate.of(2023, 3, 6), "title6", true,"https://i.pravatar.cc/305", "CARD_LINE_1", false),
            AlbumUiModel(106, "name3", "이모", LocalDate.of(2023, 4, 7), "title7", false, "https://i.pravatar.cc/306", "CARD_CHECK_1", true),
            AlbumUiModel(107, "name4", "삼촌", LocalDate.of(2023, 4, 8), "title8", true,"https://i.pravatar.cc/307", "CARD_CHECK_2", false),
        )


    }


}
package kids.baba.mobile.presentation.event

import androidx.annotation.StringRes

sealed class AlbumConfigEvent{
    object DeleteAlbum: AlbumConfigEvent()

    object ShowDeleteCheckDialog: AlbumConfigEvent()

    object ReportAlbum: AlbumConfigEvent()

    object ShowReportCheckDialog: AlbumConfigEvent()

    data class ShowSnackBar(@StringRes val message: Int): AlbumConfigEvent()
}

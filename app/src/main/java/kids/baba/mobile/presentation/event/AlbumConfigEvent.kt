package kids.baba.mobile.presentation.event

import android.net.Uri
import androidx.annotation.StringRes

sealed class AlbumConfigEvent{
    object DeleteAlbum: AlbumConfigEvent()

    object ShowDeleteCheckDialog: AlbumConfigEvent()

    object ReportAlbum: AlbumConfigEvent()

    object ShowReportCheckDialog: AlbumConfigEvent()

    data class ShowDownSuccessNotification(val uri: Uri): AlbumConfigEvent()

    data class ShowSnackBar(@StringRes val message: Int): AlbumConfigEvent()
}

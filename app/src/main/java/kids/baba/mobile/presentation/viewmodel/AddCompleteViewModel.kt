package kids.baba.mobile.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kids.baba.mobile.R
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class AddCompleteViewModel @Inject constructor() : ViewModel() {
    val title = MutableStateFlow("1")
    val bannerTitle = MutableStateFlow("2")
    val bannerDescription = MutableStateFlow("3")

    fun bind(context: Context) = with(context) {
        title.value = getString(R.string.add_baby_title)
        bannerTitle.value = getString(R.string.baby_add_complete)
        bannerDescription.value = getString(R.string.save_your_memories)
    }
}
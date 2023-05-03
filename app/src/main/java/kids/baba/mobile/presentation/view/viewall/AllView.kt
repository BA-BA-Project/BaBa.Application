package kids.baba.mobile.presentation.view.viewall

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import kids.baba.mobile.R
import javax.inject.Inject

@AndroidEntryPoint
class AllView @Inject constructor(): Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_all_view, container, false)
    }
}
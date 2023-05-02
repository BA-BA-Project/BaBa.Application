package kids.baba.mobile.presentation.view.viewall

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kids.baba.mobile.R

class AllView: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d("AllView", "AllView")
        return inflater.inflate(R.layout.fragment_all_view, container, false)
    }
}
package kids.baba.mobile.presentation.view.viewall

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kids.baba.mobile.R

class YearView: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d("YearView", "Yearview")
        return inflater.inflate(R.layout.fragment_year_view, container, false)
    }

}
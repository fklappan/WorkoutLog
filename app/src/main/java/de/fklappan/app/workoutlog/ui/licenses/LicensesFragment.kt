package de.fklappan.app.workoutlog.ui.licenses

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import de.fklappan.app.workoutlog.R
import de.fklappan.app.workoutlog.common.BaseFragment
import de.fklappan.app.workoutlog.common.LOG_TAG
import de.fklappan.app.workoutlog.common.ViewModelFactory
import kotlinx.android.synthetic.main.licenses.*
import kotlinx.android.synthetic.main.overviewresult.*
import javax.inject.Inject

class LicensesFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.licenses, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initFragment()
    }

    private fun initFragment() {
        getAppBarHeader().setHeaderText(R.string.menu_licenses)
        webview.loadUrl("file:///android_asset/html/licenses.html")
    }

}
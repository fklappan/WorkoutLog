package de.fklappan.app.workoutlog.ui.licenses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.fklappan.app.workoutlog.R
import de.fklappan.app.workoutlog.common.BaseFragment
import de.fklappan.app.workoutlog.databinding.LicensesBinding

class LicensesFragment : BaseFragment() {

    private lateinit var binding : LicensesBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = LicensesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initFragment()
    }

    private fun initFragment() {
        getAppBarHeader().setHeaderText(R.string.menu_licenses)
        binding.webview.loadUrl("file:///android_asset/html/licenses.html")
    }

}
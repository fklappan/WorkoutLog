package de.fklappan.app.workoutlog.ui.addresult

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import de.fklappan.app.workoutlog.common.LOG_TAG
import de.fklappan.app.workoutlog.R
import de.fklappan.app.workoutlog.common.BaseFragment
import de.fklappan.app.workoutlog.common.ViewModelFactory
import de.fklappan.app.workoutlog.ui.detailview.WorkoutResultGuiModel
import kotlinx.android.synthetic.main.addworkout.*
import kotlinx.android.synthetic.main.overview.floatingActionButton
import kotlinx.android.synthetic.main.overview.textViewError
import java.util.*
import javax.inject.Inject

class AddResultFragment : BaseFragment() {

    @Inject lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModelAddResult: AddResultViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.addresult, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getInjector().inject(this)
        super.onViewCreated(view, savedInstanceState)

        initFragment()
        initFab()
        initViewModels()
        observeViewModels()
    }

    private fun initFab() {
        floatingActionButton.setOnClickListener { view ->
            // TODO 09.07.2019 Flo fill additional data
            val guiModel = WorkoutResultGuiModel(0, editTextContent.text.toString(), Date(), arguments!!.getInt("workoutId"), false, "", "")
            viewModelAddResult.saveResult(guiModel)
        }
    }

    private fun initFragment() {
    }

    private fun initViewModels() {
        viewModelAddResult = ViewModelProviders.of(this, viewModelFactory).get(AddResultViewModel::class.java)
    }

    private fun observeViewModels() {
        viewModelAddResult.state.observe(this, Observer { state -> renderState(state) } )
    }

    private fun renderState(state: AddResultState) {
        if (state.error != null) {
            showError(state.error)
        } else {
            showResult(state.saved)
        }
    }

    private fun showResult(saved: Boolean) {
        Log.d(LOG_TAG, "saved workout: $saved")
        textViewError.visibility = View.GONE
        Snackbar.make(view!!, getString(R.string.message_added_workout_result), Snackbar.LENGTH_LONG).show()
        Navigation.findNavController(view!!).navigateUp()
    }

    private fun showError(error: Throwable) {
        Log.d(LOG_TAG, "Error occured", error)
        textViewError.visibility = View.VISIBLE
        textViewError.text = error.message
    }

}
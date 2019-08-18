package de.fklappan.app.workoutlog.ui.addworkout

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import de.fklappan.app.workoutlog.R
import de.fklappan.app.workoutlog.common.BaseFragment
import de.fklappan.app.workoutlog.common.LOG_TAG
import de.fklappan.app.workoutlog.common.ViewModelFactory
import de.fklappan.app.workoutlog.ui.overviewworkout.WorkoutGuiModel
import kotlinx.android.synthetic.main.addworkout.*
import kotlinx.android.synthetic.main.overview.floatingActionButton
import javax.inject.Inject

class AddWorkoutFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModelAddWorkout: AddWorkoutViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.addworkout, container, false)
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
        floatingActionButton.setOnClickListener {
            val guiModel = WorkoutGuiModel(0, editTextContent.text.toString(), favorite = false)
            viewModelAddWorkout.saveWorkout(guiModel)
        }
    }

    private fun initFragment() {
        getAppBarHeader().setHeaderText(R.string.caption_add_workout)
    }

    private fun initViewModels() {
        viewModelAddWorkout = ViewModelProviders.of(this, viewModelFactory).get(AddWorkoutViewModel::class.java)
    }

    private fun observeViewModels() {
        viewModelAddWorkout.saveState.observe(this, Observer { saved -> showResult(saved) })
        viewModelAddWorkout.errorState.observe(this, Observer { error -> showError(error) })
    }

    private fun showResult(saved: Boolean) {
        Log.d(LOG_TAG, "saved workout: $saved")
        textViewError.visibility = View.GONE
        Snackbar.make(view!!, getString(R.string.message_saved_workout), Snackbar.LENGTH_LONG).show()
        Navigation.findNavController(view!!).navigateUp()
    }

    private fun showError(error: Throwable) {
        Log.d(LOG_TAG, "Error occured", error)
        textViewError.visibility = View.VISIBLE
        textViewError.text = error.message
    }

}
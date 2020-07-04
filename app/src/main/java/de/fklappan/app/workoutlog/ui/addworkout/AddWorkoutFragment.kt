package de.fklappan.app.workoutlog.ui.addworkout

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
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

    companion object {
        val KEY_WORKOUT_ADDED = "KEY_WORKOUT_ADDED"
    }

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
        viewModelAddWorkout.state.observe(viewLifecycleOwner, Observer { state -> updateState(state) })
    }

    private fun updateState(state: AddWorkoutState) {
        when(state) {
            is AddWorkoutState.Save -> showResult()
            is AddWorkoutState.Error -> showError(state.message)
        }
    }

    private fun showResult() {
        Log.d(LOG_TAG, "saved workout")
        textViewError.visibility = View.GONE
        Snackbar.make(view!!, getString(R.string.message_saved_workout), Snackbar.LENGTH_LONG).show()
        // create livedata to notify calling fragment about workout creation
        findNavController().previousBackStackEntry?.savedStateHandle?.set(KEY_WORKOUT_ADDED, true)
        Navigation.findNavController(view!!).navigateUp()
    }

    private fun showError(message: String) {
        Log.d(LOG_TAG, "Error occured: $message")
        textViewError.visibility = View.VISIBLE
        textViewError.text = message
    }

}
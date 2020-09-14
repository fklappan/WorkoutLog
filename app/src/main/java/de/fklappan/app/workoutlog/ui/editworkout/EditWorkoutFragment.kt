package de.fklappan.app.workoutlog.ui.editworkout

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import de.fklappan.app.workoutlog.R
import de.fklappan.app.workoutlog.common.BaseFragment
import de.fklappan.app.workoutlog.common.LOG_TAG
import de.fklappan.app.workoutlog.common.ViewModelFactory
import de.fklappan.app.workoutlog.common.errorStyle
import de.fklappan.app.workoutlog.ui.overviewworkout.WorkoutGuiModel
import kotlinx.android.synthetic.main.addworkout.editTextContent
import kotlinx.android.synthetic.main.overview.floatingActionButton
import javax.inject.Inject

class EditWorkoutFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: EditWorkoutViewModel

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
        fetchData()
    }

    private fun initFab() {
        floatingActionButton.setOnClickListener {
            viewModel.saveWorkout(editTextContent.text.toString())
        }
    }

    private fun initFragment() {
        getAppBarHeader().setHeaderText(R.string.caption_edit_workout)
    }

    private fun initViewModels() {
        viewModel = ViewModelProvider(this, viewModelFactory).get(EditWorkoutViewModel::class.java)
    }

    private fun observeViewModels() {
        viewModel.state.observe(viewLifecycleOwner, Observer { state -> updateState(state) })
    }

    private fun updateState(state: EditWorkoutState) {
        when(state) {
            is EditWorkoutState.Loading -> showLoading()
            is EditWorkoutState.Error -> showError(state.message)
            is EditWorkoutState.Workout -> showWorkout(state.workout)
            is EditWorkoutState.Save -> showResult()
        }
    }

    private fun showLoading() {
        Log.d(LOG_TAG, "Loading workout")
    }

    private fun showWorkout(workout: WorkoutGuiModel) {
        editTextContent.setText(workout.text)
    }

    private fun showResult() {
        Log.d(LOG_TAG, "saved workout")
        Snackbar.make(requireView(), getString(R.string.message_saved_workout), Snackbar.LENGTH_LONG).show()
        Navigation.findNavController(requireView()).navigateUp()
    }

    private fun showError(message: String) {
        Log.e(LOG_TAG, "Error fetching workout: $message")
        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT)
            .errorStyle(requireContext())
            .show()
    }

    private fun fetchData() {
        viewModel.loadWorkout(requireArguments().getInt("workoutId"))
    }

}
package de.fklappan.app.workoutlog.ui.overviewworkout

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import de.fklappan.app.workoutlog.R
import de.fklappan.app.workoutlog.common.BaseFragment
import de.fklappan.app.workoutlog.common.LOG_TAG
import de.fklappan.app.workoutlog.common.ViewModelFactory
import kotlinx.android.synthetic.main.overview.*
import javax.inject.Inject

class OverviewWorkoutFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModelOverviewWorkout: OverviewWorkoutViewModel
    private lateinit var overviewWorkoutAdapter: OverviewWorkoutAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.overview, container, false)
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
        floatingActionButton.setOnClickListener { view ->
            Navigation.findNavController(view).navigate(R.id.action_overviewFragment_to_addWorkoutFragment)
        }
    }

    private fun initFragment() {
        getAppBarHeader().setHeaderText(R.string.caption_workout_overview)
        overviewWorkoutAdapter = OverviewWorkoutAdapter(this::workoutClicked, this::favoriteClicked)
        recyclerViewWorkouts.adapter = overviewWorkoutAdapter
    }

    private fun workoutClicked(workoutGuiModel: WorkoutGuiModel) {
        Log.d(LOG_TAG, "clicked workout id" + workoutGuiModel.workoutId)
        val bundle = bundleOf("workoutId" to workoutGuiModel.workoutId)
        Navigation.findNavController(view!!).navigate(R.id.action_overviewFragment_to_detailviewWorkoutFragment, bundle)
    }

    private fun favoriteClicked(workoutId: Int) {
        Log.d(LOG_TAG, "clicked workout $workoutId to toggle favorite")
        viewModelOverviewWorkout.favoriteClicked(workoutId)
    }

    private fun initViewModels() {
        viewModelOverviewWorkout =
            ViewModelProviders.of(this, viewModelFactory).get(OverviewWorkoutViewModel::class.java)
    }

    private fun observeViewModels() {
        viewModelOverviewWorkout.workoutList.observe(this, Observer { workoutList -> showWorkoutList(workoutList) })
        viewModelOverviewWorkout.updateStream.observe(this, Observer { workout -> updateWorkout(workout) })
    }

    private fun fetchData() {
        viewModelOverviewWorkout.loadWorkouts()
    }

    private fun updateWorkout(workoutGuiModel: WorkoutGuiModel) {
        Log.d(LOG_TAG, "Updating workout ${workoutGuiModel.workoutId}")
        overviewWorkoutAdapter.update(workoutGuiModel)
    }

    private fun showWorkoutList(resultList: MutableList<WorkoutGuiModel>) {
        Log.d(LOG_TAG, "Results loaded: " + resultList.size)
        overviewWorkoutAdapter.items = resultList
    }
}
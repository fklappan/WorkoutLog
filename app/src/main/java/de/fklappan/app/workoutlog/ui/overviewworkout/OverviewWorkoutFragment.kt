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
        overviewWorkoutAdapter = OverviewWorkoutAdapter { clickedWorkout ->
            Log.d(LOG_TAG, "clicked workout id" + clickedWorkout.workoutId)
            val bundle = bundleOf("workoutId" to clickedWorkout.workoutId)
            Navigation.findNavController(view!!)
                .navigate(R.id.action_overviewFragment_to_detailviewWorkoutFragment, bundle)
        }
        recyclerViewWorkouts.adapter = overviewWorkoutAdapter
    }

    private fun initViewModels() {
        viewModelOverviewWorkout =
            ViewModelProviders.of(this, viewModelFactory).get(OverviewWorkoutViewModel::class.java)
    }

    private fun observeViewModels() {
        viewModelOverviewWorkout.workoutState.observe(this, Observer { state -> renderState(state) })
    }

    private fun fetchData() {
        viewModelOverviewWorkout.loadWorkouts()
    }

    private fun renderState(workoutState: OverviewWorkoutState) {
        if (workoutState.error != null) {
            showError(workoutState.error)
        } else {
            showResult(workoutState.workoutList)
        }
    }

    private fun showResult(resultList: MutableList<WorkoutGuiModel>) {
        Log.d(LOG_TAG, "Results loaded: " + resultList.size)
        recyclerViewWorkouts.visibility = View.VISIBLE
        overviewWorkoutAdapter.items = resultList
    }

    private fun showError(error: Throwable) {
        Log.d(LOG_TAG, "Error occured", error)
        recyclerViewWorkouts.visibility = View.GONE
    }
}
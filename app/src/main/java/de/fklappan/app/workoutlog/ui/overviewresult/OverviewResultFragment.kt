package de.fklappan.app.workoutlog.ui.overviewresult

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
import de.fklappan.app.workoutlog.ui.detailviewworkout.WorkoutResultGuiModel
import kotlinx.android.synthetic.main.overview.*
import javax.inject.Inject

class OverviewResultFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: OverviewResultViewModel
    private lateinit var adapter: OverviewResultAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.overview_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getInjector().inject(this)
        super.onViewCreated(view, savedInstanceState)

        initFragment()
        initViewModels()
        observeViewModels()
        fetchData()
    }

    private fun initFragment() {
        getAppBarHeader().setHeaderText(R.string.caption_workout_results)
        adapter = OverviewResultAdapter(this::resultClicked)
        recyclerViewWorkouts.adapter = adapter
    }

    private fun resultClicked(resultGuiModel: WorkoutResultGuiModel) {
        Log.d(LOG_TAG, "clicked result id" + resultGuiModel.resultId)
        val bundle = bundleOf("workoutId" to resultGuiModel.workout)
        Navigation.findNavController(view!!).navigate(R.id.detailviewWorkoutFragment, bundle)
    }

    private fun initViewModels() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(OverviewResultViewModel::class.java)
    }

    private fun observeViewModels() {
        viewModel.state.observe(this, Observer { state -> updateState(state) })
    }

    private fun fetchData() {
        viewModel.init()
    }

    private fun updateState(state: OverviewResultState) {
        when (state) {
            is OverviewResultState.Loading -> showLoading()
            is OverviewResultState.Error -> showError(state.message)
            is OverviewResultState.ResultList -> showWorkoutList(state.workoutResults.toMutableList())
        }
    }

    private fun showLoading() {
        Log.d(LOG_TAG, "Loading workout list")
    }

    private fun showError(message: String) {
        Log.e(LOG_TAG, "Error fetching workouts: $message")
    }

    private fun showWorkoutList(resultList: MutableList<OverviewResultGuiModel>) {
        Log.d(LOG_TAG, "Results loaded: " + resultList.size)
        adapter.items = resultList
    }
}
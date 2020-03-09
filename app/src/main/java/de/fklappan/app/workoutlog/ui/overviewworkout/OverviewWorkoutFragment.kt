package de.fklappan.app.workoutlog.ui.overviewworkout

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
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
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.setQuery(viewModelOverviewWorkout.getLastSearchQuery(), false)
        searchView.setIconifiedByDefault(viewModelOverviewWorkout.getLastSearchQuery().isEmpty())

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModelOverviewWorkout.onSearchWorkoutQueryChanged(newText!!)
                return true
            }

        })
    }

    private fun workoutClicked(workoutGuiModel: WorkoutGuiModel) {
        Log.d(LOG_TAG, "clicked workout id" + workoutGuiModel.workoutId)
        val bundle = bundleOf("workoutId" to workoutGuiModel.workoutId)
        Navigation.findNavController(view!!).navigate(R.id.action_overviewFragment_to_detailviewWorkoutFragment, bundle)
    }

    private fun favoriteClicked(workoutId: Int) {
        Log.d(LOG_TAG, "clicked workout $workoutId to toggle favorite")
        viewModelOverviewWorkout.onFavoriteClicked(workoutId)
    }

    private fun initViewModels() {
        viewModelOverviewWorkout =
            ViewModelProviders.of(this, viewModelFactory).get(OverviewWorkoutViewModel::class.java)
    }

    private fun observeViewModels() {
        viewModelOverviewWorkout.state.observe(this, Observer { state -> updateState(state) })
    }

    private fun updateState(state: OverviewWorkoutState) {
        when (state) {
            is OverviewWorkoutState.Loading -> showLoading()
            is OverviewWorkoutState.Error -> showError(state.message)
            is OverviewWorkoutState.WorkoutList -> showWorkoutList(state.workouts.toMutableList())
            is OverviewWorkoutState.WorkoutUpdate -> showWorkoutUpdate(state.workout)
        }
    }

    private fun showLoading() {
        Log.d(LOG_TAG, "Loading workout list")
    }

    private fun showError(message: String) {
        Log.e(LOG_TAG, "Error fetching workouts: $message")
    }

    private fun showWorkoutUpdate(workoutGuiModel: WorkoutGuiModel) {
        Log.d(LOG_TAG, "Updating workout ${workoutGuiModel.workoutId}")
        overviewWorkoutAdapter.update(workoutGuiModel)
    }

    private fun showWorkoutList(resultList: MutableList<WorkoutGuiModel>) {
        Log.d(LOG_TAG, "Results loaded: " + resultList.size)
        overviewWorkoutAdapter.items = resultList
    }
}
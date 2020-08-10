package de.fklappan.app.workoutlog.ui.overviewworkout

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.snackbar.Snackbar
import de.fklappan.app.workoutlog.R
import de.fklappan.app.workoutlog.common.BaseFragment
import de.fklappan.app.workoutlog.common.LOG_TAG
import de.fklappan.app.workoutlog.common.ViewModelFactory
import de.fklappan.app.workoutlog.ui.addworkout.AddWorkoutFragment
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.overview.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class OverviewWorkoutFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModelOverviewWorkout: OverviewWorkoutViewModel
    private lateinit var viewModelDelete: DeleteViewModel
    private lateinit var overviewWorkoutAdapter: OverviewWorkoutAdapter
    private lateinit var snackbarDelete: Snackbar
    private var disposableDelete = CompositeDisposable()

    private var motionProgress: Float = 0f

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

        savedInstanceState?.let{
            motionProgress = it.getFloat("motion-progress", 0f)
        }

        motionLayout.progress = motionProgress
    }

    override fun onDestroyView() {
        motionProgress = motionLayout.progress
        super.onDestroyView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putFloat("motion-progress", motionProgress)
    }

    private fun initFab() {
        floatingActionButton.setOnClickListener { view ->
            Navigation.findNavController(view).navigate(R.id.action_overviewFragment_to_addWorkoutFragment)
        }
    }

    private fun initFragment() {
        getAppBarHeader().setHeaderText(R.string.caption_workout_overview)
        overviewWorkoutAdapter = OverviewWorkoutAdapter(this::workoutClicked, this::optionsClicked, this::favoriteClicked)
        recyclerViewWorkouts.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        recyclerViewWorkouts.adapter = overviewWorkoutAdapter
        snackbarDelete = Snackbar.make(requireView(), getString(R.string.success_deleted_workout), Snackbar.LENGTH_LONG)
            .setActionTextColor(resources.getColor(android.R.color.holo_red_light, requireActivity().theme))
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
        Navigation.findNavController(requireView()).navigate(R.id.action_overviewFragment_to_detailviewWorkoutFragment, bundle)
    }

    private fun favoriteClicked(workoutId: Int) {
        Log.d(LOG_TAG, "clicked workout $workoutId to toggle favorite")
        viewModelOverviewWorkout.onFavoriteClicked(workoutId)
    }

    @SuppressLint("RestrictedApi")
    private fun optionsClicked(view: View, workoutGuiModel: WorkoutGuiModel) {
        val menuBuilder = MenuBuilder(requireContext())
        val inflater = MenuInflater(requireContext())
        inflater.inflate(R.menu.options, menuBuilder)

        val menuHelper = MenuPopupHelper(requireContext(), menuBuilder, view)
        menuHelper.setForceShowIcon(true)
        val callback = object: MenuBuilder.Callback {
            override fun onMenuModeChange(menu: MenuBuilder?) {
            }

            override fun onMenuItemSelected(menu: MenuBuilder?, item: MenuItem?): Boolean {
                return menuItemClicked(item!!, workoutGuiModel)
            }
        }
        menuBuilder.setCallback(callback)
        menuHelper.show()
    }

    private fun menuItemClicked(item: MenuItem, workoutGuiModel: WorkoutGuiModel) : Boolean {
        when(item.itemId) {
            R.id.action_delete -> {
                prepareDeleteWorkout(workoutGuiModel)
                return true
            }
            R.id.action_edit -> {
                val bundle = bundleOf("workoutId" to workoutGuiModel.workoutId)
                Navigation.findNavController(requireView())
                    .navigate(R.id.editWorkoutFragment, bundle)
                return true
            }
        }
        return false
    }

    private fun prepareDeleteWorkout(workoutGuiModel: WorkoutGuiModel) {
        Log.d(LOG_TAG, "deleteWorkout")

        val deletedIndex = overviewWorkoutAdapter.deleteItem(workoutGuiModel)

        snackbarDelete
            .setAction(getString(R.string.undo)) { undoDelete(workoutGuiModel, deletedIndex) }
            .show()

        disposableDelete.add(
            Single.just(workoutGuiModel)
                .delay(2000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::deleteForGood)
        )
    }

    private fun initViewModels() {
        viewModelOverviewWorkout = ViewModelProvider(this, viewModelFactory).get(OverviewWorkoutViewModel::class.java)
//        viewModelOverviewWorkout = ViewModelProviders.of(this, viewModelFactory).get(OverviewWorkoutViewModel::class.java)
        viewModelDelete = ViewModelProvider(this, viewModelFactory).get(DeleteViewModel::class.java)
//        viewModelDelete = ViewModelProviders.of(this, viewModelFactory).get(DeleteViewModel::class.java)
    }

    private fun observeViewModels() {
        viewModelOverviewWorkout.state.observe(viewLifecycleOwner, Observer { state -> updateState(state) })

        // observe livedata which will be created by the AddWorkoutFragment
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData(AddWorkoutFragment.KEY_WORKOUT_ADDED, false)?.
        observe(viewLifecycleOwner, Observer { addedWorkout ->
            if (addedWorkout) {
                viewModelOverviewWorkout.loadWorkouts()
            }})
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

    private fun undoDelete(workoutGuiModel: WorkoutGuiModel, deletedIndex: Int) {
        disposableDelete.clear()
        overviewWorkoutAdapter.addItem(workoutGuiModel, deletedIndex)
    }

    fun deleteForGood(workoutGuiModel: WorkoutGuiModel) {
        viewModelDelete.deleteWorkout(workoutGuiModel.workoutId)
    }

}
package de.fklappan.app.workoutlog.ui.overviewworkout

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.fklappan.app.workoutlog.common.GuiModelMapper
import de.fklappan.app.workoutlog.common.LOG_TAG
import de.fklappan.app.workoutlog.common.RxViewModel
import de.fklappan.app.workoutlog.common.UseCasesFactory
import de.fklappan.app.workoutlog.domain.WorkoutDomainModel
import io.reactivex.Scheduler

class OverviewWorkoutViewModel(private val useCaseFactory: UseCasesFactory,
                               private val schedulerIo: Scheduler,
                               private val schedulerMainThread: Scheduler,
                               private val modelMapper: GuiModelMapper
) :
    RxViewModel() {

    private var lastSearchQuery: String = ""

    // store the unfiltered list to be able to restore it after deleting a filter
    private val _workoutListUnfiltered = ArrayList<WorkoutGuiModel>()
    private val _workoutListFiltered = ArrayList<WorkoutGuiModel>()

    // exposing only a state
    private val _state = MutableLiveData<OverviewWorkoutState>()
    val state: LiveData<OverviewWorkoutState>
        get() = _state

    init {
        loadWorkouts()
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // public methods aka entry points - will be invoked by the view

    fun loadWorkouts() {
        _state.value = OverviewWorkoutState.Loading
        addDisposable(
            useCaseFactory.createGetWorkoutsUseCase().execute()
                .subscribeOn(schedulerIo)
                .observeOn(schedulerMainThread)
                .subscribe(
                    this::handleSuccess,
                    this::handleError
                )
        )
    }

    fun onFavoriteClicked(workoutId: Int) {
        addDisposable(
            useCaseFactory.createToggleFavoriteWorkoutUseCase().execute(workoutId)
                .subscribeOn(schedulerIo)
                .observeOn(schedulerMainThread)
                .subscribe(
                    this::handleSuccessFavoriteUseCase,
                    this::handleErrorFavoriteUseCase
                )
        )
    }

    fun onSearchWorkoutQueryChanged(query: String) {
        lastSearchQuery = query
        _workoutListFiltered.clear()

        for(workout in _workoutListUnfiltered) {
            if (workout.text.contains(query, true)) {
                _workoutListFiltered.add(workout)
            }
        }
        _state.value = OverviewWorkoutState.WorkoutList(_workoutListFiltered)
    }

    fun getLastSearchQuery() = lastSearchQuery

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // private methods

    private fun handleSuccess(workoutList: List<WorkoutDomainModel>) {
        // map domain model to gui model
        val guiModelList = ArrayList<WorkoutGuiModel>()
        for (domainWorkout in workoutList) {
            guiModelList.add(modelMapper.mapDomainToGui(domainWorkout))
        }
        _workoutListUnfiltered.clear()
        _workoutListUnfiltered.addAll(guiModelList)
        // apply last known filter on new result
        onSearchWorkoutQueryChanged(lastSearchQuery)
    }

    private fun handleError(error: Throwable) {
        Log.e(LOG_TAG, "Error while loading workouts", error)
        _state.value = OverviewWorkoutState.Error(error.localizedMessage)
    }

    private fun handleSuccessFavoriteUseCase(workoutDomainModel: WorkoutDomainModel) {
        // after changing the model we need to make sure that the state of the view will be updated
        // so that after (ie) orientation change the correct list state is loaded.
        // if we only update the single element of the internal list, after orientation change the
        // LAST known state will be provided by the livedata again - which is without the changed data
        loadWorkouts()
    }

    private fun handleErrorFavoriteUseCase(error: Throwable) {
        // at least log the error. Maybe in the future we need to present it to the user
        Log.e(LOG_TAG, "Error while executing toggle favorite", error)
        _state.value = OverviewWorkoutState.Error(error.localizedMessage)
    }
}
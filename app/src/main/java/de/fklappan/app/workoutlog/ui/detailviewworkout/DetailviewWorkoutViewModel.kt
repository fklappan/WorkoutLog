package de.fklappan.app.workoutlog.ui.detailviewworkout

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.fklappan.app.workoutlog.common.GuiModelMapper
import de.fklappan.app.workoutlog.common.LOG_TAG
import de.fklappan.app.workoutlog.common.RxViewModel
import de.fklappan.app.workoutlog.common.UseCasesFactory
import de.fklappan.app.workoutlog.domain.WorkoutDetailsDomainModel
import de.fklappan.app.workoutlog.domain.WorkoutDomainModel
import io.reactivex.Scheduler

class DetailviewWorkoutViewModel(private val useCaseFactory: UseCasesFactory,
                                 private val schedulerIo: Scheduler,
                                 private val schedulerMainThread: Scheduler,
                                 private val modelMapper: GuiModelMapper
) : RxViewModel() {

    private var currentWorkoutDetails : WorkoutDetailsGuiModel? = null

    private val _state = MutableLiveData<DetailviewWorkoutState>()
    val state: LiveData<DetailviewWorkoutState>
        get() = _state

    fun forceLoad(workoutId: Int) {
        currentWorkoutDetails = null
        initialize(workoutId)
    }

    fun initialize(workoutId: Int) {
        if (currentWorkoutDetails != null) {
            _state.value = DetailviewWorkoutState.WorkoutDetails(currentWorkoutDetails!!)
            return
        }
        _state.value = DetailviewWorkoutState.Loading
        addDisposable(
            useCaseFactory.createGetWorkoutDetailsUseCase().execute(workoutId)
                .subscribeOn(schedulerIo)
                .observeOn(schedulerMainThread)
                .subscribe(
                    this::workoutDetailsLoaded,
                    this::handleError
                )
        )
    }

    fun onFavoriteClicked() {
        addDisposable(
            useCaseFactory.createToggleFavoriteWorkoutUseCase().execute(currentWorkoutDetails!!.workout.workoutId)
                .subscribeOn(schedulerIo)
                .observeOn(schedulerMainThread)
                .subscribe(
                    this::workoutUpdated,
                    this::handleError
                )
        )
    }

    private fun workoutDetailsLoaded(workoutDetails: WorkoutDetailsDomainModel) {
        // map domain model to gui model
        currentWorkoutDetails = modelMapper.mapDomainToGui(workoutDetails)
        _state.value = DetailviewWorkoutState.WorkoutDetails(currentWorkoutDetails!!)
    }

    private fun workoutUpdated(workout: WorkoutDomainModel) {
        _state.value = DetailviewWorkoutState.WorkoutUpdate(modelMapper.mapDomainToGui(workout))
    }

    private fun handleError(error: Throwable) {
        Log.e(LOG_TAG, "Error while executing request", error)
        _state.value = DetailviewWorkoutState.Error(error.localizedMessage)
    }

}
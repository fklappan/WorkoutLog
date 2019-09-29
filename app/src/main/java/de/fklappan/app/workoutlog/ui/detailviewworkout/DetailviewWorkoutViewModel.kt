package de.fklappan.app.workoutlog.ui.detailviewworkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.fklappan.app.workoutlog.common.GuiModelMapper
import de.fklappan.app.workoutlog.common.RxViewModel
import de.fklappan.app.workoutlog.common.UseCasesFactory
import de.fklappan.app.workoutlog.domain.WorkoutDetailsDomainModel
import de.fklappan.app.workoutlog.domain.WorkoutDomainModel
import de.fklappan.app.workoutlog.ui.overviewworkout.WorkoutGuiModel
import io.reactivex.Scheduler

class DetailviewWorkoutViewModel(private val useCaseFactory: UseCasesFactory,
                                 private val schedulerIo: Scheduler,
                                 private val schedulerMainThread: Scheduler,
                                 private val modelMapper: GuiModelMapper
) : RxViewModel() {

    private lateinit var currentWorkoutDetails: WorkoutDetailsGuiModel

    private val _workoutDetailStream = MutableLiveData<WorkoutDetailsGuiModel>()
    // expose read only workoutState
    val workoutDetailStream: LiveData<WorkoutDetailsGuiModel>
        get() = _workoutDetailStream

    private val _errorStream = MutableLiveData<Throwable>()
    // expose read only workoutState
    val errorStream: LiveData<Throwable>
        get() = _errorStream

    private val _updateWorkoutStream = MutableLiveData<WorkoutGuiModel>()
    // expose read only
    val updateWorkoutStream: LiveData<WorkoutGuiModel>
        get() = _updateWorkoutStream

    fun loadWorkout(workoutId: Int) {
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

    fun favoriteClicked() {
        addDisposable(
            useCaseFactory.createToggleFavoriteWorkoutUseCase().execute(currentWorkoutDetails.workout.workoutId)
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
        _workoutDetailStream.value = currentWorkoutDetails
    }

    private fun workoutUpdated(workout: WorkoutDomainModel) {
        _updateWorkoutStream.value = modelMapper.mapDomainToGui(workout)
    }

    private fun handleError(error: Throwable) {
        _errorStream.value = error
    }

}
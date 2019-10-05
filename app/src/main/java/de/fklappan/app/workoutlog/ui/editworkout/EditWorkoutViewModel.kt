package de.fklappan.app.workoutlog.ui.editworkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.fklappan.app.workoutlog.common.GuiModelMapper
import de.fklappan.app.workoutlog.common.RxViewModel
import de.fklappan.app.workoutlog.common.UseCasesFactory
import de.fklappan.app.workoutlog.domain.WorkoutDetailsDomainModel
import de.fklappan.app.workoutlog.domain.WorkoutDomainModel
import de.fklappan.app.workoutlog.domain.WorkoutLogRepository
import de.fklappan.app.workoutlog.domain.usecases.AddWorkoutUseCase
import de.fklappan.app.workoutlog.domain.usecases.EditWorkoutUseCase
import de.fklappan.app.workoutlog.domain.usecases.GetWorkoutDetailsUseCase
import de.fklappan.app.workoutlog.domain.usecases.GetWorkoutUseCase
import de.fklappan.app.workoutlog.ui.overviewworkout.WorkoutGuiModel
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class EditWorkoutViewModel(private val useCaseFactory: UseCasesFactory,
                           private val schedulerIo: Scheduler,
                           private val schedulerMainThread: Scheduler,
                           private val modelMapper: GuiModelMapper
) :
    RxViewModel() {

    private lateinit var currentWorkout: WorkoutGuiModel

    private val _state = MutableLiveData<EditWorkoutState>()
    // expose read only workoutState
    val state: LiveData<EditWorkoutState>
        get() = _state

    fun loadWorkout(workoutId: Int) {
        _state.value = EditWorkoutState.Loading
        addDisposable(
            useCaseFactory.createGetWorkoutUseCase().execute(workoutId)
                .subscribeOn(schedulerIo)
                .observeOn(schedulerMainThread)
                .subscribe(
                    this::workoutLoaded,
                    this::handleError
                )
        )
    }

    fun saveWorkout(text: String) {
        val workout = WorkoutGuiModel(currentWorkout.workoutId, text, currentWorkout.favorite)
        addDisposable(
            useCaseFactory.createEditWorkoutUseCase().execute(modelMapper.mapGuiToDomain(workout))
                .subscribeOn(schedulerIo)
                .observeOn(schedulerMainThread)
                .subscribe(
                    this::handleSuccess,
                    this::handleError
                )
        )
    }

    private fun workoutLoaded(workout: WorkoutDomainModel) {
        // only pass workout to gui, no need for results here
        currentWorkout = modelMapper.mapDomainToGui(workout)
        _state.value = EditWorkoutState.Workout(currentWorkout)
    }

    private fun handleSuccess() {
        _state.value = EditWorkoutState.Save
    }

    private fun handleError(error: Throwable) {
        _state.value = EditWorkoutState.Error(error.localizedMessage)
    }

}
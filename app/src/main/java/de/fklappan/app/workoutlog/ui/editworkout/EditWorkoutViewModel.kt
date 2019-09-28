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

    private val _workoutStream = MutableLiveData<WorkoutGuiModel>()
    // expose read only
    val workoutStream: LiveData<WorkoutGuiModel>
        get() = _workoutStream

    private val _errorState = MutableLiveData<Throwable>()
    // expose read only workoutState
    val errorState: LiveData<Throwable>
        get() = _errorState

    private val _saveState = MutableLiveData<Boolean>()
    // expose read only workoutState
    val saveState: LiveData<Boolean>
        get() = _saveState

    fun loadWorkout(workoutId: Int) {
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
        _workoutStream.value = currentWorkout
    }

    private fun handleSuccess() {
        _saveState.value = true
    }

    private fun handleError(error: Throwable) {
        _errorState.value = error
    }

}
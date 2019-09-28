package de.fklappan.app.workoutlog.ui.addworkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.fklappan.app.workoutlog.common.GuiModelMapper
import de.fklappan.app.workoutlog.common.RxViewModel
import de.fklappan.app.workoutlog.common.UseCasesFactory
import de.fklappan.app.workoutlog.domain.WorkoutLogRepository
import de.fklappan.app.workoutlog.domain.usecases.AddWorkoutUseCase
import de.fklappan.app.workoutlog.ui.overviewworkout.WorkoutGuiModel
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

// TODO rename, as this is a save workout viewmodel ?

class AddWorkoutViewModel(private val useCaseFactory: UseCasesFactory,
                          private val schedulerIo: Scheduler,
                          private val schedulerMainThread: Scheduler,
                          private val modelMapper: GuiModelMapper) :
    RxViewModel() {


    private val _errorState = MutableLiveData<Throwable>()
    // expose read only workoutState
    val errorState: LiveData<Throwable>
        get() = _errorState

    private val _saveState = MutableLiveData<Boolean>()
    // expose read only workoutState
    val saveState: LiveData<Boolean>
        get() = _saveState

    fun saveWorkout(guiModel: WorkoutGuiModel) {
        addDisposable(
            useCaseFactory.createAddWorkoutUseCase().execute(modelMapper.mapGuiToDomain(guiModel))
                .subscribeOn(schedulerIo)
                .observeOn(schedulerMainThread)
                .subscribe(
                    this::handleSuccess,
                    this::handleError
                )
        )
    }

    private fun handleSuccess() {
        _saveState.value = true
    }

    private fun handleError(error: Throwable) {
        _errorState.value = error
    }

}
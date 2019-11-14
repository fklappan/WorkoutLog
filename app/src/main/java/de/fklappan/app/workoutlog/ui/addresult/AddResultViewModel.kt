package de.fklappan.app.workoutlog.ui.addresult

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.fklappan.app.workoutlog.common.GuiModelMapper
import de.fklappan.app.workoutlog.common.RxViewModel
import de.fklappan.app.workoutlog.common.UseCasesFactory
import de.fklappan.app.workoutlog.domain.WorkoutDetailsDomainModel
import de.fklappan.app.workoutlog.ui.detailviewworkout.WorkoutResultGuiModel
import io.reactivex.Scheduler

class AddResultViewModel(private val useCaseFactory: UseCasesFactory,
                         private val schedulerIo: Scheduler,
                         private val schedulerMainThread: Scheduler,
                         private val modelMapper: GuiModelMapper) :
    RxViewModel() {

    private val _state = MutableLiveData<AddResultState>()
    // expose read only workoutState
    val state: LiveData<AddResultState>
        get() = _state

    fun saveResult(guiModel: WorkoutResultGuiModel) {
        addDisposable(
            useCaseFactory.createAddWorkoutResultUseCase().execute(modelMapper.mapGuiToDomain(guiModel))
                .subscribeOn(schedulerIo)
                .observeOn(schedulerMainThread)
                .subscribe(
                    this::handleSuccess,
                    this::handleError
                )
        )
    }

    fun loadWorkout(workoutId: Int) {
        _state.value = AddResultState.Loading
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

    private fun workoutDetailsLoaded(domainModel: WorkoutDetailsDomainModel) {
        _state.value = AddResultState.WorkoutDetails(GuiModelMapper().mapDomainToGui(domainModel))
    }

    private fun handleSuccess() {
        _state.value = AddResultState.Save
    }

    private fun handleError(error: Throwable) {
        _state.value = AddResultState.Error(error.localizedMessage)
    }
}
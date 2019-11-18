package de.fklappan.app.workoutlog.ui.addresult

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.fklappan.app.workoutlog.common.GuiModelMapper
import de.fklappan.app.workoutlog.common.RxViewModel
import de.fklappan.app.workoutlog.common.UseCasesFactory
import de.fklappan.app.workoutlog.domain.WorkoutDetailsDomainModel
import de.fklappan.app.workoutlog.ui.detailviewworkout.WorkoutDetailsGuiModel
import de.fklappan.app.workoutlog.ui.detailviewworkout.WorkoutResultGuiModel
import io.reactivex.Scheduler
import java.util.*

class AddResultViewModel(private val useCaseFactory: UseCasesFactory,
                         private val schedulerIo: Scheduler,
                         private val schedulerMainThread: Scheduler,
                         private val modelMapper: GuiModelMapper) :
    RxViewModel() {

    private var currentdate = Date()
    private lateinit var currentWorkout: WorkoutDetailsGuiModel
    private var workoutId = 0
    private var isPr = false

    private val _state = MutableLiveData<AddResultState>()
    // expose read only workoutState
    val state: LiveData<AddResultState>
        get() = _state

    fun initialize(workoutId: Int) {
        if (this.workoutId > 0) {
            // set last known state
            _state.value = AddResultState.Data(currentWorkout, currentdate, isPr)
        } else {
            loadWorkout(workoutId)
        }
    }

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

    private fun loadWorkout(workoutId: Int) {
        this.workoutId = workoutId
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

    fun onSaveClicked(content: String, note: String, feeling: String) {
        saveResult(WorkoutResultGuiModel(
            0,
            content,
            currentdate,
            workoutId,
            isPr,
            note,
            feeling
        ))
    }

    fun onDateSelected(year: Int, month: Int, day: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        currentdate = calendar.time
        _state.value = AddResultState.DateSelected(currentdate)
    }

    fun onPrClicked() {
        isPr = !isPr
        _state.value = AddResultState.PrChanged(isPr)
    }

    private fun workoutDetailsLoaded(domainModel: WorkoutDetailsDomainModel) {
        currentWorkout = GuiModelMapper().mapDomainToGui(domainModel)
        _state.value = AddResultState.Data(currentWorkout, currentdate, false)
    }

    private fun handleSuccess() {
        _state.value = AddResultState.WorkoutSaved
    }

    private fun handleError(error: Throwable) {
        _state.value = AddResultState.Error(error.localizedMessage)
    }
}
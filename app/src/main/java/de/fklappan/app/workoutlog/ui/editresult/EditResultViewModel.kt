package de.fklappan.app.workoutlog.ui.editresult

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.fklappan.app.workoutlog.common.GuiModelMapper
import de.fklappan.app.workoutlog.common.RxViewModel
import de.fklappan.app.workoutlog.common.UseCasesFactory
import de.fklappan.app.workoutlog.domain.WorkoutResultDomainModel
import de.fklappan.app.workoutlog.ui.detailviewworkout.WorkoutResultGuiModel
import io.reactivex.Scheduler
import java.util.*

class EditResultViewModel(private val useCaseFactory: UseCasesFactory,
                          private val schedulerIo: Scheduler,
                          private val schedulerMainThread: Scheduler,
                          private val modelMapper: GuiModelMapper) :
    RxViewModel() {

    private lateinit var currentResult: WorkoutResultGuiModel

    private val _state = MutableLiveData<EditResultState>()
    // expose read only workoutState
    val state: LiveData<EditResultState>
        get() = _state

    fun loadResult(resultId: Int) {
        _state.value = EditResultState.Loading
        addDisposable(
            useCaseFactory.createGetResultDetailsUseCase().execute(resultId)
                .subscribeOn(schedulerIo)
                .observeOn(schedulerMainThread)
                .subscribe(
                    this::resultLoaded,
                    this::handleError
                )
        )
    }

    private fun saveResult(guiModel: WorkoutResultGuiModel) {
        addDisposable(
            useCaseFactory.createEditResultUseCase().execute(modelMapper.mapGuiToDomain(guiModel))
                .subscribeOn(schedulerIo)
                .observeOn(schedulerMainThread)
                .subscribe(
                    this::handleSuccess,
                    this::handleError
                )
        )
    }

    fun onSaveClicked(content: String, note: String, feeling: String) {
        // text stuff is updated ad hoc when requested. date and pr is already updated through events
        currentResult.score = content
        currentResult.note = note
        currentResult.feeling = feeling
        saveResult(currentResult)
    }

    fun onDateSelected(year: Int, month: Int, day: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        currentResult.date = calendar.time
        _state.value = EditResultState.DateSelected(currentResult.date)
    }

    fun onPrClicked() {
        currentResult.pr = !currentResult.pr
        _state.value = EditResultState.PrChanged(currentResult.pr)
    }

    private fun resultLoaded(domainModel: WorkoutResultDomainModel) {
        currentResult = GuiModelMapper().mapDomainToGui(domainModel)
        _state.value = EditResultState.Data(currentResult)
    }

    private fun handleSuccess() {
        _state.value = EditResultState.ResultSaved
    }

    private fun handleError(error: Throwable) {
        _state.value = EditResultState.Error(error.localizedMessage)
    }
}
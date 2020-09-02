package de.fklappan.app.workoutlog.ui.detailviewresult

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.fklappan.app.workoutlog.common.GuiModelMapper
import de.fklappan.app.workoutlog.common.LOG_TAG
import de.fklappan.app.workoutlog.common.RxViewModel
import de.fklappan.app.workoutlog.common.UseCasesFactory
import de.fklappan.app.workoutlog.domain.WorkoutResultDomainModel
import de.fklappan.app.workoutlog.ui.detailviewworkout.WorkoutResultGuiModel
import io.reactivex.Scheduler

class DetailviewResultViewModel(private val useCaseFactory: UseCasesFactory,
                                private val schedulerIo: Scheduler,
                                private val schedulerMainThread: Scheduler,
                                private val modelMapper: GuiModelMapper
) : RxViewModel() {

    private lateinit var currentResultDetails: WorkoutResultGuiModel

    private val _state = MutableLiveData<DetailviewResultState>()
    val state: LiveData<DetailviewResultState>
        get() = _state

    fun loadResult(resultId: Int) {
        _state.value = DetailviewResultState.Loading
        addDisposable(
            useCaseFactory.createGetResultDetailsUseCase().execute(resultId)
                .subscribeOn(schedulerIo)
                .observeOn(schedulerMainThread)
                .subscribe(
                    this::resultDetailsLoaded,
                    this::handleError
                )
        )
    }

    private fun resultDetailsLoaded(resultDetails: WorkoutResultDomainModel) {
        // map domain model to gui model
        currentResultDetails = modelMapper.mapDomainToGui(resultDetails)
        _state.value = DetailviewResultState.ResultDetails(currentResultDetails)
    }

    private fun handleError(error: Throwable) {
        Log.e(LOG_TAG, "Error while executing request", error)
        _state.value = DetailviewResultState.Error(error.localizedMessage)
    }

}
package de.fklappan.app.workoutlog.ui.overviewstatistic

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.fklappan.app.workoutlog.common.GuiModelMapper
import de.fklappan.app.workoutlog.common.LOG_TAG
import de.fklappan.app.workoutlog.common.RxViewModel
import de.fklappan.app.workoutlog.common.UseCasesFactory
import de.fklappan.app.workoutlog.domain.StatisticCurrentDomainModel
import io.reactivex.Scheduler
import java.util.*

class OverviewStatisticViewModel(private val useCaseFactory: UseCasesFactory,
                                 private val schedulerIo: Scheduler,
                                 private val schedulerMainThread: Scheduler,
                                 private val modelMapper: GuiModelMapper) :
    RxViewModel() {

    private val _state = MutableLiveData<OverviewStatisticState>()
    val state: LiveData<OverviewStatisticState>
        get() = _state

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // public methods aka entry points - will be invoked by the view

    fun loadData() {
        _state.value = OverviewStatisticState.Loading
        addDisposable(
            useCaseFactory.createGetStatisticUseCase().execute(Calendar.getInstance())
                .subscribeOn(schedulerIo)
                .observeOn(schedulerMainThread)
                .subscribe(
                    this::handleSuccess,
                    this::handleError
                )
        )
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // private methods

    private fun handleSuccess(statisticCurrentDomainModel: StatisticCurrentDomainModel) {
        // map domain model to gui model
        _state.value = OverviewStatisticState.Statistic(modelMapper.mapDomainToGui(statisticCurrentDomainModel))
    }

    private fun handleError(error: Throwable) {
        Log.e(LOG_TAG, "Error while loading workouts", error)
        _state.value = OverviewStatisticState.Error(error.localizedMessage)
    }
}
package de.fklappan.app.workoutlog.ui.overviewstatistic

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.fklappan.app.workoutlog.common.GuiModelMapper
import de.fklappan.app.workoutlog.common.LOG_TAG
import de.fklappan.app.workoutlog.common.RxViewModel
import de.fklappan.app.workoutlog.common.UseCasesFactory
import de.fklappan.app.workoutlog.domain.StatisticCurrentDomainModel
import de.fklappan.app.workoutlog.domain.WorkoutDomainModel
import de.fklappan.app.workoutlog.domain.WorkoutLogRepository
import de.fklappan.app.workoutlog.domain.usecases.GetStatisticUseCase
import de.fklappan.app.workoutlog.ui.overviewworkout.WorkoutGuiModel
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

class OverviewStatisticViewModel(private val useCaseFactory: UseCasesFactory,
                                 private val schedulerIo: Scheduler,
                                 private val schedulerMainThread: Scheduler,
                                 private val modelMapper: GuiModelMapper) :
    RxViewModel() {

    private val _statisticCurrent = MutableLiveData<StatisticCurrentGuiModel>()
    val statisticCurrent: LiveData<StatisticCurrentGuiModel>
        get() = _statisticCurrent

    // exposing single workouts to be able to update it independently
    private val _statisticPerYear = MutableLiveData<WorkoutGuiModel>()
    val statisticPerYear: LiveData<WorkoutGuiModel>
        get() = _statisticPerYear

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // public methods aka entry points - will be invoked by the view

    fun loadData() {
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
        _statisticCurrent.value = modelMapper.mapDomainToGui(statisticCurrentDomainModel)
    }

    private fun handleError(error: Throwable) {
        Log.e(LOG_TAG, "Error while loading workouts", error)
    }

    private fun handleSuccessFavoriteUseCase(workoutDomainModel: WorkoutDomainModel) {
//        _updateStream.value = modelMapper.mapDomainToGui(workoutDomainModel)
    }

    private fun handleErrorFavoriteUseCase(error: Throwable) {
        // at least log the error. Maybe in the future we need to present it to the user
        Log.e(LOG_TAG, "Error while executing toggle favorite", error)
    }
}
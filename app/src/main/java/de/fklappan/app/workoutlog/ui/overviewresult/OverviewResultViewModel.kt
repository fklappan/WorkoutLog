package de.fklappan.app.workoutlog.ui.overviewresult

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.fklappan.app.workoutlog.common.GuiModelMapper
import de.fklappan.app.workoutlog.common.LOG_TAG
import de.fklappan.app.workoutlog.common.RxViewModel
import de.fklappan.app.workoutlog.common.UseCasesFactory
import de.fklappan.app.workoutlog.domain.OverviewResultDomainModel
import io.reactivex.Scheduler
import java.util.*
import kotlin.collections.ArrayList

class OverviewResultViewModel(private val useCaseFactory: UseCasesFactory,
                              private val schedulerIo: Scheduler,
                              private val schedulerMainThread: Scheduler,
                              private val modelMapper: GuiModelMapper)
    : RxViewModel() {

    // store the unfiltered list to be able to restore it after deleting a filter
    private val _workoutListUnfiltered = ArrayList<OverviewResultGuiModel>()

    // exposing only a state
    private val _state = MutableLiveData<OverviewResultState>()
    val state: LiveData<OverviewResultState>
        get() = _state

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // public methods aka entry points - will be invoked by the view

    fun init() {
        if (_workoutListUnfiltered.isEmpty()) {
            loadWorkouts()
        } else {
            _state.value = OverviewResultState.ResultList(_workoutListUnfiltered)
        }
    }

    private fun loadWorkouts() {
        _state.value = OverviewResultState.Loading
        addDisposable(
            useCaseFactory.createGetResultsUseCase().execute(Calendar.getInstance())
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

    private fun handleSuccess(workoutList: List<OverviewResultDomainModel>) {
        // map domain model to gui model
        val guiModelList = ArrayList<OverviewResultGuiModel>()
        for (domainWorkout in workoutList) {
            guiModelList.add(modelMapper.mapDomainToGui(domainWorkout))
        }
        _workoutListUnfiltered.clear()
        _workoutListUnfiltered.addAll(guiModelList)
        _state.value = OverviewResultState.ResultList(guiModelList)
    }

    private fun handleError(error: Throwable) {
        Log.e(LOG_TAG, "Error while loading workouts", error)
        _state.value = OverviewResultState.Error(error.localizedMessage)
    }
}
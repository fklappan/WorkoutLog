package de.fklappan.app.workoutlog.ui.detailview

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.fklappan.app.workoutlog.common.LOG_TAG
import de.fklappan.app.workoutlog.common.GuiModelMapper
import de.fklappan.app.workoutlog.common.RxViewModel
import de.fklappan.app.workoutlog.domain.WorkoutDetailsDomainModel
import de.fklappan.app.workoutlog.domain.WorkoutLogRepository
import de.fklappan.app.workoutlog.domain.WorkoutResultDomainModel
import de.fklappan.app.workoutlog.domain.usecases.AddWorkoutResultUseCase
import de.fklappan.app.workoutlog.domain.usecases.GetWorkoutDetailsUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class DetailviewWorkoutViewModel(private val repository: WorkoutLogRepository, private val modelMapper: GuiModelMapper) : RxViewModel() {

    private val _state = MutableLiveData<DetailviewWorkoutState>()
    // expose read only workoutState
    val state: LiveData<DetailviewWorkoutState>
        get() = _state

    fun loadWorkout(workoutId: Int) {
        addDisposable(GetWorkoutDetailsUseCase(repository).execute(workoutId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe{ _state.value = DetailviewWorkoutState(null, null, true)}
            .subscribe(
                this::loadResults,
                this::handleError
            ))
    }

    private fun handleSuccess() {
        Log.d(LOG_TAG, "Success")
    }

    private fun handleErrorSave(error: Throwable) {
        Log.d(LOG_TAG, "Error")
    }


    private fun loadResults(workoutDetails: WorkoutDetailsDomainModel) {
        // map domain model to gui model
        val guiModel = modelMapper.mapDomainToGui(workoutDetails)
        _state.value = DetailviewWorkoutState(guiModel, null, false)
    }

    private fun handleError(error: Throwable) {
        _state.value = DetailviewWorkoutState(null, error, false)
    }

}
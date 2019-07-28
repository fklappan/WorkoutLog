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
import de.fklappan.app.workoutlog.domain.usecases.ToggleFavoriteWorkoutUseCase
import de.fklappan.app.workoutlog.ui.overviewworkout.WorkoutGuiModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class DetailviewWorkoutViewModel(private val repository: WorkoutLogRepository, private val modelMapper: GuiModelMapper) : RxViewModel() {

    private lateinit var currentWorkoutDetails: WorkoutDetailsGuiModel

    private val _state = MutableLiveData<DetailviewWorkoutState>()
    // expose read only workoutState
    val state: LiveData<DetailviewWorkoutState>
        get() = _state

    private val _updateStream = MutableLiveData<WorkoutGuiModel>()
    // expose read only
    val updateStream: LiveData<WorkoutGuiModel>
        get() = _updateStream

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


    fun favoriteClicked() {
        addDisposable(
            ToggleFavoriteWorkoutUseCase(repository).execute(currentWorkoutDetails.workout.workoutId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { workout ->
                    _updateStream.value = modelMapper.mapDomainToGui(workout)
                })
    }


    private fun handleSuccess() {
        Log.d(LOG_TAG, "Success")
    }

    private fun handleErrorSave(error: Throwable) {
        Log.d(LOG_TAG, "Error")
    }


    private fun loadResults(workoutDetails: WorkoutDetailsDomainModel) {
        // map domain model to gui model
        currentWorkoutDetails = modelMapper.mapDomainToGui(workoutDetails)
        _state.value = DetailviewWorkoutState(currentWorkoutDetails, null, false)
    }

    private fun handleError(error: Throwable) {
        _state.value = DetailviewWorkoutState(null, error, false)
    }

}
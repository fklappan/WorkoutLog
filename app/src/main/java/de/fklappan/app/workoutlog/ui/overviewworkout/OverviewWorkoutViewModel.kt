package de.fklappan.app.workoutlog.ui.overviewworkout

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.fklappan.app.workoutlog.common.GuiModelMapper
import de.fklappan.app.workoutlog.common.LOG_TAG
import de.fklappan.app.workoutlog.common.RxViewModel
import de.fklappan.app.workoutlog.domain.WorkoutDomainModel
import de.fklappan.app.workoutlog.domain.WorkoutLogRepository
import de.fklappan.app.workoutlog.domain.usecases.GetWorkoutsUseCase
import de.fklappan.app.workoutlog.domain.usecases.ToggleFavoriteWorkoutUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.collections.ArrayList

class OverviewWorkoutViewModel(private val repository: WorkoutLogRepository, private val modelMapper: GuiModelMapper) : RxViewModel() {

    // exposing the whole list for initial data fetching or hard reload
    private val _workoutList = MutableLiveData<MutableList<WorkoutGuiModel>>()
    val workoutList: LiveData<MutableList<WorkoutGuiModel>>
        get() = _workoutList

    // exposing single workouts to be able to update it independently
    private val _updateStream = MutableLiveData<WorkoutGuiModel>()
    val updateStream: LiveData<WorkoutGuiModel>
        get() = _updateStream

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // public methods aka entry points - will be invoked by the view

    fun loadWorkouts() {
        addDisposable(GetWorkoutsUseCase(repository).execute()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                this::handleSuccess,
                this::handleError
            ))
    }

    fun favoriteClicked(workoutId: Int) {
        addDisposable(ToggleFavoriteWorkoutUseCase(repository).execute(workoutId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                this::handleSuccessFavoriteUseCase,
                this::handleErrorFavoriteUseCase
            ))
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // private methods

    private fun handleSuccess(workoutList: List<WorkoutDomainModel>) {
        // map domain model to gui model
        val guiModelList = ArrayList<WorkoutGuiModel>()
        for (domainWorkout in workoutList) {
            guiModelList.add(modelMapper.mapDomainToGui(domainWorkout))
        }
        _workoutList.value = guiModelList
    }

    private fun handleError(error: Throwable) {
        Log.e(LOG_TAG, "Error while loading workouts", error)
    }

    private fun handleSuccessFavoriteUseCase(workoutDomainModel: WorkoutDomainModel) {
        _updateStream.value = modelMapper.mapDomainToGui(workoutDomainModel)
    }

    private fun handleErrorFavoriteUseCase(error: Throwable) {
        // at least log the error. Maybe in the future we need to present it to the user
        Log.e(LOG_TAG, "Error while executing toggle favorite", error)
    }
}
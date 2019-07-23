package de.fklappan.app.workoutlog.ui.overviewworkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.fklappan.app.workoutlog.common.GuiModelMapper
import de.fklappan.app.workoutlog.common.RxViewModel
import de.fklappan.app.workoutlog.domain.WorkoutDomainModel
import de.fklappan.app.workoutlog.domain.WorkoutLogRepository
import de.fklappan.app.workoutlog.domain.usecases.GetWorkoutsUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.collections.ArrayList

class OverviewWorkoutViewModel(private val repository: WorkoutLogRepository, private val modelMapper: GuiModelMapper) : RxViewModel() {

    private val _state = MutableLiveData<OverviewWorkoutState>()
    // expose read only workoutState
    val workoutState: LiveData<OverviewWorkoutState>
        get() = _state

    fun loadWorkouts() {
        addDisposable(GetWorkoutsUseCase(repository).execute()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe{ _state.value = OverviewWorkoutState(Collections.emptyList(), null, true)}
            .subscribe(
                this::handleSuccess,
                this::handleError
            ))
    }

    private fun handleSuccess(workoutList: List<WorkoutDomainModel>) {
        // map domain model to gui model
        val guiModelList = ArrayList<WorkoutGuiModel>()
        for (domainWorkout in workoutList) {
            guiModelList.add(modelMapper.mapDomainToGui(domainWorkout))
        }
        _state.value = OverviewWorkoutState(guiModelList, null, false)
    }

    private fun handleError(error: Throwable) {
        _state.value = OverviewWorkoutState(Collections.emptyList(), error, false)
    }

}
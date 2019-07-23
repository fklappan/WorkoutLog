package de.fklappan.app.workoutlog.ui.addworkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.fklappan.app.workoutlog.common.GuiModelMapper
import de.fklappan.app.workoutlog.common.RxViewModel
import de.fklappan.app.workoutlog.domain.WorkoutLogRepository
import de.fklappan.app.workoutlog.domain.usecases.AddWorkoutUseCase
import de.fklappan.app.workoutlog.ui.overviewworkout.WorkoutGuiModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AddWorkoutViewModel(private val repository: WorkoutLogRepository, private val modelMapper: GuiModelMapper) : RxViewModel() {

    private val _state = MutableLiveData<AddWorkoutState>()
    // expose read only workoutState
    val state: LiveData<AddWorkoutState>
        get() = _state

    fun saveWorkout(guiModel: WorkoutGuiModel) {
        addDisposable(AddWorkoutUseCase(repository).execute(modelMapper.mapGuiToDomain(guiModel))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                this::handleSuccess,
                this::handleError
            ))
    }

    private fun handleSuccess() {
        _state.value = AddWorkoutState(null, true)
    }

    private fun handleError(error: Throwable) {
        _state.value = AddWorkoutState(error, false)
    }

}
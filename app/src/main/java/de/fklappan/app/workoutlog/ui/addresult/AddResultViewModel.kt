package de.fklappan.app.workoutlog.ui.addresult

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.fklappan.app.workoutlog.common.GuiModelMapper
import de.fklappan.app.workoutlog.common.RxViewModel
import de.fklappan.app.workoutlog.ui.detailview.WorkoutResultGuiModel
import de.fklappan.app.workoutlog.domain.WorkoutLogRepository
import de.fklappan.app.workoutlog.domain.usecases.AddWorkoutResultUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AddResultViewModel(private val repository: WorkoutLogRepository, private val modelMapper: GuiModelMapper) : RxViewModel() {

    private val _state = MutableLiveData<AddResultState>()
    // expose read only workoutState
    val state: LiveData<AddResultState>
        get() = _state

    fun saveResult(guiModel: WorkoutResultGuiModel) {
        addDisposable(AddWorkoutResultUseCase(repository).execute(modelMapper.mapGuiToDomain(guiModel))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                this::handleSuccess,
                this::handleError
            ))
    }

    private fun handleSuccess() {
        _state.value = AddResultState(null, true)
    }

    private fun handleError(error: Throwable) {
        _state.value = AddResultState(error, false)
    }

}
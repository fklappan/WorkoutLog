package de.fklappan.app.workoutlog.ui.addresult

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.fklappan.app.workoutlog.common.GuiModelMapper
import de.fklappan.app.workoutlog.common.RxViewModel
import de.fklappan.app.workoutlog.domain.WorkoutLogRepository
import de.fklappan.app.workoutlog.domain.usecases.AddWorkoutResultUseCase
import de.fklappan.app.workoutlog.ui.detailview.WorkoutResultGuiModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AddResultViewModel(private val repository: WorkoutLogRepository, private val modelMapper: GuiModelMapper) :
    RxViewModel() {

    private val _errorState = MutableLiveData<Throwable>()
    // expose read only workoutState
    val errorState: LiveData<Throwable>
        get() = _errorState

    private val _saveState = MutableLiveData<Boolean>()
    // expose read only workoutState
    val saveState: LiveData<Boolean>
        get() = _saveState

    fun saveResult(guiModel: WorkoutResultGuiModel) {
        addDisposable(
            AddWorkoutResultUseCase(repository).execute(modelMapper.mapGuiToDomain(guiModel))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    this::handleSuccess,
                    this::handleError
                )
        )
    }

    private fun handleSuccess() {
        _saveState.value = true
    }

    private fun handleError(error: Throwable) {
        _errorState.value = error
    }

}
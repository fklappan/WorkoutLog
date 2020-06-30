package de.fklappan.app.workoutlog.ui.overviewworkout

import android.util.Log
import de.fklappan.app.workoutlog.common.GuiModelMapper
import de.fklappan.app.workoutlog.common.LOG_TAG
import de.fklappan.app.workoutlog.common.RxViewModel
import de.fklappan.app.workoutlog.common.UseCasesFactory
import io.reactivex.Scheduler

class DeleteViewModel(private val useCaseFactory: UseCasesFactory,
                      private val schedulerIo: Scheduler,
                      private val schedulerMainThread: Scheduler,
                      private val modelMapper: GuiModelMapper
) :
    RxViewModel() {

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // public methods aka entry points - will be invoked by the view

    fun deleteWorkout(workoutId: Int) {
        addDisposable(
            useCaseFactory.createDeleteWorkoutUseCase().execute(workoutId)
                .subscribeOn(schedulerIo)
                .observeOn(schedulerMainThread)
                .subscribe(this::success, this::error)
        )
    }

    private fun success() {
        Log.d(LOG_TAG, "Deleted workout with results")
    }

    private fun error(throwable: Throwable) {
        Log.e(LOG_TAG, "Error deleting workouts", throwable)
    }
}


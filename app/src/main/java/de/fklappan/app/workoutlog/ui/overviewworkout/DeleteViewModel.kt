package de.fklappan.app.workoutlog.ui.overviewworkout

import android.util.Log
import de.fklappan.app.workoutlog.common.GuiModelMapper
import de.fklappan.app.workoutlog.common.LOG_TAG
import de.fklappan.app.workoutlog.common.RxViewModel
import de.fklappan.app.workoutlog.common.UseCasesFactory
import io.reactivex.Scheduler

/**
 * Dedicated ViewModel class to delete a workout.
 * This is a dedicated/standalone ViewModel for reusability. The delete process should also be
 * invokable from within the detailview of a workout.
 */
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
                .subscribe(this::deleteWorkoutSuccess, this::deleteWorkoutError)
        )
    }

    fun deleteResult(resultId: Int) {
        addDisposable(
            useCaseFactory.createDeleteResultUseCase().execute(resultId)
                .subscribeOn(schedulerIo)
                .observeOn(schedulerMainThread)
                .subscribe(this::deleteResultSuccess, this::deleteResultError)
        )
    }

    private fun deleteWorkoutSuccess() {
        Log.d(LOG_TAG, "Deleted workout with results")
    }

    private fun deleteWorkoutError(throwable: Throwable) {
        Log.e(LOG_TAG, "Error deleting workouts", throwable)
    }

    private fun deleteResultSuccess() {
        Log.d(LOG_TAG, "Deleted result")
    }

    private fun deleteResultError(throwable: Throwable) {
        Log.e(LOG_TAG, "Error deleting result", throwable)
    }
}


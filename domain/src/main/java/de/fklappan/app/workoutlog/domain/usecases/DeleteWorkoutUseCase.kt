package de.fklappan.app.workoutlog.domain.usecases

import de.fklappan.app.workoutlog.domain.WorkoutLogRepository
import io.reactivex.Completable

class DeleteWorkoutUseCase(val repository: WorkoutLogRepository) : OutputlessUseCase<Int> {
    override fun execute(param: Int): Completable {
        return Completable.create {
            val resultList = repository.getResultsForWorkout(param)
            for (result in resultList) {
                repository.deleteResult(result.resultId)
            }
            repository.deleteWorkout(param)
            if (!it.isDisposed) {
                it.onComplete()
            }
        }
    }
}
package de.fklappan.app.workoutlog.domain.usecases

import de.fklappan.app.workoutlog.domain.WorkoutLogRepository
import io.reactivex.Completable

class DeleteResultUseCase(val repository: WorkoutLogRepository) : OutputlessUseCase<Int> {
    override fun execute(param: Int): Completable {
        return Completable.create {
            repository.deleteResult(param)
            if (!it.isDisposed) {
                it.onComplete()
            }
        }
    }
}
package de.fklappan.app.workoutlog.domain.usecases

import de.fklappan.app.workoutlog.domain.WorkoutLogRepository
import de.fklappan.app.workoutlog.domain.WorkoutResultDomainModel
import io.reactivex.Completable

class EditResultUseCase(val repository: WorkoutLogRepository) : OutputlessUseCase<WorkoutResultDomainModel> {
    override fun execute(param: WorkoutResultDomainModel): Completable {
        repository.updateResult(param)
        return Completable.complete()
    }

}
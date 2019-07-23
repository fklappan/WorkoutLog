package de.fklappan.app.workoutlog.domain.usecases

import de.fklappan.app.workoutlog.domain.WorkoutDomainModel
import de.fklappan.app.workoutlog.domain.WorkoutLogRepository
import de.fklappan.app.workoutlog.domain.WorkoutResultDomainModel
import io.reactivex.Completable

class AddWorkoutResultUseCase(val repository: WorkoutLogRepository) : OutputlessUseCase<WorkoutResultDomainModel> {
    override fun execute(param: WorkoutResultDomainModel): Completable {
        repository.addResult(param.workout, param)
        return Completable.complete()
    }

}
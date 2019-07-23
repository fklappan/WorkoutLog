package de.fklappan.app.workoutlog.domain.usecases

import de.fklappan.app.workoutlog.domain.WorkoutDomainModel
import de.fklappan.app.workoutlog.domain.WorkoutLogRepository
import io.reactivex.Completable

class AddWorkoutUseCase(val repository: WorkoutLogRepository) : OutputlessUseCase<WorkoutDomainModel> {
    override fun execute(param: WorkoutDomainModel): Completable {
        repository.addWorkout(param)
        return Completable.complete()
    }

}
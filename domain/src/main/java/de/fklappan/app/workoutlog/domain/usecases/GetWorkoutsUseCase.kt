package de.fklappan.app.workoutlog.domain.usecases

import de.fklappan.app.workoutlog.domain.WorkoutDomainModel
import de.fklappan.app.workoutlog.domain.WorkoutLogRepository
import io.reactivex.Single

class GetWorkoutsUseCase(val repository: WorkoutLogRepository) : InputlessUseCase<List<WorkoutDomainModel>> {

    override fun execute(): Single<List<WorkoutDomainModel>> {
        return Single.fromCallable { repository.getWorkouts() }
    }
}
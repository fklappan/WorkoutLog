package de.fklappan.app.workoutlog.domain.usecases

import de.fklappan.app.workoutlog.domain.WorkoutDetailsDomainModel
import de.fklappan.app.workoutlog.domain.WorkoutDomainModel
import de.fklappan.app.workoutlog.domain.WorkoutLogRepository
import io.reactivex.Single

class GetWorkoutUseCase(val repository: WorkoutLogRepository) : UseCase<Int,WorkoutDomainModel> {

    override fun execute(workoutId: Int): Single<WorkoutDomainModel> {
        val workoutDomainModel = repository.getWorkoutById(workoutId)
        return Single.just(workoutDomainModel)
    }
}
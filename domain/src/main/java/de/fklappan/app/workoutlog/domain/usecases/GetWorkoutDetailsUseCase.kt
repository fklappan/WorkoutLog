package de.fklappan.app.workoutlog.domain.usecases

import de.fklappan.app.workoutlog.domain.WorkoutDetailsDomainModel
import de.fklappan.app.workoutlog.domain.WorkoutDomainModel
import de.fklappan.app.workoutlog.domain.WorkoutLogRepository
import io.reactivex.Single

class GetWorkoutDetailsUseCase(val repository: WorkoutLogRepository) : UseCase<Int,WorkoutDetailsDomainModel> {

    override fun execute(workoutId: Int): Single<WorkoutDetailsDomainModel> {
        val workoutDomainModel = repository.getWorkoutById(workoutId)
        val workoutDetailsDomainModel = WorkoutDetailsDomainModel(workoutDomainModel, repository.getResultsForWorkout(workoutId))
        return Single.just(workoutDetailsDomainModel)
    }
}
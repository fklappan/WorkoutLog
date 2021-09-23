package de.fklappan.app.workoutlog.domain.usecases

import de.fklappan.app.workoutlog.domain.WorkoutDomainModel
import de.fklappan.app.workoutlog.domain.WorkoutLogRepository
import io.reactivex.Single

class ToggleFavoriteWorkoutUseCase(val repository: WorkoutLogRepository) : UseCase<Int,WorkoutDomainModel> {

    override fun execute(workoutId: Int): Single<WorkoutDomainModel> {
        return Single.fromCallable { internalExecute(workoutId) }
    }

    private fun internalExecute(workoutId: Int) : WorkoutDomainModel{
        val workoutDomainModel = repository.getWorkoutById(workoutId)
        workoutDomainModel.favorite = !workoutDomainModel.favorite
        repository.updateWorkout(workoutDomainModel)
        return workoutDomainModel
    }
}
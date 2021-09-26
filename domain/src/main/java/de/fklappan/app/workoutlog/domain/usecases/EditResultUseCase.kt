package de.fklappan.app.workoutlog.domain.usecases

import de.fklappan.app.workoutlog.domain.WorkoutLogRepository
import de.fklappan.app.workoutlog.domain.WorkoutResultDomainModel
import io.reactivex.Completable

class EditResultUseCase(val repository: WorkoutLogRepository) : OutputlessUseCase<WorkoutResultDomainModel> {
    override fun execute(param: WorkoutResultDomainModel): Completable {
        return Completable.fromCallable { internalExecute(param)}
    }

    private fun internalExecute(param: WorkoutResultDomainModel) {
        if (param.pr) {
            // remove pr from old results
            val results = repository.getResultsForWorkout(param.workout)
            results.filter { it.pr }.forEach {
                it.pr = false
                repository.updateResult(it)
            }
        }
        repository.updateResult(param)
    }

}
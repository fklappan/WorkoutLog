package de.fklappan.app.workoutlog.domain.usecases

import de.fklappan.app.workoutlog.domain.WorkoutLogRepository
import de.fklappan.app.workoutlog.domain.WorkoutResultDomainModel
import io.reactivex.Single

class GetResultDetailsUseCase(val repository: WorkoutLogRepository) : UseCase<Int,WorkoutResultDomainModel> {

    override fun execute(resultId: Int): Single<WorkoutResultDomainModel> {
        val resultDomainModel = repository.getResultById(resultId)
        return Single.just(resultDomainModel)
    }
}
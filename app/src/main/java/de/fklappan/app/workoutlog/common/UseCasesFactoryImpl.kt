package de.fklappan.app.workoutlog.common

import de.fklappan.app.workoutlog.domain.WorkoutLogRepository
import de.fklappan.app.workoutlog.domain.usecases.*

/**
 * Implementation of the UseCasesFactory. Methods simply create and return the requested UseCase instance.
 */
class UseCasesFactoryImpl(val repository: WorkoutLogRepository) : UseCasesFactory {

    override fun createToggleFavoriteWorkoutUseCase(): ToggleFavoriteWorkoutUseCase = ToggleFavoriteWorkoutUseCase(repository)

    override fun createGetWorkoutsUseCase() : GetWorkoutsUseCase = GetWorkoutsUseCase(repository)

    override fun createAddWorkoutResultUseCase(): AddWorkoutResultUseCase = AddWorkoutResultUseCase(repository)

    override fun createAddWorkoutUseCase(): AddWorkoutUseCase = AddWorkoutUseCase(repository)

    override fun createGetWorkoutDetailsUseCase(): GetWorkoutDetailsUseCase = GetWorkoutDetailsUseCase(repository)

    override fun createGetResultDetailsUseCase(): GetResultDetailsUseCase = GetResultDetailsUseCase(repository)

    override fun createGetWorkoutUseCase(): GetWorkoutUseCase = GetWorkoutUseCase(repository)

    override fun createEditWorkoutUseCase(): EditWorkoutUseCase = EditWorkoutUseCase(repository)

    override fun createGetStatisticUseCase(): GetStatisticUseCase = GetStatisticUseCase(repository)

    override fun createGetResultsUseCase(): GetResultsUseCase = GetResultsUseCase(repository)

    override fun createDeleteWorkoutUseCase(): DeleteWorkoutUseCase = DeleteWorkoutUseCase(repository)
}
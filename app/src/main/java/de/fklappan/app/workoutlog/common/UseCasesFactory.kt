package de.fklappan.app.workoutlog.common

import de.fklappan.app.workoutlog.domain.usecases.*

/**
 * Factory for creation of UseCase instances.
 */
interface UseCasesFactory {
    fun createGetWorkoutsUseCase() : GetWorkoutsUseCase
    fun createGetWorkoutDetailsUseCase() : GetWorkoutDetailsUseCase
    fun createGetResultDetailsUseCase() : GetResultDetailsUseCase
    fun createToggleFavoriteWorkoutUseCase() : ToggleFavoriteWorkoutUseCase
    fun createAddWorkoutResultUseCase() : AddWorkoutResultUseCase
    fun createAddWorkoutUseCase() : AddWorkoutUseCase
    fun createGetWorkoutUseCase() : GetWorkoutUseCase
    fun createEditWorkoutUseCase() : EditWorkoutUseCase
    fun createEditResultUseCase() : EditResultUseCase
    fun createGetStatisticUseCase() : GetStatisticUseCase
    fun createGetResultsUseCase() : GetResultsUseCase
    fun createDeleteWorkoutUseCase() : DeleteWorkoutUseCase
    fun createDeleteResultUseCase() : DeleteResultUseCase
}
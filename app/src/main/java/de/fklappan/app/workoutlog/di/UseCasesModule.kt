package de.fklappan.app.workoutlog.di

import dagger.Module
import dagger.Provides
import de.fklappan.app.workoutlog.common.UseCasesFactory
import de.fklappan.app.workoutlog.common.UseCasesFactoryImpl
import de.fklappan.app.workoutlog.domain.WorkoutLogRepository

/**
 * Module for providing the UseCasesFactor
 */
@Module
class UseCasesModule {

    @Provides
    fun provideUseCasesFactory(repository: WorkoutLogRepository) : UseCasesFactory {
        return UseCasesFactoryImpl(repository)
    }

}
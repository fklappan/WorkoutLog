package de.fklappan.app.workoutlog.di

import android.content.Context
import dagger.Module
import dagger.Provides
import de.fklappan.app.workoutlog.common.GuiModelMapper
import de.fklappan.app.workoutlog.common.ViewModelFactory
import de.fklappan.app.workoutlog.data.AppDatabase
import de.fklappan.app.workoutlog.data.WorkoutLogRepositoryImpl
import de.fklappan.app.workoutlog.domain.WorkoutLogRepository

@Module
class ControllerModule(private val context: Context) {

    @Provides
    fun provideContext() = context

    @Provides
    fun provideRepository(appDatabase: AppDatabase): WorkoutLogRepository {
        return WorkoutLogRepositoryImpl(appDatabase.workoutDao(), appDatabase.resultDao())
//        return WorkoutLogRepositoryImpl(appDatabase.workoutDao(), appDatabase.resultDao())
    }

    @Provides
    fun provideViewModelFactory(repository: WorkoutLogRepository, guiModelMapper: GuiModelMapper): ViewModelFactory {
        return ViewModelFactory(repository, guiModelMapper)
    }
}
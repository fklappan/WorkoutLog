package de.fklappan.app.workoutlog.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import de.fklappan.app.workoutlog.common.AndroidLoggerImpl
import de.fklappan.app.workoutlog.data.AppDatabase
import de.fklappan.app.workoutlog.domain.Logger
import javax.inject.Singleton

@Module
class ApplicationModule(private var application: Application) {

    @Provides
    fun provideApplication(): Application = application

    @Provides
    fun provideApplicationContext(): Context = application.applicationContext

    @Provides
    @Singleton
    fun provideLogger(androidLoggerImpl: AndroidLoggerImpl): Logger = androidLoggerImpl

    @Provides
    fun provideDatabase(context: Context): AppDatabase = AppDatabase.getInstance(context)

}
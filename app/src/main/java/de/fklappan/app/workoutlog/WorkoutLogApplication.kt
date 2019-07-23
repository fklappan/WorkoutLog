package de.fklappan.app.workoutlog

import android.app.Application
import de.fklappan.app.workoutlog.di.ApplicationComponent
import de.fklappan.app.workoutlog.di.ApplicationModule
import de.fklappan.app.workoutlog.di.DaggerApplicationComponent

class WorkoutLogApplication : Application() {

    private lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        applicationComponent = DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(this))
            .build()
    }

    public fun getApplicationComponent() : ApplicationComponent = applicationComponent
}
package de.fklappan.app.workoutlog.di

import dagger.Component

@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicationComponent {

    fun newControllerComponent(module: ControllerModule): ControllerComponent
}
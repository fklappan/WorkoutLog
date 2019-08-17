package de.fklappan.app.workoutlog.di

import dagger.Subcomponent
import de.fklappan.app.workoutlog.ui.addresult.AddResultFragment
import de.fklappan.app.workoutlog.ui.addworkout.AddWorkoutFragment
import de.fklappan.app.workoutlog.ui.addworkout.EditWorkoutFragment
import de.fklappan.app.workoutlog.ui.detailview.DetailviewWorkoutFragment
import de.fklappan.app.workoutlog.ui.overviewworkout.OverviewWorkoutFragment

@Subcomponent(modules = arrayOf(ControllerModule::class))
interface ControllerComponent {

    fun inject(workoutFragment: OverviewWorkoutFragment)
    fun inject(fragment: AddWorkoutFragment)
    fun inject(fragment: EditWorkoutFragment)
    fun inject(fragment: DetailviewWorkoutFragment)
    fun inject(fragment: AddResultFragment)
}
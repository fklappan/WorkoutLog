package de.fklappan.app.workoutlog.di

import dagger.Subcomponent
import de.fklappan.app.workoutlog.ui.addresult.AddResultFragment
import de.fklappan.app.workoutlog.ui.addworkout.AddWorkoutFragment
import de.fklappan.app.workoutlog.ui.detailviewworkout.DetailviewWorkoutFragment
import de.fklappan.app.workoutlog.ui.editworkout.EditWorkoutFragment
import de.fklappan.app.workoutlog.ui.overviewstatistic.OverviewStatisticFragment
import de.fklappan.app.workoutlog.ui.overviewworkout.OverviewWorkoutFragment

@Subcomponent(modules = arrayOf(
    ControllerModule::class,
    UseCasesModule::class,
    SchedulersModule::class))
interface ControllerComponent {

    fun inject(workoutFragment: OverviewWorkoutFragment)
    fun inject(fragment: AddWorkoutFragment)
    fun inject(fragment: EditWorkoutFragment)
    fun inject(fragment: DetailviewWorkoutFragment)
    fun inject(fragment: AddResultFragment)
    fun inject(fragment: OverviewStatisticFragment)
}
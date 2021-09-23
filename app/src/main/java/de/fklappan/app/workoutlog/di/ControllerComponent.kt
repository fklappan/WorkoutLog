package de.fklappan.app.workoutlog.di

import dagger.Subcomponent
import de.fklappan.app.workoutlog.ui.addresult.AddResultFragment
import de.fklappan.app.workoutlog.ui.addworkout.AddWorkoutFragment
import de.fklappan.app.workoutlog.ui.detailviewresult.DetailviewResultFragment
import de.fklappan.app.workoutlog.ui.detailviewworkout.DetailviewWorkoutFragment
import de.fklappan.app.workoutlog.ui.editresult.EditResultFragment
import de.fklappan.app.workoutlog.ui.editworkout.EditWorkoutFragment
import de.fklappan.app.workoutlog.ui.filter.FilterFragment
import de.fklappan.app.workoutlog.ui.overviewresult.OverviewResultFragment
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
    fun inject(fragment: OverviewResultFragment)
    fun inject(fragment: DetailviewResultFragment)
    fun inject(fragment: EditResultFragment)
    fun inject(fragment: FilterFragment)
}

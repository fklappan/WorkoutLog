package de.fklappan.app.workoutlog.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.fklappan.app.workoutlog.domain.WorkoutLogRepository
import de.fklappan.app.workoutlog.ui.addresult.AddResultViewModel
import de.fklappan.app.workoutlog.ui.addworkout.AddWorkoutViewModel
import de.fklappan.app.workoutlog.ui.detailview.DetailviewWorkoutViewModel
import de.fklappan.app.workoutlog.ui.editworkout.EditWorkoutViewModel
import de.fklappan.app.workoutlog.ui.overviewstatistic.OverviewStatisticViewModel
import de.fklappan.app.workoutlog.ui.overviewworkout.OverviewWorkoutViewModel
import io.reactivex.Scheduler

/**
 * Factory for creating the ViewModel instances.
 */
class ViewModelFactory(private val guiModelMapper: GuiModelMapper,
                       private val useCasesFactory: UseCasesFactory,
                       private val schedulerIo : Scheduler,
                       private val schedulerMainThread: Scheduler
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OverviewWorkoutViewModel::class.java)) {
            return OverviewWorkoutViewModel(useCasesFactory, schedulerIo, schedulerMainThread, guiModelMapper) as T
        }
        if (modelClass.isAssignableFrom(AddWorkoutViewModel::class.java)) {
            return AddWorkoutViewModel(useCasesFactory, schedulerIo, schedulerMainThread, guiModelMapper) as T
        }
        if (modelClass.isAssignableFrom(DetailviewWorkoutViewModel::class.java)) {
            return DetailviewWorkoutViewModel(useCasesFactory, schedulerIo, schedulerMainThread, guiModelMapper) as T
        }
        if (modelClass.isAssignableFrom(AddResultViewModel::class.java)) {
            return AddResultViewModel(useCasesFactory, schedulerIo, schedulerMainThread, guiModelMapper) as T
        }
        if (modelClass.isAssignableFrom(EditWorkoutViewModel::class.java)) {
            return EditWorkoutViewModel(useCasesFactory, schedulerIo, schedulerMainThread, guiModelMapper) as T
        }
        if (modelClass.isAssignableFrom(OverviewStatisticViewModel::class.java)) {
            return OverviewStatisticViewModel(useCasesFactory, schedulerIo, schedulerMainThread, guiModelMapper) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
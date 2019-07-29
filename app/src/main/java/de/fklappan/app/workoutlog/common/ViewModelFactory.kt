package de.fklappan.app.workoutlog.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.fklappan.app.workoutlog.domain.WorkoutLogRepository
import de.fklappan.app.workoutlog.ui.addresult.AddResultViewModel
import de.fklappan.app.workoutlog.ui.addworkout.AddWorkoutViewModel
import de.fklappan.app.workoutlog.ui.detailview.DetailviewWorkoutViewModel
import de.fklappan.app.workoutlog.ui.overviewworkout.OverviewWorkoutViewModel

class ViewModelFactory(private val repository: WorkoutLogRepository, private val guiModelMapper: GuiModelMapper) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OverviewWorkoutViewModel::class.java)) {
            return OverviewWorkoutViewModel(repository, guiModelMapper) as T
        }
        if (modelClass.isAssignableFrom(AddWorkoutViewModel::class.java)) {
            return AddWorkoutViewModel(repository, guiModelMapper) as T
        }
        if (modelClass.isAssignableFrom(DetailviewWorkoutViewModel::class.java)) {
            return DetailviewWorkoutViewModel(repository, guiModelMapper) as T
        }
        if (modelClass.isAssignableFrom(AddResultViewModel::class.java)) {
            return AddResultViewModel(repository, guiModelMapper) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
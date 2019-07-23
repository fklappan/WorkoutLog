package de.fklappan.app.workoutlog.ui.detailview

import de.fklappan.app.workoutlog.ui.overviewworkout.WorkoutGuiModel

data class WorkoutDetailsGuiModel(var workout: WorkoutGuiModel, var resultList: List<WorkoutResultGuiModel>)
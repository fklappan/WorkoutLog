package de.fklappan.app.workoutlog.ui.overviewresult

import de.fklappan.app.workoutlog.ui.detailviewworkout.WorkoutResultGuiModel
import de.fklappan.app.workoutlog.ui.overviewworkout.WorkoutGuiModel

data class OverviewResultGuiModel(
    val workout: WorkoutGuiModel,
    val result: WorkoutResultGuiModel
)



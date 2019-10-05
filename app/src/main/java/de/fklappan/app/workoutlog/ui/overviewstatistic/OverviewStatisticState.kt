package de.fklappan.app.workoutlog.ui.overviewstatistic

sealed class OverviewStatisticState {
    object Loading : OverviewStatisticState()
    data class Error(val message: String) : OverviewStatisticState()
    data class Statistic(val statistic: StatisticCurrentGuiModel) : OverviewStatisticState()
}
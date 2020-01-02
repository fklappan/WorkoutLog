package de.fklappan.app.workoutlog.ui.overviewresult

sealed class OverviewResultState {
    object Loading : OverviewResultState()
    data class Error(val message: String) : OverviewResultState()
    data class ResultList(val workoutResults: List<OverviewResultGuiModel>) : OverviewResultState()
}
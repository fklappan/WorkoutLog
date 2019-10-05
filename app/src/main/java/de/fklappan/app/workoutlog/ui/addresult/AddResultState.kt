package de.fklappan.app.workoutlog.ui.addresult


sealed class AddResultState {
    data class Error(val message: String) : AddResultState()
    object Save : AddResultState()
}
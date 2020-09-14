package de.fklappan.app.workoutlog.common

import android.content.Context
import androidx.annotation.NonNull
import com.google.android.material.snackbar.Snackbar
import de.fklappan.app.workoutlog.R

fun Snackbar.errorStyle(@NonNull context: Context) : Snackbar {
    this.setTextColor(context.resources.getColor(R.color.red, context.theme))
    return this
}


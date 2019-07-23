package de.fklappan.app.workoutlog.common

import android.content.Context
import androidx.fragment.app.Fragment
import dagger.Component
import de.fklappan.app.workoutlog.WorkoutLogApplication
import de.fklappan.app.workoutlog.di.ControllerComponent
import de.fklappan.app.workoutlog.di.ControllerModule

open class BaseFragment : Fragment() {

    private lateinit var controllerComponent: ControllerComponent
    private lateinit var appBarHeader: AppBarHeader

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context!!.applicationContext is WorkoutLogApplication) {
            val application: WorkoutLogApplication = context.applicationContext as WorkoutLogApplication
            controllerComponent = application.getApplicationComponent()
                .newControllerComponent(ControllerModule(context))
        }
        if (context is AppBarHeader) {
            appBarHeader = context
        }
    }

    fun getInjector() : ControllerComponent = controllerComponent

    fun getAppBarHeader() : AppBarHeader = appBarHeader
}
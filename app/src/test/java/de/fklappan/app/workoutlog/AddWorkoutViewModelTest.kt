package de.fklappan.app.workoutlog

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import de.fklappan.app.workoutlog.common.GuiModelMapper
import de.fklappan.app.workoutlog.common.UseCasesFactory
import de.fklappan.app.workoutlog.domain.StatisticCurrentDomainModel
import de.fklappan.app.workoutlog.domain.WorkoutDomainModel
import de.fklappan.app.workoutlog.domain.usecases.AddWorkoutUseCase
import de.fklappan.app.workoutlog.domain.usecases.EditWorkoutUseCase
import de.fklappan.app.workoutlog.domain.usecases.GetStatisticUseCase
import de.fklappan.app.workoutlog.domain.usecases.GetWorkoutUseCase
import de.fklappan.app.workoutlog.ui.addworkout.AddWorkoutState
import de.fklappan.app.workoutlog.ui.addworkout.AddWorkoutViewModel
import de.fklappan.app.workoutlog.ui.editworkout.EditWorkoutState
import de.fklappan.app.workoutlog.ui.editworkout.EditWorkoutViewModel
import de.fklappan.app.workoutlog.ui.overviewstatistic.OverviewStatisticState
import de.fklappan.app.workoutlog.ui.overviewstatistic.OverviewStatisticViewModel
import io.mockk.*
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AddWorkoutViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    // using trampoline for FIFO of thread scheduling
    private val scheduler = Schedulers.trampoline()
    private val factory = mockk<UseCasesFactory>()
    private val addWorkoutUseCase = mockk<AddWorkoutUseCase>()

    private lateinit var uut: AddWorkoutViewModel
    private val stateObserver = mockk<Observer<AddWorkoutState>>()

    @Before
    fun setup() {
        uut = AddWorkoutViewModel(factory, scheduler, scheduler, GuiModelMapper())
        uut.state.observeForever(stateObserver)

        every { stateObserver.onChanged(any()) } just Runs
    }

    @Test
    fun `test saveWorkout expecting correct state sequence`() {

        // given
        val workoutDomainModel = WorkoutDomainModel(1, "workout", false)
        val workoutGuiModel = GuiModelMapper().mapDomainToGui(workoutDomainModel)

        every { factory.createAddWorkoutUseCase() } returns addWorkoutUseCase
        every { addWorkoutUseCase.execute(workoutDomainModel) } returns Completable.complete()

        // when
        uut.saveWorkout(workoutGuiModel)

        //then
        verifySequence {
            stateObserver.onChanged(AddWorkoutState.Save)
        }
    }
}

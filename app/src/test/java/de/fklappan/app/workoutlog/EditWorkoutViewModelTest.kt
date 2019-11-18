package de.fklappan.app.workoutlog

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import de.fklappan.app.workoutlog.common.GuiModelMapper
import de.fklappan.app.workoutlog.common.UseCasesFactory
import de.fklappan.app.workoutlog.domain.WorkoutDomainModel
import de.fklappan.app.workoutlog.domain.usecases.EditWorkoutUseCase
import de.fklappan.app.workoutlog.domain.usecases.GetWorkoutUseCase
import de.fklappan.app.workoutlog.ui.editworkout.EditWorkoutState
import de.fklappan.app.workoutlog.ui.editworkout.EditWorkoutViewModel
import io.mockk.*
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class EditWorkoutViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    // using trampoline for FIFO of thread scheduling
    private val scheduler = Schedulers.trampoline()
    private val factory = mockk<UseCasesFactory>()
    private val getWorkoutUseCase = mockk<GetWorkoutUseCase>()
    private val editWorkoutUseCase = mockk<EditWorkoutUseCase>()

    private lateinit var uut: EditWorkoutViewModel
    private val stateObserver = mockk<Observer<EditWorkoutState>>()

    @Before
    fun setup() {
        uut = EditWorkoutViewModel(factory, scheduler, scheduler, GuiModelMapper())
        uut.state.observeForever(stateObserver)

        every { stateObserver.onChanged(any()) } just Runs
    }

    @Test
    fun `test loadWorkout then saveWorkout expecting correct state sequence`() {

        // given
        val workoutDomainModel = WorkoutDomainModel(1, "workout", false)
        val afterSaveDomainModel = WorkoutDomainModel(1, "other name", false)
        val workoutGuiModel = GuiModelMapper().mapDomainToGui(workoutDomainModel)

        every { factory.createGetWorkoutUseCase() } returns getWorkoutUseCase
        every { getWorkoutUseCase.execute(1) } returns Single.just(workoutDomainModel)

        every { factory.createEditWorkoutUseCase() } returns editWorkoutUseCase
        every { editWorkoutUseCase.execute(afterSaveDomainModel) } returns Completable.complete()

        // when
        uut.loadWorkout(1)
        uut.saveWorkout("other name")

        //then
        verifySequence {
            stateObserver.onChanged(EditWorkoutState.Loading)
            stateObserver.onChanged(EditWorkoutState.Workout(workoutGuiModel))
            stateObserver.onChanged(EditWorkoutState.Save)
        }
    }
}

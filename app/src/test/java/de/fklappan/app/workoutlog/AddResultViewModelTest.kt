package de.fklappan.app.workoutlog

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import de.fklappan.app.workoutlog.common.GuiModelMapper
import de.fklappan.app.workoutlog.common.UseCasesFactory
import de.fklappan.app.workoutlog.domain.WorkoutResultDomainModel
import de.fklappan.app.workoutlog.domain.usecases.AddWorkoutResultUseCase
import de.fklappan.app.workoutlog.ui.addresult.AddResultState
import de.fklappan.app.workoutlog.ui.addresult.AddResultViewModel
import io.mockk.*
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

class AddResultViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    // using trampoline for FIFO of thread scheduling
    private val scheduler = Schedulers.trampoline()
    private val factory = mockk<UseCasesFactory>()
    private val addWorkoutResultUseCase = mockk<AddWorkoutResultUseCase>()

    private lateinit var uut: AddResultViewModel
    private val stateObserver = mockk<Observer<AddResultState>>()

    @Before
    fun setup() {
        uut = AddResultViewModel(factory, scheduler, scheduler, GuiModelMapper())
        uut.state.observeForever(stateObserver)

        every { stateObserver.onChanged(any()) } just Runs
    }

    @Test
    fun `test saveResult expecting correct state sequence`() {

        // given
        val workoutResultDomainModel = WorkoutResultDomainModel(1, "score", Date(), 1, true, "notes", "feeling")
        val workoutResultGuiModel = GuiModelMapper().mapDomainToGui(workoutResultDomainModel)

        every { factory.createAddWorkoutResultUseCase() } returns addWorkoutResultUseCase
        every { addWorkoutResultUseCase.execute(workoutResultDomainModel) } returns Completable.complete()

        // when
        uut.saveResult(workoutResultGuiModel)

        //then
        verifySequence {
            stateObserver.onChanged(AddResultState.WorkoutSaved)
        }
    }
}

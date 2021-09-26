package de.fklappan.app.workoutlog

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import de.fklappan.app.workoutlog.common.GuiModelMapper
import de.fklappan.app.workoutlog.common.UseCasesFactory
import de.fklappan.app.workoutlog.domain.WorkoutDomainModel
import de.fklappan.app.workoutlog.domain.usecases.GetWorkoutsUseCase
import de.fklappan.app.workoutlog.domain.usecases.ToggleFavoriteWorkoutUseCase
import de.fklappan.app.workoutlog.ui.overviewworkout.OverviewWorkoutState
import de.fklappan.app.workoutlog.ui.overviewworkout.OverviewWorkoutViewModel
import de.fklappan.app.workoutlog.ui.overviewworkout.WorkoutGuiModel
import io.mockk.*
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*
import kotlin.collections.ArrayList

class OverviewWorkoutViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    // using trampoline for FIFO of thread scheduling
    private val scheduler = Schedulers.trampoline()
    private val factory = mockk<UseCasesFactory>(relaxed = true)
    private val getWorkoutsUseCaseMock = mockk<GetWorkoutsUseCase>()
    private val toggleFavoriteWorkoutUseCase = mockk<ToggleFavoriteWorkoutUseCase>()

    private lateinit var uut : OverviewWorkoutViewModel
    private val stateObserver = mockk<Observer<OverviewWorkoutState>>(relaxed = true)

    @Test
    fun `test loadworkouts expecting loading and one workout`() {

        // given
        val domainModel = WorkoutDomainModel(1, "workout", true)
        val domainModelList = arrayListOf(domainModel)
        val guiModelList = arrayListOf(GuiModelMapper().mapDomainToGui(domainModel))

        every { factory.createGetWorkoutsUseCase() } returns getWorkoutsUseCaseMock
        every { getWorkoutsUseCaseMock.execute() } returns Single.just(domainModelList)

        uut = OverviewWorkoutViewModel(factory, scheduler, scheduler, GuiModelMapper())
        uut.state.observeForever(stateObserver)

        every { stateObserver.onChanged(any())} just Runs

        uut.loadWorkouts()

        //then
        verifySequence {
            // first list is the repeated loaded result when initialising the ViewModel
            stateObserver.onChanged(OverviewWorkoutState.WorkoutList(guiModelList))
            // following the states which are sent after loadWorkouts()
            stateObserver.onChanged(OverviewWorkoutState.Loading)
            stateObserver.onChanged(OverviewWorkoutState.WorkoutList(guiModelList))
        }
    }

    @Test
    fun `test favoriteclicked expecting updatestream has value`() {

        // given
        val domainModel = WorkoutDomainModel(1, "workout", false)
        val guiModel = GuiModelMapper().mapDomainToGui(domainModel)

        every { factory.createToggleFavoriteWorkoutUseCase() } returns toggleFavoriteWorkoutUseCase
        every { toggleFavoriteWorkoutUseCase.execute(1) } returns Single.just(domainModel)

        uut = OverviewWorkoutViewModel(factory, scheduler, scheduler, GuiModelMapper())
        uut.state.observeForever(stateObserver)

        every { stateObserver.onChanged(any())} just Runs

        // when
        uut.onFavoriteClicked(1)

        // then
        verifySequence {
            stateObserver.onChanged(OverviewWorkoutState.Loading)
            toggleFavoriteWorkoutUseCase.execute(1)
            stateObserver.onChanged(OverviewWorkoutState.Loading)
//            stateObserver.onChanged(OverviewWorkoutState.WorkoutUpdate(guiModel))
        }
    }

    @Test
    fun `test searchquery expecting only one result`() {

        val domainModelList = arrayListOf(
            WorkoutDomainModel(1, "workout", false),
            WorkoutDomainModel(2, "", false),
            WorkoutDomainModel(3, "workout", false),
            WorkoutDomainModel(4, "workout", false),
            WorkoutDomainModel(5, "workout", false),
            WorkoutDomainModel(6, "wewasdfvvd", false))

        val guiModelList = ArrayList<WorkoutGuiModel>()

        val mapper = GuiModelMapper()

        for (domainModel in domainModelList) {
            guiModelList.add(mapper.mapDomainToGui(domainModel))
        }

        val matchingGuiModel = WorkoutGuiModel(6, "wewasdfvvd", false)

        every { factory.createGetWorkoutsUseCase() } returns getWorkoutsUseCaseMock
        every { getWorkoutsUseCaseMock.execute() } returns Single.just(domainModelList)

        uut = OverviewWorkoutViewModel(factory, scheduler, scheduler, GuiModelMapper())
        uut.state.observeForever(stateObserver)

        every { stateObserver.onChanged(any())} just Runs

        uut.onSearchWorkoutQueryChanged("wew")

        verifySequence {
            stateObserver.onChanged(OverviewWorkoutState.WorkoutList(guiModelList))
            stateObserver.onChanged(OverviewWorkoutState.WorkoutList(listOf(matchingGuiModel)))
        }
    }
}

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
import org.mockito.Mockito.mock
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class OverviewWorkoutViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    // using trampoline for FIFO of thread scheduling
    private val scheduler = Schedulers.trampoline()
    private val factory = mockk<UseCasesFactory>()
    private val getWorkoutsUseCaseMock = mockk<GetWorkoutsUseCase>()
    private val toggleFavoriteWorkoutUseCase = mockk<ToggleFavoriteWorkoutUseCase>()

    private lateinit var uut : OverviewWorkoutViewModel
    private val stateObserver = mockk<Observer<OverviewWorkoutState>>()

    @Before
    fun setup() {
        uut = OverviewWorkoutViewModel(factory, scheduler, scheduler, GuiModelMapper())
        uut.state.observeForever(stateObserver)

        every { stateObserver.onChanged(any())} just Runs
    }

    @Test
    fun `test loadworkouts expecting one workout`() {

        // given
        val domainModel = WorkoutDomainModel(1, "workout", true)
        val domainModelList = arrayListOf(domainModel)
        val guiModelList = arrayListOf(GuiModelMapper().mapDomainToGui(domainModel))

        every { factory.createGetWorkoutsUseCase() } returns getWorkoutsUseCaseMock
        every { getWorkoutsUseCaseMock.execute() } returns Single.just(domainModelList)

        // when
        uut.loadWorkouts()

        //then
        verifySequence {
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

        // when
        uut.favoriteClicked(1)

        // then
        verifySequence {
            toggleFavoriteWorkoutUseCase.execute(1)
            stateObserver.onChanged(OverviewWorkoutState.WorkoutUpdate(guiModel))
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

        uut.loadWorkouts()
        uut.searchWorkoutQueryChanged("wew")

        verifySequence {
            stateObserver.onChanged(OverviewWorkoutState.Loading)
            stateObserver.onChanged(OverviewWorkoutState.WorkoutList(guiModelList))
            stateObserver.onChanged(OverviewWorkoutState.WorkoutList(listOf(matchingGuiModel)))
        }
    }
}

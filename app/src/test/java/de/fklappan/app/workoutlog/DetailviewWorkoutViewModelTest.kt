package de.fklappan.app.workoutlog

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import de.fklappan.app.workoutlog.common.GuiModelMapper
import de.fklappan.app.workoutlog.common.UseCasesFactory
import de.fklappan.app.workoutlog.domain.WorkoutDetailsDomainModel
import de.fklappan.app.workoutlog.domain.WorkoutDomainModel
import de.fklappan.app.workoutlog.domain.WorkoutResultDomainModel
import de.fklappan.app.workoutlog.domain.usecases.GetWorkoutDetailsUseCase
import de.fklappan.app.workoutlog.domain.usecases.ToggleFavoriteWorkoutUseCase
import de.fklappan.app.workoutlog.ui.detailviewworkout.DetailviewWorkoutState
import de.fklappan.app.workoutlog.ui.detailviewworkout.DetailviewWorkoutViewModel
import io.mockk.*
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

class DetailviewWorkoutViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    // using trampoline for FIFO of thread scheduling
    private val scheduler = Schedulers.trampoline()
    private val factory = mockk<UseCasesFactory>()
    private val getWorkoutDetailsUseCaseMock = mockk<GetWorkoutDetailsUseCase>()
    private val toggleFavoriteWorkoutUseCase = mockk<ToggleFavoriteWorkoutUseCase>()

    private lateinit var uut: DetailviewWorkoutViewModel
    private val stateObserver = mockk<Observer<DetailviewWorkoutState>>()

    @Before
    fun setup() {
        uut = DetailviewWorkoutViewModel(factory, scheduler, scheduler, GuiModelMapper())
        uut.state.observeForever(stateObserver)

        every { stateObserver.onChanged(any()) } just Runs
    }

    @Test
    fun `test loadworkout expecting workout details then toggle favorite`() {

        // given
        val domainModel = WorkoutDomainModel(1, "workout", false)
        val domainModelResult1 = WorkoutResultDomainModel(1, "score1", Date(), 1, true, "note", "feeling")
        val domainModelResult2 = WorkoutResultDomainModel(2, "score2", Date(), 1, false, "note 2", "feeling 2")
        val domainModelDetail = WorkoutDetailsDomainModel(domainModel, arrayListOf(domainModelResult1, domainModelResult2))

        val guiModelDetail = GuiModelMapper().mapDomainToGui(domainModelDetail)
        val domainModelFavorite = domainModel.copy()
        val guiModelFavorite = GuiModelMapper().mapDomainToGui(domainModelFavorite)

        every { factory.createGetWorkoutDetailsUseCase() } returns getWorkoutDetailsUseCaseMock
        every { getWorkoutDetailsUseCaseMock.execute(1) } returns Single.just(domainModelDetail)
        every { factory.createToggleFavoriteWorkoutUseCase() } returns toggleFavoriteWorkoutUseCase
        every { toggleFavoriteWorkoutUseCase.execute(1) } returns Single.just(domainModelFavorite)

        // when
        uut.loadWorkout(1)
        uut.favoriteClicked()

        //then
        verifySequence {
            stateObserver.onChanged(DetailviewWorkoutState.Loading)
            stateObserver.onChanged(DetailviewWorkoutState.WorkoutDetails(guiModelDetail))
            stateObserver.onChanged(DetailviewWorkoutState.WorkoutUpdate(guiModelFavorite))
        }
    }
}

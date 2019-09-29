package de.fklappan.app.workoutlog

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import de.fklappan.app.workoutlog.common.GuiModelMapper
import de.fklappan.app.workoutlog.common.UseCasesFactory
import de.fklappan.app.workoutlog.domain.WorkoutDomainModel
import de.fklappan.app.workoutlog.domain.usecases.GetWorkoutsUseCase
import de.fklappan.app.workoutlog.domain.usecases.ToggleFavoriteWorkoutUseCase
import de.fklappan.app.workoutlog.ui.overviewworkout.OverviewWorkoutViewModel
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertFalse

class OverviewWorkoutViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    // using trampoline for FIFO of thread scheduling
    private val scheduler = Schedulers.trampoline()
    private val factory = mockk<UseCasesFactory>()
    private val getWorkoutsUseCaseMock = mockk<GetWorkoutsUseCase>()
    private val toggleFavoriteWorkoutUseCase = mockk<ToggleFavoriteWorkoutUseCase>()

    @Test
    fun `test loadworkouts expecting one workout`() {

        val domainModelList = arrayListOf(WorkoutDomainModel(1, "workout", true))

        every { factory.createGetWorkoutsUseCase() } returns getWorkoutsUseCaseMock
        every { getWorkoutsUseCaseMock.execute() } returns Single.just(domainModelList)

        val uut = OverviewWorkoutViewModel(factory, scheduler, scheduler, GuiModelMapper())

        uut.loadWorkouts()

        assert(uut.workoutList.value!!.size == 1)
        assert(uut.workoutList.value!![0].workoutId == 1)
    }

    @Test
    fun `test favoriteclicked expecting updatestream has value`() {

        val domainModel = WorkoutDomainModel(1, "workout", false)

        every { factory.createToggleFavoriteWorkoutUseCase() } returns toggleFavoriteWorkoutUseCase
        every { toggleFavoriteWorkoutUseCase.execute(1) } returns Single.just(domainModel)

        val uut = OverviewWorkoutViewModel(factory, scheduler, scheduler, GuiModelMapper())

        uut.favoriteClicked(1)

        assert(uut.updateStream.value!!.workoutId == 1)
        assertFalse(uut.updateStream.value!!.favorite )
    }

    @Test
    fun `test searchquery expecting only one result remains out of two`() {

        val domainModelList = arrayListOf(
            WorkoutDomainModel(1, "workout", false),
            WorkoutDomainModel(2, "", false),
            WorkoutDomainModel(3, "workout", false),
            WorkoutDomainModel(4, "workout", false),
            WorkoutDomainModel(5, "workout", false),
            WorkoutDomainModel(6, "wewasdfvvd", false))

        every { factory.createGetWorkoutsUseCase() } returns getWorkoutsUseCaseMock
        every { getWorkoutsUseCaseMock.execute() } returns Single.just(domainModelList)

        val uut = OverviewWorkoutViewModel(factory, scheduler, scheduler, GuiModelMapper())
        uut.loadWorkouts()
        assert(uut.workoutList.value!!.size == 6)
        uut.searchWorkoutQueryChanged("as")
        assert(uut.workoutList.value!!.size == 1)
        assert(uut.workoutList.value!![0].workoutId == 6) {"Workout ID 2 expected but was " + uut.workoutList.value!![0].workoutId}
    }
}

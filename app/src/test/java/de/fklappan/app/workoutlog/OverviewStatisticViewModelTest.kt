package de.fklappan.app.workoutlog

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import de.fklappan.app.workoutlog.common.GuiModelMapper
import de.fklappan.app.workoutlog.common.UseCasesFactory
import de.fklappan.app.workoutlog.domain.StatisticCurrentDomainModel
import de.fklappan.app.workoutlog.domain.usecases.GetStatisticUseCase
import de.fklappan.app.workoutlog.ui.overviewstatistic.OverviewStatisticState
import de.fklappan.app.workoutlog.ui.overviewstatistic.OverviewStatisticViewModel
import io.mockk.*
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.PrintWriter

class OverviewStatisticViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    // using trampoline for FIFO of thread scheduling
    private val scheduler = Schedulers.trampoline()
    private val factory = mockk<UseCasesFactory>()
    private val getStatisticUseCaseMock = mockk<GetStatisticUseCase>()
    private val expectedException = mockk<Exception>()

    private lateinit var uut: OverviewStatisticViewModel
    private val stateObserver = mockk<Observer<OverviewStatisticState>>()

    @Before
    fun setup() {
        uut = OverviewStatisticViewModel(factory, scheduler, scheduler, GuiModelMapper())
        uut.state.observeForever(stateObserver)

        every { stateObserver.onChanged(any()) } just Runs
    }

    @Test
    fun `test loadData expecting correct state sequence`() {

        // given
        val statisticCurrentDomainModel = StatisticCurrentDomainModel(4, true, 3, 12)
        val statisticCurrentGuiModel = GuiModelMapper().mapDomainToGui(statisticCurrentDomainModel)


        every { factory.createGetStatisticUseCase() } returns getStatisticUseCaseMock
        every { getStatisticUseCaseMock.execute(any()) } returns Single.just(statisticCurrentDomainModel
        )

        // when
        uut.loadData()

        //then
        verifySequence {
            stateObserver.onChanged(OverviewStatisticState.Loading)
            stateObserver.onChanged(OverviewStatisticState.Statistic(statisticCurrentGuiModel))
        }
    }
//
//    @Test
//    fun `test loadData expecting error`() {
//
//        // given
//        every { factory.createGetStatisticUseCase() } returns getStatisticUseCaseMock
//        every { getStatisticUseCaseMock.execute(any()) } throws expectedException
//        every { expectedException.localizedMessage } returns "error"
//        every { expectedException.message } returns "error"
//        every { expectedException.printStackTrace() } just Runs
//        every { expectedException.printStackTrace(any<PrintWriter>()) } just Runs
//
//        // when
//        uut.loadData()
//
//        //then
//        verifySequence {
//            stateObserver.onChanged(OverviewStatisticState.Loading)
//            stateObserver.onChanged(OverviewStatisticState.Error("error"))
//        }
//    }
}

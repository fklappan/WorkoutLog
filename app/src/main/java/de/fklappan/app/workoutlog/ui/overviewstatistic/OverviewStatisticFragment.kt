package de.fklappan.app.workoutlog.ui.overviewstatistic

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import de.fklappan.app.workoutlog.R
import de.fklappan.app.workoutlog.common.BaseFragment
import de.fklappan.app.workoutlog.common.LOG_TAG
import de.fklappan.app.workoutlog.common.ViewModelFactory
import kotlinx.android.synthetic.main.overviewresult.*
import javax.inject.Inject

class OverviewStatisticFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModelOverviewStatistic: OverviewStatisticViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.overviewresult, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getInjector().inject(this)
        super.onViewCreated(view, savedInstanceState)

        initFragment()
        initViewModels()
        observeViewModels()
        fetchData()
    }

    private fun initFragment() {
        getAppBarHeader().setHeaderText(R.string.caption_statistic_overview)
    }

    private fun initViewModels() {
        viewModelOverviewStatistic =
            ViewModelProviders.of(this, viewModelFactory).get(OverviewStatisticViewModel::class.java)
    }

    private fun observeViewModels() {
        viewModelOverviewStatistic.statisticCurrent.observe(this, Observer { showCurrentStatistic(it) })
    }

    private fun fetchData() {
        viewModelOverviewStatistic.loadData()
    }

    private fun showCurrentStatistic(statisticCurrentGuiModel: StatisticCurrentGuiModel) {
        Log.d(LOG_TAG, "Statistics loaded")
        textViewStreakCount.text = statisticCurrentGuiModel.streak.toString()
        if (statisticCurrentGuiModel.isWorkoutStreak) {
            textViewStreakText.text = resources.getString(R.string.day_streak)
        } else {
            textViewStreakText.text = resources.getQuantityString(R.plurals.quantity_rest_days, statisticCurrentGuiModel.streak)
        }

        textViewStatisticMonthCount.text = statisticCurrentGuiModel.workoutCountMonth.toString()
        textViewStatisticMonthText.text = resources.getQuantityString(R.plurals.quantity_monthly_workouts, statisticCurrentGuiModel.workoutCountMonth)
        textViewStatisticYearCount.text = statisticCurrentGuiModel.workoutCountYear.toString()
        textViewStatisticYearText.text = resources.getQuantityString(R.plurals.quantity_yearly_workouts, statisticCurrentGuiModel.workoutCountYear)
    }
}
package de.fklappan.app.workoutlog.ui.overviewstatistic

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import de.fklappan.app.workoutlog.R
import de.fklappan.app.workoutlog.common.BaseFragment
import de.fklappan.app.workoutlog.common.LOG_TAG
import de.fklappan.app.workoutlog.common.ViewModelFactory
import de.fklappan.app.workoutlog.databinding.StatisticBinding
import javax.inject.Inject

class OverviewStatisticFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var binding : StatisticBinding
    private lateinit var viewModelOverviewStatistic: OverviewStatisticViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = StatisticBinding.inflate(inflater, container, false)
        return binding.root
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
            ViewModelProvider(this, viewModelFactory).get(OverviewStatisticViewModel::class.java)
    }

    private fun observeViewModels() {
        viewModelOverviewStatistic.state.observe(viewLifecycleOwner, Observer { state -> updateState(state) })
    }

    private fun fetchData() {
        viewModelOverviewStatistic.loadData()
    }

    private fun updateState(state: OverviewStatisticState) {
        when(state) {
            is OverviewStatisticState.Loading -> showLoading()
            is OverviewStatisticState.Error -> showError(state.message)
            is OverviewStatisticState.Statistic -> showStatistic(state.statistic)
        }
    }

    private fun showLoading() {
        Log.d(LOG_TAG, "Loading statistics")
    }

    private fun showError(message: String) {
        Log.e(LOG_TAG, "Error fetching statistics: $message")
    }

    private fun showStatistic(statisticCurrentGuiModel: StatisticCurrentGuiModel) {
        Log.d(LOG_TAG, "Statistics loaded")
        binding.textViewStreakCount.text = statisticCurrentGuiModel.streak.toString()
        if (statisticCurrentGuiModel.isWorkoutStreak) {
            binding.textViewStreakText.text = resources.getString(R.string.day_streak)
        } else {
            binding.textViewStreakText.text = resources.getQuantityString(R.plurals.quantity_rest_days, statisticCurrentGuiModel.streak)
        }

        binding.textViewStatisticMonthCount.text = statisticCurrentGuiModel.workoutCountMonth.toString()
        binding.textViewStatisticMonthText.text = resources.getQuantityString(R.plurals.quantity_monthly_workouts, statisticCurrentGuiModel.workoutCountMonth)
        binding.textViewStatisticYearCount.text = statisticCurrentGuiModel.workoutCountYear.toString()
        binding.textViewStatisticYearText.text = resources.getQuantityString(R.plurals.quantity_yearly_workouts, statisticCurrentGuiModel.workoutCountYear)
    }
}
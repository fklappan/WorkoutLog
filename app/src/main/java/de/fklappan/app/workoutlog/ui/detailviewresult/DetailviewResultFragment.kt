package de.fklappan.app.workoutlog.ui.detailviewresult

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import de.fklappan.app.workoutlog.R
import de.fklappan.app.workoutlog.common.BaseFragment
import de.fklappan.app.workoutlog.common.LOG_TAG
import de.fklappan.app.workoutlog.common.ViewModelFactory
import de.fklappan.app.workoutlog.ui.detailviewworkout.WorkoutResultGuiModel
import kotlinx.android.synthetic.main.detailview_result.*
import javax.inject.Inject

class DetailviewResultFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModelDetail: DetailviewResultViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.detailview_result, container, false)
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
        getAppBarHeader().setHeaderText(R.string.result_details)
    }

    private fun initViewModels() {
        viewModelDetail = ViewModelProvider(this, viewModelFactory).get(DetailviewResultViewModel::class.java)
    }

    private fun observeViewModels() {
        viewModelDetail.state.observe(viewLifecycleOwner, Observer { state -> updateState(state)})
    }

    private fun fetchData() {
        viewModelDetail.loadResult(requireArguments().getInt("resultId"))
    }

    private fun updateState(state: DetailviewResultState) {
        when(state) {
            is DetailviewResultState.Loading -> showLoading()
            is DetailviewResultState.Error -> showError(state.message)
            is DetailviewResultState.ResultDetails -> showResultDetails(state.resultDetails)
            is DetailviewResultState.ResultUpdate -> showResultUpdate(state.result)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showResultUpdate(resultGuiModel: WorkoutResultGuiModel) {
        Log.d(LOG_TAG, "result updated")
        textViewResultDetails.text = resultGuiModel.score
        textViewResultNote.text = resultGuiModel.note
        textViewResultFeeling.text = resultGuiModel.feeling
    }
//
//    private fun editWorkout() {
//        Navigation.findNavController(requireView())
//            .navigate(R.id.action_detailviewWorkoutFragment_to_editWorkoutFragment, arguments)
//    }

    private fun showResultDetails(result: WorkoutResultGuiModel) {
        Log.d(LOG_TAG, "Result details loaded: " + result.score)
        showResultUpdate(result)
    }

    private fun showLoading() {
        Log.d(LOG_TAG, "Loading result details")
    }

    private fun showError(message: String) {
        Log.e(LOG_TAG, "Error fetching result details: $message")
    }
}
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
import de.fklappan.app.workoutlog.common.color
import de.fklappan.app.workoutlog.databinding.DetailviewResultBinding
import de.fklappan.app.workoutlog.domain.toPrettyString
import de.fklappan.app.workoutlog.ui.detailviewworkout.WorkoutResultGuiModel
import javax.inject.Inject

class DetailviewResultFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var binding : DetailviewResultBinding
    private lateinit var viewModelDetail: DetailviewResultViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DetailviewResultBinding.inflate(inflater, container, false)
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

    @Override
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.edit, menu)
    }

    @Override
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_edit) {
            editResult()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initFragment() {
        getAppBarHeader().setHeaderText(R.string.result_details)
        setHasOptionsMenu(true)
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
        binding.textViewResultDetails.text = resultGuiModel.score
        binding.textViewResultNote.text = resultGuiModel.note
        binding.textViewResultFeeling.text = resultGuiModel.feeling
        binding.textViewDate.text = resultGuiModel.date.toPrettyString()
        if (resultGuiModel.pr) {
            binding.imagebuttonPr.color = R.color.colorAccent
        } else {
            binding.imagebuttonPr.color = R.color.gray
        }
    }

    private fun editResult() {
        Navigation.findNavController(requireView())
            .navigate(R.id.editResultFragment, arguments)
    }

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
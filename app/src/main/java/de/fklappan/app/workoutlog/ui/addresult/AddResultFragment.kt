package de.fklappan.app.workoutlog.ui.addresult

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import de.fklappan.app.workoutlog.R
import de.fklappan.app.workoutlog.common.*
import de.fklappan.app.workoutlog.databinding.AddresultBinding
import de.fklappan.app.workoutlog.domain.toPrettyString
import de.fklappan.app.workoutlog.ui.detailviewworkout.WorkoutDetailsGuiModel
import java.util.*
import javax.inject.Inject

class AddResultFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var binding : AddresultBinding
    private lateinit var viewModelAddResult: AddResultViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = AddresultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getInjector().inject(this)
        super.onViewCreated(view, savedInstanceState)

        initFragment()
        initFab()
        initViewModels()
        observeViewModels()
        fetchData()
    }

    private fun initFab() {
        binding.floatingActionButton.setOnClickListener(this::onClickFab)
    }

    private fun initFragment() {
        getAppBarHeader().setHeaderText(R.string.caption_add_result)

        // set listeners
        binding.linearLayoutDate.setOnClickListener(this::onClickDate)
        binding.imagebuttonPr.setOnClickListener(this::onClickPr)
    }

    private fun initViewModels() {
        viewModelAddResult = ViewModelProvider(this, viewModelFactory).get(AddResultViewModel::class.java)
    }

    private fun observeViewModels() {
        viewModelAddResult.state.observe(viewLifecycleOwner, Observer { state -> updateState(state) })
    }

    private fun fetchData() {
        viewModelAddResult.initialize(requireArguments().getInt("workoutId"))
    }

    private fun updateState(state: AddResultState) {
        when(state) {
            is AddResultState.WorkoutSaved -> showResult()
            is AddResultState.Error -> showError(state.message)
            is AddResultState.Data -> showWorkoutDetails(state.workoutDetails, state.date, state.isPr)
            is AddResultState.DateSelected -> showDate(state.date)
            is AddResultState.PrChanged -> showPr(state.isPr)
            is AddResultState.Loading -> Unit
        }
    }

    private fun showPr(isPr: Boolean) {
        Log.d(LOG_TAG, "updating PR: $isPr")
        if (isPr) {
            binding.imagebuttonPr.playGrowAnimation()
            binding.imagebuttonPr.color = R.color.colorAccent
        } else {
            binding.imagebuttonPr.color = R.color.gray
        }
    }

    private fun showDate(date: Date) {
        Log.d(LOG_TAG, "updating date: " + date.toPrettyString())
        binding.textViewDate.text = date.toPrettyString()
    }

    private fun showResult() {
        Log.d(LOG_TAG, "saved workout")
        binding.textViewError.visibility = View.GONE
        Snackbar.make(requireView(), getString(R.string.message_added_workout_result), Snackbar.LENGTH_LONG).show()
        Navigation.findNavController(requireView()).navigateUp()
    }

    private fun showWorkoutDetails(guiModel: WorkoutDetailsGuiModel, date: Date, isPr: Boolean) {
        if (!guiModel.resultList.isEmpty()) {
            binding.editTextContent.setText(guiModel.resultList.last().score)
        } else {
            binding.editTextContent.setText(guiModel.workout.text)
        }

        binding.bottomSheet.textViewWorkoutDetails.text = guiModel.workout.text

        showDate(date)
        showPr(isPr)
    }

    private fun showError(message: String) {
        Log.d(LOG_TAG, "Error occured: $message")
        binding.textViewError.visibility = View.VISIBLE
        binding.textViewError.text = message
    }

    private fun onClickDate(view: View) {
        val bottomSheetFragment = BottomSheetFragment(this::chooseDate)
        bottomSheetFragment.show(parentFragmentManager, "bottomSheet")
    }

    private fun chooseDate(view: DatePicker, year: Int, month: Int, day: Int) {
        Log.d(LOG_TAG, "Date chosen: $year/$month/$day")
        viewModelAddResult.onDateSelected(year, month, day)
    }

    private fun onClickPr(view: View) {
        viewModelAddResult.onPrClicked()
    }

    private fun onClickFab(view: View) {
        viewModelAddResult.onSaveClicked(
            binding.editTextContent.text.toString(),
            binding.editTextNote.text.toString(),
            binding.editTextFeeling.text.toString())
    }
}
package de.fklappan.app.workoutlog.ui.editresult

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator.REVERSE
import android.content.res.ColorStateList
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
import de.fklappan.app.workoutlog.domain.toPrettyString
import de.fklappan.app.workoutlog.ui.detailviewworkout.WorkoutResultGuiModel
import kotlinx.android.synthetic.main.card_workout_detail.*
import kotlinx.android.synthetic.main.editresult.*
import kotlinx.android.synthetic.main.overview.floatingActionButton
import java.util.*
import javax.inject.Inject

class EditResultFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: EditResultViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.editresult, container, false)
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
        floatingActionButton.setOnClickListener(this::onClickFab)
    }

    private fun initFragment() {
        getAppBarHeader().setHeaderText(R.string.caption_edit_result)

        // set listeners
        linearLayoutDate.setOnClickListener(this::onClickDate)
        imagebuttonPr.setOnClickListener(this::onClickPr)
    }

    private fun initViewModels() {
        viewModel = ViewModelProvider(this, viewModelFactory).get(EditResultViewModel::class.java)
    }

    private fun observeViewModels() {
        viewModel.state.observe(viewLifecycleOwner, Observer { state -> updateState(state) })
    }

    private fun fetchData() {
        viewModel.loadResult(requireArguments().getInt("resultId"))
    }

    private fun updateState(state: EditResultState) {
        when(state) {
            is EditResultState.ResultSaved -> showSavedMessage()
            is EditResultState.Error -> showError(state.message)
            is EditResultState.Data -> showResult(state.result)
            is EditResultState.DateSelected -> showDate(state.date)
            is EditResultState.PrChanged -> showPr(state.isPr)
        }
    }

    private fun showPr(isPr: Boolean) {
        Log.d(LOG_TAG, "updating PR: $isPr")
        imagebuttonPr.isSelected = isPr
        if (isPr) {
            imagebuttonPr.color = R.color.colorAccent
        } else {
            imagebuttonPr.color = R.color.gray
        }
    }

    private fun showDate(date: Date) {
        Log.d(LOG_TAG, "updating date: " + date.toPrettyString())
        textViewDate.text = date.toPrettyString()
    }

    private fun showSavedMessage() {
        Log.d(LOG_TAG, "saved result")
        textViewError.visibility = View.GONE
        Snackbar.make(requireView(), getString(R.string.message_saved_changes_success), Snackbar.LENGTH_LONG).show()
        Navigation.findNavController(requireView()).navigateUp()
    }

    private fun showResult(result: WorkoutResultGuiModel) {
        editTextContent.setText(result.score)
        editTextNote.setText(result.note)
        editTextFeeling.setText(result.feeling)
        showDate(result.date)
        showPr(result.pr)
    }

    private fun showError(message: String) {
        Log.d(LOG_TAG, "Error occured: $message")
        textViewError.visibility = View.VISIBLE
        textViewError.text = message
    }

    private fun onClickDate(view: View) {
        val bottomSheetFragment = BottomSheetFragment(this::chooseDate)
        bottomSheetFragment.show(parentFragmentManager, "bottomSheet")
    }

    private fun chooseDate(view: DatePicker, year: Int, month: Int, day: Int) {
        Log.d(LOG_TAG, "Date chosen: $year/$month/$day")
        viewModel.onDateSelected(year, month, day)
    }

    private fun onClickPr(view: View) {
        if (!view.isSelected) {
            imagebuttonPr.playGrowAnimation()
        }
        viewModel.onPrClicked()
    }

    private fun onClickFab(view: View) {
        viewModel.onSaveClicked(editTextContent.text.toString(), editTextNote.text.toString(), editTextFeeling.text.toString())
    }
}
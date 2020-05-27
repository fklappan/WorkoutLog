package de.fklappan.app.workoutlog.ui.addresult

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
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import de.fklappan.app.workoutlog.R
import de.fklappan.app.workoutlog.common.*
import de.fklappan.app.workoutlog.domain.toPrettyString
import de.fklappan.app.workoutlog.ui.detailviewworkout.WorkoutDetailsGuiModel
import kotlinx.android.synthetic.main.addresult.*
import kotlinx.android.synthetic.main.addworkout.editTextContent
import kotlinx.android.synthetic.main.addworkout.textViewError
import kotlinx.android.synthetic.main.bottom_sheet_addresult.*
import kotlinx.android.synthetic.main.overview.floatingActionButton
import java.util.*
import javax.inject.Inject

class AddResultFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModelAddResult: AddResultViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.addresult, container, false)
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
        getAppBarHeader().setHeaderText(R.string.caption_add_result)

        // set listeners
        linearLayoutDate.setOnClickListener(this::onClickDate)
        imagebuttonPr.setOnClickListener(this::onClickPr)
    }

    private fun initViewModels() {
        viewModelAddResult = ViewModelProviders.of(this, viewModelFactory).get(AddResultViewModel::class.java)
    }

    private fun observeViewModels() {
        viewModelAddResult.state.observe(this, Observer { state -> updateState(state) })
    }

    private fun fetchData() {
        viewModelAddResult.initialize(arguments!!.getInt("workoutId"))
    }

    private fun updateState(state: AddResultState) {
        when(state) {
            is AddResultState.WorkoutSaved -> showResult()
            is AddResultState.Error -> showError(state.message)
            is AddResultState.Data -> showWorkoutDetails(state.workoutDetails, state.date, state.isPr)
            is AddResultState.DateSelected -> showDate(state.date)
            is AddResultState.PrChanged -> showPr(state.isPr)
        }
    }

    private fun showPr(isPr: Boolean) {
        Log.d(LOG_TAG, "updating PR: $isPr")
        if (isPr) {
            playPrButtonAnimation()
            imagebuttonPr.imageTintList = ColorStateList.valueOf(context!!.getColor(R.color.colorAccent))
        } else {
            imagebuttonPr.imageTintList = ColorStateList.valueOf(context!!.getColor(R.color.gray))
        }
    }

    private fun showDate(date: Date) {
        Log.d(LOG_TAG, "updating date: " + date.toPrettyString())
        textViewDate.text = date.toPrettyString()
    }

    private fun showResult() {
        Log.d(LOG_TAG, "saved workout")
        textViewError.visibility = View.GONE
        Snackbar.make(view!!, getString(R.string.message_added_workout_result), Snackbar.LENGTH_LONG).show()
        Navigation.findNavController(view!!).navigateUp()
    }

    private fun showWorkoutDetails(guiModel: WorkoutDetailsGuiModel, date: Date, isPr: Boolean) {
        if (!guiModel.resultList.isEmpty()) {
            editTextContent.setText(guiModel.resultList.last().score)
        } else {
            editTextContent.setText(guiModel.workout.text)
        }
        textViewWorkoutDetails.text = guiModel.workout.text

        showDate(date)
        showPr(isPr)
    }

    private fun showError(message: String) {
        Log.d(LOG_TAG, "Error occured: $message")
        textViewError.visibility = View.VISIBLE
        textViewError.text = message
    }

    private fun onClickDate(view: View) {
        val bottomSheetFragment = BottomSheetFragment(this::chooseDate)
        bottomSheetFragment.show(fragmentManager!!, "bottomSheet")
    }

    private fun chooseDate(view: DatePicker, year: Int, month: Int, day: Int) {
        Log.d(LOG_TAG, "Date chosen: $year/$month/$day")
        viewModelAddResult.onDateSelected(year, month, day)
    }

    private fun onClickPr(view: View) {
        viewModelAddResult.onPrClicked()
    }

    private fun playPrButtonAnimation() {
        // double scale the button for 200 ms and return back to normal size
        val animScaleX = ObjectAnimator.ofFloat(imagebuttonPr, "scaleX", 1.0f, 2.3f)
        with(animScaleX) {
            repeatCount = 1
            repeatMode = REVERSE
            duration = 200
        }

        val animScaleY = ObjectAnimator.ofFloat(imagebuttonPr, "scaleY", 1.0f, 2.3f)
        with(animScaleY) {
            repeatCount = 1
            repeatMode = REVERSE
            duration = 200
        }

        val growAnimator = AnimatorSet()
        growAnimator.play(animScaleX).with(animScaleY)
        growAnimator.start()
    }

    private fun onClickFab(view: View) {
        viewModelAddResult.onSaveClicked(editTextContent.text.toString(), editTextNote.text.toString(), editTextFeeling.text.toString())
    }
}
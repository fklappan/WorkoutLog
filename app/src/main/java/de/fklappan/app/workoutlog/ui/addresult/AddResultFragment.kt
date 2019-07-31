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
import android.view.animation.AccelerateInterpolator
import android.view.animation.AnticipateInterpolator
import android.view.animation.AnticipateOvershootInterpolator
import android.view.animation.BounceInterpolator
import android.widget.DatePicker
import androidx.core.animation.addListener
import androidx.core.animation.doOnEnd
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import de.fklappan.app.workoutlog.R
import de.fklappan.app.workoutlog.common.*
import de.fklappan.app.workoutlog.ui.detailview.WorkoutResultGuiModel
import kotlinx.android.synthetic.main.addresult.*
import kotlinx.android.synthetic.main.addworkout.editTextContent
import kotlinx.android.synthetic.main.addworkout.textViewError
import kotlinx.android.synthetic.main.overview.floatingActionButton
import java.util.*
import javax.inject.Inject

const val EXTRA_DATE = "EXTRA_DATE"

class AddResultFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModelAddResult: AddResultViewModel
    private var currentdate = Date()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.addresult, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getInjector().inject(this)
        super.onViewCreated(view, savedInstanceState)

        restoreState(savedInstanceState)

        initFragment()
        initFab()
        initViewModels()
        observeViewModels()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong(EXTRA_DATE, currentdate.time)
    }

    override fun restoreState(savedInstanceState: Bundle?) {
        // note, dont access gui elements because they are not initialised yet
        if (savedInstanceState != null) {
            currentdate.time = savedInstanceState.getLong(EXTRA_DATE, Date().time)
        }
    }

    private fun initFab() {
        floatingActionButton.setOnClickListener(this::onClickFab)
    }

    private fun initFragment() {
        getAppBarHeader().setHeaderText(R.string.caption_add_result)

        // set listeners
        linearLayoutDate.setOnClickListener(this::onClickDate)
        imagebuttonPr.setOnClickListener(this::onClickPr)

        // load initial data
        textViewDate.text = currentdate.toPrettyString()
    }

    private fun initViewModels() {
        viewModelAddResult = ViewModelProviders.of(this, viewModelFactory).get(AddResultViewModel::class.java)
    }

    private fun observeViewModels() {
        viewModelAddResult.saveState.observe(this, Observer { saved -> showResult(saved) })
        viewModelAddResult.errorState.observe(this, Observer { error -> showError(error) })
    }

    private fun showResult(saved: Boolean) {
        Log.d(LOG_TAG, "saved workout: $saved")
        textViewError.visibility = View.GONE
        Snackbar.make(view!!, getString(R.string.message_added_workout_result), Snackbar.LENGTH_LONG).show()
        Navigation.findNavController(view!!).navigateUp()
    }

    private fun showError(error: Throwable) {
        Log.d(LOG_TAG, "Error occured", error)
        textViewError.visibility = View.VISIBLE
        textViewError.text = error.message
    }

    private fun onClickDate(view: View) {
        val bottomSheetFragment = BottomSheetFragment(this::chooseDate)
        bottomSheetFragment.show(fragmentManager, "bottomSheet")
    }

    private fun chooseDate(view: DatePicker, year: Int, month: Int, day: Int) {
        Log.d(LOG_TAG, "Date chosen: $year/$month/$day")
        currentdate = Date(year-1900, month, day)
        textViewDate.text = currentdate.toPrettyString()
    }

    private fun onClickPr(view: View) {
        if (imagebuttonPr.isSelected) {
            imagebuttonPr.imageTintList = ColorStateList.valueOf(context!!.getColor(R.color.gray))
        }
        else {
            playPrButtonAnimation()
            imagebuttonPr.imageTintList = ColorStateList.valueOf(context!!.getColor(R.color.colorAccent))
        }
        imagebuttonPr.isSelected = !imagebuttonPr.isSelected
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
        val guiModel = WorkoutResultGuiModel(
            0,
            editTextContent.text.toString(),
            currentdate,
            arguments!!.getInt("workoutId"),
            imagebuttonPr.isSelected,
            editTextNote.text.toString(),
            editTextFeeling.text.toString()
        )
        viewModelAddResult.saveResult(guiModel)
    }
}
package de.fklappan.app.workoutlog.ui.filter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.core.view.setMargins
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import de.fklappan.app.workoutlog.R
import de.fklappan.app.workoutlog.common.BaseFragment
import de.fklappan.app.workoutlog.common.LOG_TAG
import de.fklappan.app.workoutlog.common.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_filter.*
import javax.inject.Inject

class FilterFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: FilterViewModel

    private val colors = arrayListOf(R.color.colorAccent,R.color.colorAccentOld, android.R.color.holo_orange_dark, android.R.color.holo_blue_bright, android.R.color.holo_purple)


    companion object {
        private const val MY_BOOLEAN = "my_boolean"
        private const val MY_INT = "my_int"

        fun newInstance() = FilterFragment().apply {
//            arguments = bundleOf(
//                MY_BOOLEAN to aBoolean,
//                MY_INT to anInt)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getInjector().inject(this)
        super.onViewCreated(view, savedInstanceState)

        initFragment()
        initViewModels()
        observeViewModels()
    }

    private fun initFragment() {
        // set listeners

        editTextSearch.doOnTextChanged(this::onTextChanged)
//        editTextSearch.doOnTextChanged { text, start, count, after ->
//            Log.d(LOG_TAG, "Text changed: $text, start: $start, count: $count, after: $after")
//            if (after > 1) {
//
//            }
//            var searchTag = text.toString()
//            // text before first space will be used as search tag
//            if (searchTag.contains(" ")) {
//                searchTag = searchTag.split(" ").first()
//                addChip(searchTag, colors[(0 until colors.size).random()])
//                editTextSearch.setText("")
//            }
//        }

        showDummyFilterChips()
    }

    private fun onTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {
        Log.d(LOG_TAG, "Text changed: $text, start: $start, count: $count, after: $after")
        viewModel.onSearchTextChanged(text.toString())
    }

    private fun initViewModels() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(FilterViewModel::class.java)
    }

    private fun observeViewModels() {
        viewModel.tagChipLiveData.observe(requireActivity(), Observer(this::addChip))
    }

    private fun addChip(chipText: String) {
        val currentSearchText = editTextSearch.text.toString().apply {
            replace(chipText, "")
        }
        editTextSearch.setText(currentSearchText)

        createChip(chipText, colors[(0 until colors.size).random()])
        editTextSearch.setText("")
    }


    private fun showDummyFilterChips() {

//        addChip("MetCon", R.color.colorAccent)
//        addChip("Chest", R.color.colorAccentOld)
//        addChip("Triceps", android.R.color.holo_orange_dark)
//        addChip("Shoulder", android.R.color.holo_blue_light)
//        addChip("Triceps", android.R.color.holo_orange_dark)
//        addChip("MetCon", R.color.colorAccent)
//        addChip("Chest", R.color.colorAccentOld)
//        addChip("Triceps", android.R.color.holo_orange_dark)
//        addChip("Shoulder", android.R.color.holo_blue_light)
//        addChip("SuperLongTagBecauseItCanOnlyBeOneWord", android.R.color.holo_purple)
//        addChip("MetCon", R.color.colorAccent)
//        addChip("Chest", R.color.colorAccentOld)
//        addChip("SuperLongTagBecauseItCanOnlyBeOneWord", android.R.color.holo_purple)
//        addChip("Triceps", android.R.color.holo_orange_dark)
//        addChip("Shoulder", android.R.color.holo_blue_light)
//        addChip("Triceps", android.R.color.holo_orange_dark)
//        addChip("MetCon", R.color.colorAccent)
//        addChip("SuperLongTagBecauseItCanOnlyBeOneWord", android.R.color.holo_purple)
//        addChip("Chest", R.color.colorAccentOld)
//        addChip("Triceps", android.R.color.holo_orange_dark)
//        addChip("Shoulder", android.R.color.holo_blue_light)
    }

    private fun createChip(chipText: String, @ColorRes colorId: Int) {

        val lp = ChipGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT).apply {
            setMargins(resources.getDimensionPixelSize(R.dimen.default_margin_small))
        }

        val chip1 = Chip(requireContext()).apply {
            text = chipText
            setChipBackgroundColorResource(colorId)
            layoutParams = lp
            closeIcon = resources.getDrawable(R.drawable.close, null)
            isCloseIconVisible = true
            setOnCloseIconClickListener {
                chipGroupActiveFilters.removeView(this)
            }
        }

        chipGroupActiveFilters.addView(chip1)
    }

}
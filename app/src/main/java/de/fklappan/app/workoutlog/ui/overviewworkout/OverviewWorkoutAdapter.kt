package de.fklappan.app.workoutlog.ui.overviewworkout

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import de.fklappan.app.workoutlog.R
import de.fklappan.app.workoutlog.common.LOG_TAG
import de.fklappan.app.workoutlog.common.color
import de.fklappan.app.workoutlog.common.playGrowAnimation
import kotlinx.android.synthetic.main.list_item_workout.view.*

class OverviewWorkoutAdapter(
    private val clickListener: (WorkoutGuiModel) -> Unit,
    private val optionsListener: (View, WorkoutGuiModel) -> Unit,
    private val favoriteListener: (Int) -> Unit
) : RecyclerView.Adapter<OverviewWorkoutAdapter.ViewHolder>() {

    var items: MutableList<WorkoutGuiModel> = ArrayList()
        set(value) {
            items.clear()
            items.addAll(value)
            notifyDataSetChanged()
        }

    fun update(workoutGuiModel: WorkoutGuiModel) {
        for (i in 0 until items.size) {
            if (items[i].workoutId == workoutGuiModel.workoutId) {
                items[i] = workoutGuiModel
                notifyItemChanged(i)
                break
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_workout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(items.get(position), clickListener, optionsListener, favoriteListener)
    }

    override fun getItemCount() = items.size

    fun addItem(workoutGuiModel: WorkoutGuiModel, index: Int) {
        items.add(index, workoutGuiModel)
        notifyItemInserted(index)
    }

    fun deleteItem(workoutGuiModel: WorkoutGuiModel): Int {
        val removedIndex = items.indexOf(workoutGuiModel)
        if (items.remove(workoutGuiModel)) {
            notifyItemRemoved(removedIndex)
        }
        return removedIndex
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItem(
            workoutGuiModel: WorkoutGuiModel,
            clickListener: (WorkoutGuiModel) -> Unit,
            optionsListener: (View, WorkoutGuiModel) -> Unit,
            favoriteListener: (Int) -> Unit
        ) {
            itemView.textViewContent.text = workoutGuiModel.text
            itemView.setOnClickListener {
                clickListener(workoutGuiModel)
            }
            itemView.imageButtonOptions.setOnClickListener{optionsListener(it, workoutGuiModel)}
            itemView.imageButtonFavorite.setOnClickListener {
                if (!workoutGuiModel.favorite) {
                    itemView.imageButtonFavorite.playGrowAnimation()
                }
                clickedButton(
                    it as ImageButton,
                    workoutGuiModel,
                    favoriteListener
                )
            }
            setFavoriteButtonState(itemView.imageButtonFavorite, workoutGuiModel.favorite)
        }

        private fun clickedButton(
            view: ImageButton,
            workoutGuiModel: WorkoutGuiModel,
            favoriteListener: (Int) -> Unit
        ) {
            Log.d(LOG_TAG, "Clicked favorite button")
            setFavoriteButtonState(view, !view.isSelected)
            favoriteListener.invoke(workoutGuiModel.workoutId)
        }

        private fun setFavoriteButtonState(view: ImageButton, select: Boolean) {
            if (select) {
                view.color = R.color.colorAccent
            } else {
                view.color = R.color.gray
            }
            view.isSelected = select
        }
    }
}
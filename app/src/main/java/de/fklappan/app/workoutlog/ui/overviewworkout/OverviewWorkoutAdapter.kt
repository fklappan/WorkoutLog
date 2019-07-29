package de.fklappan.app.workoutlog.ui.overviewworkout

import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import de.fklappan.app.workoutlog.R
import de.fklappan.app.workoutlog.common.LOG_TAG
import kotlinx.android.synthetic.main.list_item_workout.view.*

class OverviewWorkoutAdapter(
    private val clickListener: (WorkoutGuiModel) -> Unit,
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
        holder.bindItem(items.get(position), clickListener, favoriteListener)
    }

    override fun getItemCount() = items.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItem(
            workoutGuiModel: WorkoutGuiModel,
            clickListener: (WorkoutGuiModel) -> Unit,
            favoriteListener: (Int) -> Unit
        ) {
            itemView.textViewContent.text = workoutGuiModel.text
            itemView.setOnClickListener { clickListener.invoke(workoutGuiModel) }
            itemView.imageButtonFavorite.setOnClickListener {
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
                view.imageTintList = ColorStateList.valueOf(view.context.getColor(R.color.colorAccent))
            } else {
                view.imageTintList = ColorStateList.valueOf(view.context.getColor(R.color.gray))
            }
            view.isSelected = select
        }

    }
}
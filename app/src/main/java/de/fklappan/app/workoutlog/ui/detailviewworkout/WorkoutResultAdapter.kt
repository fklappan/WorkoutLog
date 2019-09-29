package de.fklappan.app.workoutlog.ui.detailviewworkout

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.fklappan.app.workoutlog.R
import de.fklappan.app.workoutlog.common.toPrettyString
import kotlinx.android.synthetic.main.list_item_workout.view.textViewContent
import kotlinx.android.synthetic.main.list_item_workout_result.view.*
import kotlinx.android.synthetic.main.list_item_workout_result.view.imageButtonFavorite

class WorkoutResultAdapter(private val clickListener: (WorkoutResultGuiModel) -> Unit) :
    RecyclerView.Adapter<WorkoutResultAdapter.ViewHolder>() {

    var items: MutableList<WorkoutResultGuiModel> = ArrayList()
        set(value) {
            items.clear()
            items.addAll(value)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_workout_result,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(items.get(position), clickListener)
    }

    override fun getItemCount() = items.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItem(workoutGuiModel: WorkoutResultGuiModel, clickListener: (WorkoutResultGuiModel) -> Unit) {
            itemView.textViewDate.text = workoutGuiModel.date.toPrettyString()
            itemView.textViewContent.text = workoutGuiModel.score
            itemView.setOnClickListener { clickListener.invoke(workoutGuiModel) }
            if (workoutGuiModel.pr) {
                itemView.imageButtonFavorite.imageTintList = ColorStateList.valueOf(itemView.context.getColor(R.color.colorAccent))
            } else {
                itemView.imageButtonFavorite.imageTintList = ColorStateList.valueOf(itemView.context.getColor(R.color.gray))
            }
        }

    }
}
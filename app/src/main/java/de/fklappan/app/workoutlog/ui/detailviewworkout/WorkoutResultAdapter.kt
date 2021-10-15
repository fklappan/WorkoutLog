package de.fklappan.app.workoutlog.ui.detailviewworkout

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.fklappan.app.workoutlog.R
import de.fklappan.app.workoutlog.databinding.ListItemWorkoutResultBinding
import de.fklappan.app.workoutlog.domain.toPrettyString

class WorkoutResultAdapter(private val clickListener: (WorkoutResultGuiModel) -> Unit,
                           private val optionsListener: (View, WorkoutResultGuiModel) -> Unit) :
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
        holder.bindItem(items.get(position), clickListener, optionsListener)
    }

    override fun getItemCount() = items.size

    fun addItem(workoutResultGuiModel: WorkoutResultGuiModel, index: Int) {
        items.add(index, workoutResultGuiModel)
        notifyItemInserted(index)
    }

    fun deleteItem(workoutResultGuiModel: WorkoutResultGuiModel): Int {
        val removedIndex = items.indexOf(workoutResultGuiModel)
        if (items.remove(workoutResultGuiModel)) {
            notifyItemRemoved(removedIndex)
        }
        return removedIndex
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ListItemWorkoutResultBinding.bind(itemView)

        fun bindItem(workoutGuiModel: WorkoutResultGuiModel,
                     clickListener: (WorkoutResultGuiModel) -> Unit,
                     optionsListener: (View, WorkoutResultGuiModel) -> Unit) {
            binding.textViewDate.text = workoutGuiModel.date.toPrettyString()
            binding.textViewContent.text = workoutGuiModel.score
            itemView.setOnClickListener { clickListener.invoke(workoutGuiModel) }
            binding.imageButtonMore.setOnClickListener { optionsListener.invoke(it, workoutGuiModel) }
            binding.imageViewPr.visibility =
                if (workoutGuiModel.pr)
                    View.VISIBLE
                else
                    View.GONE
            binding.imageViewFeeling.visibility =
                if (workoutGuiModel.feeling.isNullOrEmpty())
                    View.GONE
                else
                    View.VISIBLE
            binding.imageViewNote.visibility =
                if (workoutGuiModel.note.isNullOrEmpty())
                    View.GONE
                else
                    View.VISIBLE
        }
    }
}
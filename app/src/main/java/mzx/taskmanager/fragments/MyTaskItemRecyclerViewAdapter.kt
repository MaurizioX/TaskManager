package mzx.taskmanager.fragments


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import mzx.taskmanager.databinding.FragmentTaskitemBinding
import mzx.taskmanager.fragments.TaskListFragment.OnListFragmentInteractionListener
import mzx.taskmanager.model.TaskUi
import mzx.taskmanager.model.TaskUiDiff

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class MyTaskItemRecyclerViewAdapter(
    values: List<TaskUi>,
    private val listener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<MyTaskItemRecyclerViewAdapter.ViewHolder>() {

    private val onClickListener = View.OnClickListener { v ->
        listener?.onListFragmentInteraction(v.tag as TaskUi)
    }

    var values = values
        set(value) {
            DiffUtil.calculateDiff(TaskUiDiff(field, value)).apply {
                field = value
                dispatchUpdatesTo(this@MyTaskItemRecyclerViewAdapter)
            }
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(FragmentTaskitemBinding.inflate(LayoutInflater.from(parent.context)), onClickListener)


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemBinding.apply {
            holder.itemView.tag = values[position]
            task = values[position]
            executePendingBindings()
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        }
    }

    override fun getItemCount(): Int = values.size

    class ViewHolder(val itemBinding: FragmentTaskitemBinding, val listener: View.OnClickListener) :
        RecyclerView.ViewHolder(itemBinding.root) {
        init {
            itemView.setOnClickListener(listener)
        }
    }

}

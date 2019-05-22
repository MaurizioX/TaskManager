package mzx.taskmanager.model

import android.os.Parcelable
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.DiffUtil
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TaskUi(val id: String, val name: String, val note: String?, val isDone: Boolean) : Parcelable

class TaskUiDiff(private val oldItems: List<TaskUi>, private val newItems: List<TaskUi>) : DiffUtil.Callback() {
    companion object{
        const val STATUS_VALUE = "status_value"
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldItems[oldItemPosition].id == newItems[newItemPosition].id

    override fun getOldListSize(): Int = oldItems.size

    override fun getNewListSize(): Int = newItems.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = oldItems[oldItemPosition] == newItems[newItemPosition]

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? = bundleOf(STATUS_VALUE to newItems[newItemPosition].isDone)
}
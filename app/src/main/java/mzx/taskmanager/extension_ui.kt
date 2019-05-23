package mzx.taskmanager

import android.widget.TextView
import androidx.databinding.BindingAdapter


@BindingAdapter("statusValue")
fun TextView.setStatusValue(statusValue: Boolean) {
    this.setText(
        if (statusValue) {
            R.string.done_label
        } else {
            R.string.pending_label
        }
    )

}
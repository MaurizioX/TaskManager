package mzx.taskmanager

import android.widget.TextView
import androidx.databinding.BindingAdapter


@BindingAdapter("statuXValue")
fun TextView.setStatusValue(statusValue: Boolean) {
    this.setText(
        if (statusValue) {
            R.string.done
        } else {
            R.string.done
        }
    )

}
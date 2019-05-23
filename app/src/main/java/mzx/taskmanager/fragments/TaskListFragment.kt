package mzx.taskmanager.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_taskitem_list.*
import mzx.taskmanager.R
import mzx.taskmanager.main.MainViewModel
import mzx.taskmanager.model.TaskUi
import org.koin.android.viewmodel.ext.android.sharedViewModel

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [TaskListFragment.OnListFragmentInteractionListener] interface.
 */
class TaskListFragment : Fragment() {
    private var columnCount = 1

    private val mainViewModel by sharedViewModel<MainViewModel>()

    private var listener: OnListFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_taskitem_list, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        fab.setOnClickListener {
            listener?.navToCreate()

        }

        with(list) {
            layoutManager = when {
                columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }
            adapter = MyTaskItemRecyclerViewAdapter(emptyList(), listener).apply {
                mainViewModel.allTaskUiList.observe(this@TaskListFragment, Observer {
                    this.values = it
                    if (it.isEmpty()) {
                        no_task.visibility = View.VISIBLE
                        list.visibility = View.GONE
                    } else {
                        list.visibility = View.VISIBLE
                        no_task.visibility = View.GONE
                    }
                })
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: TaskUi)
        fun navToCreate()
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            TaskListFragment().apply {
                arguments = bundleOf(ARG_COLUMN_COUNT to columnCount)
            }
    }
}

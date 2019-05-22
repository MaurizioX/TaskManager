package mzx.taskmanager.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_edit_task.*
import mzx.taskmanager.databinding.FragmentEditTaskBinding
import mzx.taskmanager.main.MainViewModel
import mzx.taskmanager.model.TaskUi
import org.koin.android.viewmodel.ext.android.sharedViewModel

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [EditTaskFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 *
 */
class EditTaskFragment : Fragment() {
    companion object {
        const val TASK = "task"
    }

    private val mainViewModel by sharedViewModel<MainViewModel>()

    private lateinit var task: TaskUi

    private var listener: OnFragmentInteractionListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        task = arguments?.getParcelable(TASK) ?: throw IllegalArgumentException("Argument needed")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentEditTaskBinding.inflate(inflater, container, false)
        .apply { task = this@EditTaskFragment.task }.root

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        delete_button.setOnClickListener { mainViewModel.deleteTask(task) }
        is_done_button.apply {
            setOnClickListener { mainViewModel.updateTask(task) }
            visibility = if (task.isDone) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
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
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

}

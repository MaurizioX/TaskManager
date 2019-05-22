package mzx.taskmanager.main

import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.activity_main.*
import mzx.taskmanager.R
import mzx.taskmanager.fragments.CreateTaskFragment
import mzx.taskmanager.fragments.EditTaskFragment
import mzx.taskmanager.fragments.TaskListFragment
import mzx.taskmanager.model.TaskUi

class MainActivity : AppCompatActivity(), TaskListFragment.OnListFragmentInteractionListener,
    EditTaskFragment.OnFragmentInteractionListener, CreateTaskFragment.OnFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener {
            findNavController(R.id.nav_host_fragment).navigate(R.id.action_taskListFragment_to_createTaskFragment)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onListFragmentInteraction(item: TaskUi) {
        findNavController(R.id.nav_host_fragment).navigate(
            R.id.action_taskListFragment_to_editTaskFragment,
            bundleOf(EditTaskFragment.TASK to item)
        )
    }

}

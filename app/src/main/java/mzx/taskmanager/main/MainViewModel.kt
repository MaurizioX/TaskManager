package mzx.taskmanager.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import mzx.taskmanager.api.TaskApi
import mzx.taskmanager.model.TaskUi
import timber.log.Timber

class MainViewModel constructor(private val taskApi: TaskApi) : ViewModel() {
    fun createTask(name: String, note: String): LiveData<Int> {
        taskApi.createTask(name, note, false)
        TODO()
    }

    fun deleteTask(task: TaskUi) {
        taskApi.deleteTask(task.id)
    }

    fun updateTask(task: TaskUi) {
        taskApi.updateTask(task.id, true)
    }

    private val _allTaskUiList = MutableLiveData<List<TaskUi>>().apply {
        taskApi.getAllTasks().observeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread())
            .subscribe({ next ->
                run {
                    Timber.i("Read item $next")
                    postValue(next.map { TaskUi(it.id, it.name, it.note, it.isDone) })
                }
            },
                { error -> Timber.e("Error ${error.message} , ${error.printStackTrace()}") })
    }
    val allTaskUiList: LiveData<List<TaskUi>> = _allTaskUiList
}

sealed class RequestState
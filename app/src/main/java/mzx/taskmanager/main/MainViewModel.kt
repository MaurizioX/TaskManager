package mzx.taskmanager.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import mzx.taskmanager.api.TaskApi
import mzx.taskmanager.model.TaskUi
import timber.log.Timber

class MainViewModel constructor(private val taskApi: TaskApi) : ViewModel() {

    var allTaskDisposable: Disposable? = null
    private val _allTaskUiList = MutableLiveData<List<TaskUi>>().apply {
        allTaskDisposable = getAllTaskAsynch()
    }

    private fun getAllTaskAsynch(): Disposable? {
        allTaskDisposable?.dispose()
        return taskApi.getAllTasks().observeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread())
            .subscribe({ next ->
                run {
                    Timber.i("Read item $next")
                    _allTaskUiList.postValue(next.map { TaskUi(it.id, it.name, it.note, it.isDone) })
                }
            },
                { error -> Timber.e("Error ${error.message} , ${error.printStackTrace()}") })

    }

    val allTaskUiList: LiveData<List<TaskUi>> = _allTaskUiList

    private val _createTask = MutableLiveData<RequestState>().apply { postValue(INIT) }
    val createTask: LiveData<RequestState> = _createTask
    private var createTaskDisposable: Disposable? = null
    fun createTask(name: String, note: String) {
        _createTask.postValue(LOADING)
        createTaskDisposable =
            taskApi.createTask(name, note, false).observeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { next ->
                        _createTask.postValue(SUCCESS)
                        allTaskDisposable = getAllTaskAsynch()
                    },
                    { error -> Timber.e("Create Error ${error.message} , ${error.printStackTrace()}") })
    }

    private val _deleteTask = MutableLiveData<RequestState>().apply { postValue(INIT) }
    val deleteTask: LiveData<RequestState> = _deleteTask
    private var deleteTaskDisposable: Disposable? = null
    fun deleteTask(task: TaskUi) {
        _deleteTask.postValue(LOADING)
        deleteTaskDisposable =
            taskApi.deleteTask(task.id).observeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { next ->
                        _deleteTask.postValue(SUCCESS)
                        allTaskDisposable = getAllTaskAsynch()
                    },
                    { error -> Timber.e("Delete Error ${error.message} , ${error.printStackTrace()}") })
    }

    private val _updateTask = MutableLiveData<RequestState>().apply { postValue(INIT) }
    val updateTask: LiveData<RequestState> = _updateTask
    private var updateTaskDisposable: Disposable? = null
    fun updateTask(task: TaskUi) {
        _updateTask.postValue(LOADING)
        updateTaskDisposable =
            taskApi.updateTask(task.id, true).observeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { next ->
                        _updateTask.postValue(SUCCESS)
                        allTaskDisposable = getAllTaskAsynch()
                    },
                    { error -> Timber.e("UpdateError ${error.message} , ${error.printStackTrace()}") })
    }

    override fun onCleared() {
        super.onCleared()
        deleteTaskDisposable?.dispose()
        createTaskDisposable?.dispose()
        allTaskDisposable?.dispose()
        updateTaskDisposable?.dispose()
    }


}

sealed class RequestState
object INIT : RequestState()
object LOADING : RequestState()
object SUCCESS : RequestState()
object ERROR : RequestState()

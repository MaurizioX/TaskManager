package mzx.taskmanager.api

import io.reactivex.Observable
import mzx.taskmanager.api.data.TaskData

interface TaskApi {
    fun getAllTasks(): Observable<List<TaskData>>
    fun createTask(name: String, note: String?, isDone: Boolean): Observable<TaskData?>
    fun deleteTask(id: String): Observable<Boolean?>
    fun updateTask(id: String, isDone: Boolean):Observable<TaskData?>
}
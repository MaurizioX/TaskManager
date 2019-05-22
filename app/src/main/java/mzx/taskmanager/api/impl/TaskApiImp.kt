package mzx.taskmanager.api.impl

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.rx2.Rx2Apollo
import io.reactivex.Observable
import mzx.taskmanager.AllTasksQuery
import mzx.taskmanager.CreateTaskMutation
import mzx.taskmanager.DeleteTaskMutation
import mzx.taskmanager.UpdateTaskStatusMutation
import mzx.taskmanager.api.TaskApi
import mzx.taskmanager.api.data.TaskData


class TaskApiImp constructor(private val apolloClient: ApolloClient) : TaskApi {
    override fun deleteTask(id: String): Observable<Boolean?> =
        Rx2Apollo.from(apolloClient.mutate(DeleteTaskMutation())).map { t: Response<DeleteTaskMutation.Data> ->
            t.data()?.deleteTask()
        }


    override fun updateTask(id: String, isDone: Boolean): Observable<TaskData?> =
        Rx2Apollo.from(apolloClient.mutate(UpdateTaskStatusMutation.builder().build())).map { response ->
            response.data()?.updateTaskStatus()?.let { TaskDataImpl(it.id(), it.name(), it.note(), it.isDone) }
        }


    override fun createTask(name: String, note: String?, isDone: Boolean): Observable<TaskData?> =
        Rx2Apollo.from(apolloClient.mutate(CreateTaskMutation.builder().build())).map { response ->
            response.data()?.createTask()?.let { TaskDataImpl(it.id(), it.name(), it.note(), it.isDone) }
        }


    override fun getAllTasks(): Observable<List<TaskData>> =
        Rx2Apollo.from(apolloClient.query(AllTasksQuery.builder().build())).map {
            it.data()?.allTasks()?.mapNotNull { task ->
                TaskDataImpl(task.id(), task.name(), task.note(), task.isDone)
            }
        }
}


private data class TaskDataImpl constructor(
    override val id: String,
    override val name: String,
    override val note: String?,
    override val isDone: Boolean
) : TaskData
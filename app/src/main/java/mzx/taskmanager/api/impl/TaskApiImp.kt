package mzx.taskmanager.api.impl

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.rx2.Rx2Apollo
import io.reactivex.Observable
import mzx.taskmanager.AllTasksQuery
import mzx.taskmanager.CreateTaskMutation
import mzx.taskmanager.api.TaskApi
import mzx.taskmanager.api.data.TaskData


class TaskApiImp constructor(private val apolloClient: ApolloClient) : TaskApi {
    override fun deleteTask(id: String): Observable<Boolean?> =
        TODO()
//        Rx2Apollo.from(apolloClient.mutate(DeleteTaskMutation())).map { t: Response<DeleteTaskMutation.Data> ->
//            t.data()?.deleteTask
//        }


    override fun updateTask(id: String, isDone: Boolean): Observable<TaskData?> {
        TODO()
    }

//        Rx2Apollo.from(apolloClient.mutate(UpdateTaskStatusMutation())).map { t: Response<UpdateTaskStatusMutation.Data> ->
//            t.data()?.updateTaskStatus?.let { TaskDataImpl(it.id, it.name, it.note, it.isDone) }
//        }


    override fun createTask(name: String, note: String?, isDone: Boolean) =
        TODO()
//        Rx2Apollo.from(apolloClient.mutate(CreateTaskMutation.builder()))


    override fun getAllTasks(): Observable<List<TaskData>> =
        Rx2Apollo.from(apolloClient.query(AllTasksQuery.builder().build())).map {
            it.data()?.allTasks()?.mapNotNull { task ->
                TaskDataImpl(task.id(), task.name(), task.note(), task.isDone)
            }

//            it.data()?.allTasks?.filterNotNull()?.map {
        }
}


private data class TaskDataImpl constructor(
    override val id: String,
    override val name: String,
    override val note: String?,
    override val isDone: Boolean
) : TaskData
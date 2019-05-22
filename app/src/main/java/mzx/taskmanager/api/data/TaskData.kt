package mzx.taskmanager.api.data


interface TaskData {
    val id: String
    val name: String
    val note: String?
    val isDone: Boolean
}
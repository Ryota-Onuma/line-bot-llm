package model

enum class Priority {
    Low, Medium, High, Vital
}

data class Task(
    val name: String,
    val description: String,
    val priority: Priority
)

fun List<Task>.tasksAsTable(): String {
    return """
        <table border="1">
            <tr>
                <th>Name</th>
                <th>Description</th>
                <th>Priority</th>
            </tr>
            ${this.joinToString("") { task ->
                """
                <tr>
                    <td>${task.name}</td>
                    <td>${task.description}</td>
                    <td>${task.priority}</td>
                </tr>
                """
            }}
        </table>
    """.trimIndent()
}

object TaskRepository {
    private val tasks = mutableListOf(
        Task("cleaning", "Clean the house", Priority.Low),
        Task("gardening", "Mow the lawn", Priority.Medium),
        Task("shopping", "Buy the groceries", Priority.High),
        Task("painting", "Paint the fence", Priority.Medium),
        Task("painting", "aaa", Priority.Low)
    )

    fun allTasks(): List<Task> = tasks

    fun tasksByPriority(priority: Priority) = tasks.filter {
        it.priority == priority
    }

    fun taskByName(name: String) = tasks.find {
        it.name.equals(name, ignoreCase = true)
    }

    fun addTask(task: Task) {
        if(taskByName(task.name) != null) {
            throw IllegalStateException("Cannot duplicate task names!")
        }
        tasks.add(task)
    }
}
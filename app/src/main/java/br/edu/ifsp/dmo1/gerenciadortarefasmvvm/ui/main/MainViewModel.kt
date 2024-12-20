package br.edu.ifsp.dmo1.gerenciadortarefasmvvm.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.edu.ifsp.dmo1.gerenciadortarefasmvvm.data.dao.TaskDao
import br.edu.ifsp.dmo1.gerenciadortarefasmvvm.data.model.Task

class MainViewModel : ViewModel() {
    private val dao = TaskDao
    private var currentState = "All"

    private val _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>>
        get() {
            return _tasks
        }

    private val _insertTask = MutableLiveData<Boolean>()
    val insertTask: LiveData<Boolean> = _insertTask

    private val _updateTask = MutableLiveData<Boolean>()
    val updateTask: LiveData<Boolean>
        get() = _updateTask

    fun filterTasks(status: String) {
        val tasks = dao.getAll()
        _tasks.value = when (status) {
            "Done" -> tasks.filter { it.isCompleted }
            "To do" -> tasks.filter { !it.isCompleted }
            else -> tasks
        }
        currentState = status
    }

    fun insertTask(description: String) {
        val task = Task(description, false)
        dao.add(task)
        _insertTask.value = true
        load()
    }

    fun updateTask(position: Long) {
        val task = dao.get(position)
        if (task != null) {
            task.isCompleted = !task.isCompleted
            _updateTask.value = true
            load()
        }
    }

    private fun load() {
        filterTasks(currentState)
    }
}
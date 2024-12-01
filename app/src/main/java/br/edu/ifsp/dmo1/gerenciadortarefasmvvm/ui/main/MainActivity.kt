package br.edu.ifsp.dmo1.gerenciadortarefasmvvm.ui.main

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import br.edu.ifsp.dmo1.gerenciadortarefasmvvm.R
import br.edu.ifsp.dmo1.gerenciadortarefasmvvm.databinding.ActivityMainBinding
import br.edu.ifsp.dmo1.gerenciadortarefasmvvm.databinding.DialogNewTaskBinding
import br.edu.ifsp.dmo1.gerenciadortarefasmvvm.ui.adapter.SpinnerItemAdapter
import br.edu.ifsp.dmo1.gerenciadortarefasmvvm.ui.adapter.TaskAdapter
import br.edu.ifsp.dmo1.gerenciadortarefasmvvm.ui.listener.TaskClickListener

class MainActivity : AppCompatActivity(), TaskClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: TaskAdapter
    private val status = arrayOf("All", "Done", "To do")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        configListview()
        configOnClickListener()
        configObservers()
        configSpinner()
    }

    override fun clickDone(position: Long) {
        viewModel.updateTask(position)
    }

    private fun configListview() {
        adapter = TaskAdapter(this, mutableListOf(), this)
        binding.listTasks.adapter = adapter
    }

    private fun configObservers() {
        viewModel.tasks.observe(this, Observer {
            adapter.updateTasks(it)
        })

        viewModel.insertTask.observe(this, Observer {
            val str = if(it){
                getString(R.string.task_inserted_sucess)
            }
            else {
                getString(R.string.task_inserted_error)
            }

            Toast.makeText(this, str, Toast.LENGTH_LONG).show()
        })

        viewModel.updateTask.observe(this, Observer{
            if(it){
                Toast.makeText(
                    this,
                    getString(R.string.task_updated_success),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun configOnClickListener() {
        binding.buttonAddTask.setOnClickListener {
            openDialogNewTask()
        }
    }

    private fun configSpinner() {
        binding.spinner.adapter = SpinnerItemAdapter(
            this,
            status,
        )

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedStatus = status[position]
                viewModel.filterTasks(selectedStatus)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun openDialogNewTask() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_new_task, null)
        val bindingDialog = DialogNewTaskBinding.bind(dialogView)

        val builder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle(getString(R.string.add_new_task))
            .setPositiveButton(
                getString(R.string.save),
                DialogInterface.OnClickListener { dialog, which ->
                    val description = bindingDialog.editDescription.text.toString()
                    viewModel.insertTask(description)
                    dialog.dismiss()
                })
            .setNegativeButton(
                getString(R.string.cancel),
                DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                })

        val dialog = builder.create()
        dialog.show()
    }
}
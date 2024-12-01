package br.edu.ifsp.dmo1.gerenciadortarefasmvvm.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import br.edu.ifsp.dmo1.gerenciadortarefasmvvm.R
import br.edu.ifsp.dmo1.gerenciadortarefasmvvm.databinding.SpinnerItemBinding
import br.edu.ifsp.dmo1.gerenciadortarefasmvvm.databinding.TasklistItemBinding

class SpinnerItemAdapter(context: Context, dataset: Array<String>):
    ArrayAdapter<String>(context, R.layout.spinner_item, dataset) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: SpinnerItemBinding
        if (convertView == null) {
            binding = SpinnerItemBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
            binding.root.tag = binding
        } else {
            binding = convertView.tag as SpinnerItemBinding
        }

        val status = getItem(position)

        if (status != null) {
            binding.spinnerText.text = status
        }

        return binding.root
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getView(position, convertView, parent)
    }
}
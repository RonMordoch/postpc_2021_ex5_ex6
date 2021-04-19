package exercise.android.reemh.todo_items

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TodoItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val text: TextView = view.findViewById(R.id.itemText)
    val checkBox: CheckBox = view.findViewById(R.id.itemCheckBox)
}

class TodoItemsAdapter(holder: TodoItemsHolder) : RecyclerView.Adapter<TodoItemViewHolder>() {

    private var _todoItemsHolder = holder
    private var bool = false

    override fun getItemCount(): Int = _todoItemsHolder.currentItems.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoItemViewHolder {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.row_todo_item, parent, false)
        return TodoItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodoItemViewHolder, position: Int) {
        val item = _todoItemsHolder.currentItems[position]
        holder.text.text = item.text
        holder.checkBox.isChecked = item.isDone
        holder.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                _todoItemsHolder.markItemDone(position)
            } else {
                _todoItemsHolder.markItemInProgress(position)
            }
            notifyDataSetChanged()

        }
    }

}

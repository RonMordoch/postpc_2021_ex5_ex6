package exercise.android.reemh.todo_items

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class TodoItemsAdapter(holder: TodoItemsHolder) : RecyclerView.Adapter<TodoItemsAdapter.TodoItemViewHolder>() {

    class TodoItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.itemText)
        val checkBox: CheckBox = view.findViewById(R.id.itemCheckBox)
        val deleteButton : Button = view.findViewById(R.id.deleteItemButton)
    }

    private val _todoItemsHolder = holder

    override fun getItemCount(): Int = _todoItemsHolder.currentItems.size

    override fun onViewRecycled(holder: TodoItemViewHolder) {
        super.onViewRecycled(holder)
        holder.checkBox.setOnCheckedChangeListener(null)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoItemViewHolder {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.row_todo_item, parent, false)
        return TodoItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodoItemViewHolder, position: Int) {
        val item = _todoItemsHolder.currentItems[position]
        holder.textView.text = item.text
        holder.checkBox.isChecked = item.isDone
        holder.textView.paintFlags = if (item.isDone) Paint.STRIKE_THRU_TEXT_FLAG else 0
        holder.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                _todoItemsHolder.markItemDone(position)
            } else {
                _todoItemsHolder.markItemInProgress(position)
            }
            notifyDataSetChanged()
        }

        holder.deleteButton.setOnClickListener{
            _todoItemsHolder.deleteItem(item)
            notifyDataSetChanged()
        }
    }

}

package exercise.android.reemh.todo_items

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


const val EXTRA_ROW_TODO_ITEM : String = "ROW_TODO_ITEM"

class TodoItemsAdapter(dataStore: TodoListDataStore) : RecyclerView.Adapter<TodoItemsAdapter.TodoItemViewHolder>() {

    class TodoItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewItem: TextView = view.findViewById(R.id.textViewItem)
        val checkBoxItem: CheckBox = view.findViewById(R.id.checkBoxItem)
        val buttonDeleteItem: Button = view.findViewById(R.id.buttonDeleteItem)
    }

    private val _todoItemsDataStore = dataStore
    // define a click callback function that receives a TodoItem
    var onTodoItemRowClickCallback : ((TodoItem) -> Unit)? = null

    override fun getItemCount(): Int = _todoItemsDataStore.currentItemsSize

    override fun onViewRecycled(holder: TodoItemViewHolder) {
        super.onViewRecycled(holder)
        holder.checkBoxItem.setOnCheckedChangeListener(null)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoItemViewHolder {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.row_todo_item, parent, false)
        return TodoItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodoItemViewHolder, position: Int) {
        val item = _todoItemsDataStore.currentItems[position]
        // set checkbox according to item state and set strike-through effects accordingly
        holder.checkBoxItem.isChecked = item.isDone
        holder.textViewItem.text = item.description
        holder.textViewItem.paintFlags = if (item.isDone) Paint.STRIKE_THRU_TEXT_FLAG else 0

        // set on click listener for the check-box
        holder.checkBoxItem.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                _todoItemsDataStore.markItemDone(item)
            } else {
                _todoItemsDataStore.markItemInProgress(item)
            }
            notifyDataSetChanged()
        }

        // set on click listener to delete the current binded item
        holder.buttonDeleteItem.setOnClickListener {
            _todoItemsDataStore.deleteItem(item)
            notifyDataSetChanged()
        }

        // set on click listener for the text view that launches the edit-item activity
        holder.textViewItem.setOnClickListener {
            val callback = onTodoItemRowClickCallback ?: return@setOnClickListener
            callback(item)
        }
    }

}


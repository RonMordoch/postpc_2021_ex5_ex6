package exercise.android.reemh.todo_items

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), TodoItemRowClickListener {

    private lateinit var fabCreateItem: FloatingActionButton
    private lateinit var editTextTask: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TodoItemsAdapter
    private lateinit var appContext : MyApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        appContext = applicationContext as MyApp
        recyclerView = findViewById(R.id.recyclerTodoItemsList)
        editTextTask = findViewById(R.id.editTextInsertTask)
        fabCreateItem = findViewById(R.id.buttonCreateTodoItem)

        // set recycler view and adapter
        adapter = TodoItemsAdapter(appContext.dataStore)
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.adapter = adapter
        adapter.onTodoItemRowClickCallback = { item: TodoItem ->
            onTodoItemRowClicked(item)
        }

        // set on click listener for create item button
        fabCreateItem.setOnClickListener {
            val description = editTextTask.text.toString()
            if (description != "") { // if the edit-text is empty (no input), nothing happens
                appContext.dataStore.addNewInProgressItem(description)
                editTextTask.setText("")
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
//        outState.putSerializable(HOLDER_BUNDLE_KEY, appContext.dataStore)
        outState.putString(USER_BUNDLE_KEY, editTextTask.text.toString())
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
//        holderDataStore.holder = savedInstanceState.getSerializable(HOLDER_BUNDLE_KEY) as TodoItemsHolderImpl
        editTextTask.setText(savedInstanceState.getString(USER_BUNDLE_KEY))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EDIT_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                val modifiedItem = data!!.getSerializableExtra(EXTRA_MODIFIED_ITEM) as TodoItem
                appContext.dataStore.updateItem(modifiedItem)
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onTodoItemRowClicked(item: TodoItem) {
        val editTodoItemIntent = Intent(this@MainActivity, EditTodoItemActivity::class.java)
        editTodoItemIntent.putExtra(EXTRA_ROW_TODO_ITEM, item)
        startActivityForResult(editTodoItemIntent, EDIT_ACTIVITY_REQUEST_CODE)
    }

    companion object {
//        private const val HOLDER_BUNDLE_KEY = "todo_items_holder"
        private const val USER_BUNDLE_KEY = "user_input"
        private const val EXTRA_ROW_TODO_ITEM = "ROW_TODO_ITEM"
        private const val EDIT_ACTIVITY_REQUEST_CODE = 123
    }
}
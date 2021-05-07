package exercise.android.reemh.todo_items

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), TodoItemRowClickListener {

    lateinit var holderDataStore: TodoListDataStore
    private lateinit var fabCreateItem: FloatingActionButton
    private lateinit var editTextTask: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TodoItemsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // initialize data source
        val app: MyApp = applicationContext as MyApp
        holderDataStore = app.dataStore
        // find initial views
        recyclerView = findViewById(R.id.recyclerTodoItemsList)
        editTextTask = findViewById(R.id.editTextInsertTask)
        fabCreateItem = findViewById(R.id.buttonCreateTodoItem)

        // set recycler view and adapter
        adapter = TodoItemsAdapter(holderDataStore)
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.adapter = adapter
        adapter.onTodoItemRowClickCallback = { item: TodoItem ->
            onTodoItemRowClicked(item)
        }

        // set on click listener for create item button
        fabCreateItem.setOnClickListener {
            val description = editTextTask.text.toString()
            if (description == "") { // if the edit-text is empty (no input), nothing happens
                return@setOnClickListener
            }
            holderDataStore.addNewInProgressItem(description)
            editTextTask.setText("")
            adapter.notifyDataSetChanged()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable(HOLDER_BUNDLE_KEY, holderDataStore.holder)
        outState.putString(USER_BUNDLE_KEY, editTextTask.text.toString())
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        holderDataStore.holder = savedInstanceState.getSerializable(HOLDER_BUNDLE_KEY) as TodoItemsHolderImpl
        editTextTask.setText(savedInstanceState.getString(USER_BUNDLE_KEY))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 123) {
            if (resultCode == RESULT_OK) {
                val modifiedItem = data!!.getSerializableExtra(EXTRA_MODIFIED_ITEM) as TodoItem
                holderDataStore.updateItem(modifiedItem)
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onTodoItemRowClicked(item: TodoItem) {
        val editTodoItemIntent = Intent(this@MainActivity, EditTodoItemActivity::class.java)
        editTodoItemIntent.putExtra(EXTRA_ROW_TODO_ITEM, item)
        startActivityForResult(editTodoItemIntent, 123)
    }

    companion object {
        private const val HOLDER_BUNDLE_KEY = "todo_items_holder"
        private const val USER_BUNDLE_KEY = "user_input"
        private const val EXTRA_ROW_TODO_ITEM = "ROW_TODO_ITEM"
    }
}
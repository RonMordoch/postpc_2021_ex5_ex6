package exercise.android.reemh.todo_items;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity
{

    private static final String HOLDER_BUNDLE_KEY = "todo_items_holder",
            USER_BUNDLE_KEY = "user_input";

    public TodoListDataStore holderDataStore;// = null;
    private FloatingActionButton fabCreateItem;
    private EditText editTextTask;
    private RecyclerView recyclerView;
    private TodoItemsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        if (savedInstanceState != null) { // on create is called again on screen rotation
//            holder = (TodoItemsHolder) savedInstanceState.getSerializable(HOLDER_BUNDLE_KEY);
//        }
//        if (holder == null) {
//            holder = ((MyApp)getApplicationContext()).holder;
//        }
        MyApp app = (MyApp) getApplicationContext();
        holderDataStore = app.dataStore;

        recyclerView = findViewById(R.id.recyclerTodoItemsList);
        adapter = new TodoItemsAdapter(holderDataStore);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        editTextTask = findViewById(R.id.editTextInsertTask);
        fabCreateItem = findViewById(R.id.buttonCreateTodoItem);
        fabCreateItem.setOnClickListener(v -> {
            String description = editTextTask.getText().toString();
            if (description.equals("")) { // if the edit-text is empty (no input), nothing happens
                return;
            }
            holderDataStore.addNewInProgressItem(description);
            editTextTask.setText("");
            adapter.notifyDataSetChanged();
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putSerializable(HOLDER_BUNDLE_KEY, holderDataStore.holder);
        outState.putString(USER_BUNDLE_KEY, editTextTask.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        holderDataStore.holder = (TodoItemsHolderImpl) savedInstanceState.getSerializable(HOLDER_BUNDLE_KEY);
        editTextTask.setText(savedInstanceState.getString(USER_BUNDLE_KEY));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123)
        {
            if (resultCode == RESULT_OK)
            {
                TodoItem modifiedItem = (TodoItem) data.getSerializableExtra("MODIFIED_ITEM");
                holderDataStore.updateItem(modifiedItem);
                adapter.notifyDataSetChanged();
            }
        }
    }
}


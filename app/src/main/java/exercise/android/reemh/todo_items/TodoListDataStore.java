package exercise.android.reemh.todo_items;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class TodoListDataStore
{

    public TodoItemsHolderImpl holder;

    public TodoListDataStore(Context context)
    {
//        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        holder = new TodoItemsHolderImpl();
    }
}

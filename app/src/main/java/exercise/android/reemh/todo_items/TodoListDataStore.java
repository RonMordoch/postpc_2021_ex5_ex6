package exercise.android.reemh.todo_items;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.Serializable;
import java.util.List;

public class TodoListDataStore
{
    public static final String HOLDER_SIZE_KEY = "holder_size",
    ITEM_TEXT_KEY = "itemText#",
            ITEM_IS_DONE_KEY = "itemIsDone#",
            ITEM_CREATION_DATE_KEY = "itemCreationDate#";
    public TodoItemsHolderImpl holder;
    private SharedPreferences sp;


    public TodoListDataStore(Context context) {
        sp = PreferenceManager.getDefaultSharedPreferences(context);
        holder = new TodoItemsHolderImpl();
        loadDataFromSP();
    }

    public void addNewInProgressItem(String description) {
        holder.addNewInProgressItem(description);
        saveDataToSP();
    }

    public void markItemDone(int itemIndex) {
        holder.markItemDone(itemIndex);
        saveDataToSP();
    }

    public void markItemInProgress(int itemIndex) {
        holder.markItemInProgress(itemIndex);
        saveDataToSP();
    }

    public void deleteItem(TodoItem item) {
        holder.deleteItem(item);
        saveDataToSP();
    }

    public void deleteItem(int itemIndex) {
        holder.deleteItem(itemIndex);
        saveDataToSP();
    }

    private void saveDataToSP()
    {
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.putInt(HOLDER_SIZE_KEY, holder.getCurrentItems().size());
        for (int i = 0; i < holder.getCurrentItems().size(); i++)
        {
            TodoItem currentItem = holder.getCurrentItems().get(i);
            // store current item's info in SP
            editor.putString(ITEM_TEXT_KEY + i, currentItem.getText());
            editor.putBoolean(ITEM_IS_DONE_KEY + i, currentItem.getIsDone());
        }
        editor.apply();
    }

    private void loadDataFromSP()
    {
        int size = sp.getInt(HOLDER_SIZE_KEY, 0);
        TodoItemsHolderImpl newHolder = new TodoItemsHolderImpl();
        for (int i = 0; i < size; i++)
        {
            // load the data into the new holder
            newHolder.addNewInProgressItem(sp.getString(ITEM_TEXT_KEY + i, ""));
            boolean currentItemIsDone = sp.getBoolean(ITEM_IS_DONE_KEY + i, false);
            if (currentItemIsDone)
            {
                newHolder.markItemDone(0); // item will always be in index 0 after adding
            }
        }
        holder = newHolder;
    }

}

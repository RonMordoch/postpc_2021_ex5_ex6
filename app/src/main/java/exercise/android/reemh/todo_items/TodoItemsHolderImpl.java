package exercise.android.reemh.todo_items;

import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TodoItemsHolderImpl implements TodoItemsHolder
{

    // In-progress items are displayed in the beginning, Done items in the end
    private List<TodoItem> items = new ArrayList<>();
//    private final SharedPreferences sp;
//
//    public TodoItemsHolderImpl(SharedPreferences sharedPreferences)
//    {
//        sp = sharedPreferences;
//        loadDataFromSharedPreferences();
//    }
//
//    private void loadDataFromSharedPreferences()
//    {
//        int size = sp.getInt("num_items", 0);
//        List<TodoItem> loadedItems = new ArrayList<>();
//        for (int i =0; i < size; i++)
//        {
//            String itemName = "item" + Integer.toString(i);
//            TodoItem item = new TodoItem(sp.getString(itemName + "_text", ""));
//            items.add(item);
//        }
//        items = loadedItems;
//    }
//
//    private void saveDataToSharedPreferences()
//    {
//        SharedPreferences.Editor editor = sp.edit();
//        editor.putInt("num_items", items.size());
//        for (int i = 0; i < items.size(); i++)
//        {
//            String itemName = "item" + i;
//            editor.putString(itemName + "_text", items.get(i).getText());
//        }
//        editor.apply();
//    }

    @Override
    public List<TodoItem> getCurrentItems() {
        return items;
    }

    @Override
    public void addNewInProgressItem(String description) {
        TodoItem item = new TodoItem(description);
        items.add(0, item);  // the last-created item is the first item in the list
//        saveDataToSharedPreferences();
    }

    @Override
    public void markItemDone(int itemIndex) {
        if (itemIndex >= items.size()){
            throw  new IndexOutOfBoundsException();
        }
        TodoItem item = items.get(itemIndex);
        setItemState(item, true);
    }

    @Override
    public void markItemInProgress(int itemIndex) {
        if (itemIndex >= items.size()){
            throw  new IndexOutOfBoundsException();
        }
        TodoItem item = items.get(itemIndex);
        setItemState(item, false);
    }

    private void setItemState(TodoItem item, boolean newState)
    {
        if (item.getIsDone() == newState)
        {
            return;
        }
        item.setIsDone(newState);
        sortItems();
//        saveDataToSharedPreferences();
    }

    @Override
    public void deleteItem(TodoItem item) {
        for (TodoItem todoItem: items)
        {
            if (todoItem == item){
                items.remove(item);
//                saveDataToSharedPreferences();
                return;
            }
        }

    }

    private void sortItems()
    {
        Collections.sort(items);
    }
}
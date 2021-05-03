package exercise.android.reemh.todo_items;

import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TodoItemsHolderImpl implements TodoItemsHolder
{

    // In-progress items are displayed in the beginning, Done items in the end
    private List<TodoItem> items = new ArrayList<>();


    @Override
    public List<TodoItem> getCurrentItems() {
        return items;
    }

    @Override
    public void addNewInProgressItem(String description) {
        TodoItem item = new TodoItem(description);
        items.add(0, item);  // the last-created item is the first item in the list
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
    }

    @Override
    public void deleteItem(TodoItem item) {
        for (TodoItem todoItem: items)
        {
            if (todoItem == item){
                items.remove(item);
                return;
            }
        }
    }

    @Override
    public void deleteItem(int itemIndex)
    {
        if (itemIndex >= items.size()){
            throw  new IndexOutOfBoundsException();
        }
        items.remove(itemIndex);
    }

    private void sortItems()
    {
        Collections.sort(items);
    }
}
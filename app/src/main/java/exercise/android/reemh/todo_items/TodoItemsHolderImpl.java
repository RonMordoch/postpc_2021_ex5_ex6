package exercise.android.reemh.todo_items;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TodoItemsHolderImpl implements TodoItemsHolder
{

    // In-progress items are displayed in the beginning, Done items in the end
    private final List<TodoItem> items = new ArrayList<>();


    @Override
    public List<TodoItem> getCurrentItems() {
        return new ArrayList<>(items); // return a shallow copy of all the items
    }

    @Override
    public void addNewInProgressItem(String description) {
        TodoItem item = new TodoItem(description);
        items.add(0, item);  // the last-created item is the first item in the list
    }

    @Override
    public void markItemDone(TodoItem item) {
        setItemState(item, true);
    }

    @Override
    public void markItemInProgress(TodoItem item) {
        setItemState(item, false);
    }



    @Override
    public void deleteItem(TodoItem item) {
        for (TodoItem todoItem : items) {
            if (todoItem == item) {
                items.remove(item);
                return;
            }
        }
    }

    public void updateItem(TodoItem item)
    {
        for (TodoItem todoItem : items)
        {
            // creation time serves as a unique key for each items ( nano second precision)
            if (todoItem.getCreationTime().equals(item.getCreationTime()))
            {
                // remove the old version of item and add the new one
                items.remove(todoItem);
                items.add(item);
                break;
            }
        }
        // sort items, in case the new modified item progress status was changed
        sortItems();
    }

    public void sortItems() {
        Collections.sort(items);
    }

    private void setItemState(TodoItem item, boolean newState) {
        if (item.getIsDone() == newState) {
            return;
        }
        item.setIsDone(newState);
        sortItems();
    }
}
package exercise.android.reemh.todo_items;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class TodoItemsHolderImpl implements TodoItemsHolder
{

    // In-progress items are displayed in the beginning, Done items in the end
    private final List<TodoItem> items = new ArrayList<>();


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
        if (item.getIsDone())
        {
            return; // avoid unnecessary sorting
        }
        // item state is IN-PROGRESS
        item.setIsDone(true);
        sortItems();

    }

    @Override
    public void markItemInProgress(int itemIndex) {
        if (itemIndex >= items.size()){
            throw  new IndexOutOfBoundsException();
        }
        TodoItem item = items.get(itemIndex);
        if (!item.getIsDone())
        {
            return; // avoid unnecessary sorting
        }
        // item state is DONE
        item.setIsDone(false);
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

    private void sortItems()
    {
        Collections.sort(items);
    }
}

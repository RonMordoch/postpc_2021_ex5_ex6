package exercise.android.reemh.todo_items;

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
    public void markItemDone(TodoItem item) {
        setItemState(item, true);
    }

    @Override
    public void markItemInProgress(TodoItem item) {
        setItemState(item, false);
    }

    private void setItemState(TodoItem item, boolean newState) {
        if (item.getIsDone() == newState) {
            return;
        }
        item.setIsDone(newState);
        sortItems();
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

    private void sortItems() {
        Collections.sort(items);
    }
}
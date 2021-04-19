package exercise.android.reemh.todo_items;

import java.io.Serializable;

public class TodoItem implements Serializable, Comparable<TodoItem>
{
    private final String text;
    private boolean isDone;

    TodoItem(String itemText) {
        text = itemText;
        isDone = false;
    }

    public boolean getIsDone() {
        return isDone;
    }

    public void setIsDone(boolean val) {
        isDone = val;
    }

    public String getText() {
        return text;
    }

    public int compareTo(TodoItem other)
    {
        boolean otherIsDone = other.getIsDone();
        if (otherIsDone)
        {
            return -1;
        }
        return 0;
    }
}

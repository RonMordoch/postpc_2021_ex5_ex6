package exercise.android.reemh.todo_items;

import java.io.Serializable;
import java.util.Date;


public class TodoItem implements Serializable, Comparable<TodoItem>
{
    private final String text;
    private boolean isDone;
    private Date creationTime;

    TodoItem(String itemText) {
        text = itemText;
        isDone = false;
        creationTime = new Date();
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

    public Date getCreationTime(){return creationTime;}

    public int compareTo(TodoItem other)
    {
        boolean otherIsDone = other.getIsDone();
        if (isDone == otherIsDone)
        {
            return other.getCreationTime().compareTo(creationTime); // last-created item is the first item in the list
        }
        if (otherIsDone)
        {
            return -1; // DONE items are showed last
        }
        return 0;
    }
}
package exercise.android.reemh.todo_items;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;


public class TodoItem implements Serializable, Comparable<TodoItem>
{
    private String description;
    private boolean isDone;
    private LocalDateTime creationTime;
    private LocalDateTime lastModifiedTime;

    TodoItem(String itemDescription) {
        description = itemDescription;
        isDone = false;
        creationTime = LocalDateTime.now(ZoneId.systemDefault());
        lastModifiedTime = creationTime;
    }

    public boolean getIsDone() {
        return isDone;
    }

    public void setIsDone(boolean val) {
        isDone = val;
        updateLastModified();
    }

    public void setDescription(String newDescription) {
        description = newDescription;
        updateLastModified();
    }

    public void setCreationTime(LocalDateTime newCreationTime)
    {
        creationTime = newCreationTime;
    }

    public void setLastModifiedTime(LocalDateTime newLastModifiedTime)
    {
        lastModifiedTime = newLastModifiedTime;
    }


    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public LocalDateTime getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void updateLastModified() {
        lastModifiedTime = LocalDateTime.now(ZoneId.systemDefault());
    }

    public int compareTo(TodoItem other) {
        boolean otherIsDone = other.getIsDone();
        if (!isDone && !otherIsDone) { // both items are in progress, sort by creation time where the last-created item is the first item in the list
            return other.getCreationTime().compareTo(creationTime);
        }
        else if (isDone && otherIsDone) // both items are done, sort by last modified where the most recently modified is the first item in the list
        {
            return other.getLastModifiedTime().compareTo(lastModifiedTime);
        }
        else if (!isDone && otherIsDone) { // other item is done, show last
            return -1;
        }
        return 0;
    }

}


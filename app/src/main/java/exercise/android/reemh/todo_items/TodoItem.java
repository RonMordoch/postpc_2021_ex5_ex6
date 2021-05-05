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
        if (isDone == otherIsDone) {
            return other.getCreationTime().compareTo(creationTime); // last-created item is the first item in the list
        }
        if (otherIsDone) {
            return -1; // DONE items are showed last
        }
        return 0;
    }

}


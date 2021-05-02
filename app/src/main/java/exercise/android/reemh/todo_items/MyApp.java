package exercise.android.reemh.todo_items;

import android.app.Application;

public class MyApp extends Application
{

    public TodoItemsHolder holder;


    @Override
    public void onCreate()
    {
        super.onCreate();
        holder = new TodoItemsHolderImpl();
    }
}

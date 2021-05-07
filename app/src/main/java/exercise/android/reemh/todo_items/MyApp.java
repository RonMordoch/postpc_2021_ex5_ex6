package exercise.android.reemh.todo_items;

import android.app.Application;

public class MyApp extends Application
{
    public TodoListDataStore dataStore;

    @Override
    public void onCreate() {
        super.onCreate();
        dataStore = new TodoListDataStore(this);
    }



}

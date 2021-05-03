package exercise.android.reemh.todo_items;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class MyApp extends Application
{
    public TodoListDataStore dataStore;

    @Override
    public void onCreate() {
        super.onCreate();
        dataStore = new TodoListDataStore(this);
    }
}

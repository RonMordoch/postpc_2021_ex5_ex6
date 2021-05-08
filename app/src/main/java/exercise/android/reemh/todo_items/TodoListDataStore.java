package exercise.android.reemh.todo_items;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * A wrapper class that extends functionality of TodoItemsHolderImpl in order to persist data
 */
public class TodoListDataStore
{
    // key constant names for the SharedPreferences object
    public static final String SP_HOLDER_KEY = "HOLDER_JSON";
    public static final String HOLDER_SIZE_KEY = "holder_size",
            ITEM_DESCRIPTION_KEY = "itemDescription#",
            ITEM_IS_DONE_KEY = "itemIsDone#",
            ITEM_CREATION_TIME_KEY = "itemCreationTime#",
            ITEM_LAST_MODIFIED_TIME_KEY = "itemLastModifiedTime#";
    public TodoItemsHolderImpl holder;
    private final SharedPreferences sp;
    private static final Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDate>() {
        @Override
        public LocalDate deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return LocalDate.parse(json.getAsJsonPrimitive().getAsString());
        }
    }).create();

    public TodoListDataStore(Context context) {
        sp = PreferenceManager.getDefaultSharedPreferences(context);
        holder = new TodoItemsHolderImpl();
        loadDataFromSP();

    }

    /** Wrapper methods for TodoItemsHolderImpl **/

    public void addNewInProgressItem(String description) {
        holder.addNewInProgressItem(description);
        saveDataToSP();
    }

    public void markItemDone(TodoItem item) {
        holder.markItemDone(item);
        saveDataToSP();
    }

    public void markItemInProgress(TodoItem item) {
        holder.markItemInProgress(item);
        saveDataToSP();
    }

    public void deleteItem(TodoItem item) {
        holder.deleteItem(item);
        saveDataToSP();
    }

    public void updateItem(TodoItem item)
    {
        holder.updateItem(item);
        saveDataToSP();
    }

    /** Saves the entire holder data to the SP object **/
    private void saveDataToSP() {
//        SharedPreferences.Editor editor = sp.edit();
//        editor.clear();
//        String holderJson = gson.toJson(holder);
//        editor.putString(SP_HOLDER_KEY, holderJson);
//        editor.apply();
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.putInt(HOLDER_SIZE_KEY, holder.getCurrentItems().size());
        for (int i = 0; i < holder.getCurrentItems().size(); i++) {
            TodoItem currentItem = holder.getCurrentItems().get(i);
            // store current item's info in SP
            editor.putString(ITEM_DESCRIPTION_KEY + i, currentItem.getDescription());
            editor.putBoolean(ITEM_IS_DONE_KEY + i, currentItem.getIsDone());
            // store creation time and last modified time
            String creationTimeString = currentItem.getCreationTime().toString();
            editor.putString(ITEM_CREATION_TIME_KEY + i, creationTimeString);
            String lastModifiedTimeString = currentItem.getLastModifiedTime().toString();
            editor.putString(ITEM_LAST_MODIFIED_TIME_KEY + i, lastModifiedTimeString);
        }
        editor.apply();
    }

    /** Loads the entire holder data from the SP object **/
    private void loadDataFromSP() {
//        String holderJson = sp.getString(SP_HOLDER_KEY, "");
//        if (!holderJson.equals("")) {
//            holder = gson.fromJson(holderJson, TodoItemsHolderImpl.class);
//        }
//        if (holderJson.equals(""))
//        {
//            return; // error reading an empty string from shared preferences
//        }
//        Gson gson = new Gson();
//        Type token = new TypeToken<ArrayList<TodoItem>>() {}.getType();
//        holder = gson.fromJson(holderJson, token);
//        // newHolder.sortItems(); // todo see if gson keeps the order

        int size = sp.getInt(HOLDER_SIZE_KEY, 0);
        TodoItemsHolderImpl newHolder = new TodoItemsHolderImpl();
        for (int i = 0; i < size; i++) {
            // load the description and progress into the new holder
            newHolder.addNewInProgressItem(sp.getString(ITEM_DESCRIPTION_KEY + i, ""));
            TodoItem currentItem = newHolder.getCurrentItems().get(0);
            boolean currentItemIsDone = sp.getBoolean(ITEM_IS_DONE_KEY + i, false);
            if (currentItemIsDone) {
                newHolder.markItemDone(currentItem); // item will always be in index 0 after adding
            }
            // load the creation and last-modified times
            String creationTimeString = sp.getString(ITEM_CREATION_TIME_KEY + i, "");
            LocalDateTime creationTime = LocalDateTime.parse(creationTimeString);
            currentItem.setCreationTime(creationTime);
            String lastModifiedTimeString = sp.getString(ITEM_LAST_MODIFIED_TIME_KEY + i, "");
            LocalDateTime lastModifiedTime = LocalDateTime.parse(lastModifiedTimeString);
            currentItem.setLastModifiedTime(lastModifiedTime);
        }
        holder = newHolder;
        holder.sortItems(); // seems like SP does not save the same order of items, sort on loading
    }
}

package exercise.android.reemh.todo_items

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId

const val EXTRA_MODIFIED_ITEM: String = "MODIFIED_ITEM"


class EditTodoItemActivity : AppCompatActivity() {

    lateinit var item: TodoItem
    lateinit var textViewLastModifiedTime: TextView
    lateinit var editTextItemText: EditText
    lateinit var checkBoxItemEdit: CheckBox
    lateinit var textViewCreationTime: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_todo_item)

        editTextItemText = findViewById(R.id.editTextItemText)
        checkBoxItemEdit = findViewById(R.id.checkBoxItemEdit)
        textViewCreationTime = findViewById(R.id.textViewCreationTime)
        textViewLastModifiedTime = findViewById(R.id.textViewLastModifiedTime)

        // get the item we are currently editing
        item = intent.getSerializableExtra(EXTRA_ROW_TODO_ITEM) as TodoItem
        initViews()

        // set on click listeners for editText
        editTextItemText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val newDescription: String = editTextItemText.text.toString()
                item.description = newDescription
                item.updateLastModified(); // update last modified after changing text
                returnResultToLaunchingActivity()

            }
        })
        // set on click listener for check box
        checkBoxItemEdit.setOnCheckedChangeListener { buttonView, isChecked ->
            item.isDone = isChecked
            item.updateLastModified(); // update last modified after changing text
            returnResultToLaunchingActivity()
        }
    }

    private fun initViews()
    {
        editTextItemText.setText(item.description)
        checkBoxItemEdit.isChecked = item.isDone
        // set the creation date and last-modified text views
        setTextViewCreationTime()
        setTextViewLastModifiedTime()
    }

    /** Initialize the creationTime text view according to the item's creation date */
    private fun setTextViewCreationTime() {
        textViewCreationTime.text = getString(R.string.creation_date,item.creationTime.dayOfMonth, item.creationTime.monthValue, item.creationTime.year)
    }

    /**
    If the last-modified time was less than a hour ago, the text is "<minutes> minutes ago"
    If the last-modified time was earlier than a hour, but today, the text is "Today at <hour>"
    If the last-modified time was yesterday or earlier, the text is "<date> at <hour>"
    Refresh the text only upon entry to the edit screen (this activity)
     */
    private fun setTextViewLastModifiedTime() {
        val currentDateTime: LocalDateTime = LocalDateTime.now(ZoneId.systemDefault())
        val duration: Duration = Duration.between(item.lastModifiedTime, currentDateTime)
        // for the two last cases
        val minutes = if (item.lastModifiedTime.minute < 10) "0${item.lastModifiedTime.minute}" else "${item.lastModifiedTime.minute}"
        val hour = "${item.lastModifiedTime.hour}:$minutes"
        when {
            duration.toMinutes() < MINUTES_IN_HOUR -> {
                textViewLastModifiedTime.text = getString(R.string.modified_less_than_hour, duration.toMinutes())
            }
            ((duration.toHours() < HOURS_IN_DAY) && (item.lastModifiedTime.dayOfMonth == currentDateTime.dayOfMonth)) -> {
                textViewLastModifiedTime.text = getString(R.string.modified_today_more_than_hour, hour)
            }
            else -> {
                textViewLastModifiedTime.text = getString(R.string.modified_yesterday_or_earlier, item.lastModifiedTime.toLocalDate().toString(), hour)
            }
        }
    }

    private fun returnResultToLaunchingActivity() {
        val intentBack = Intent()
        intentBack.putExtra(EXTRA_MODIFIED_ITEM, item)
        setResult(RESULT_OK, intentBack)
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_BUNDLE_LAST_MODIFIED, textViewLastModifiedTime.text.toString())
        outState.putString(KEY_BUNDLE_CREATION_TIME, textViewCreationTime.text.toString())
        outState.putString(KEY_BUNDLE_EDIT_ITEM_TEXT, editTextItemText.text.toString())
        outState.putBoolean(KEY_BUNDLE_ITEM_CHECKED, checkBoxItemEdit.isChecked)
        outState.putSerializable(KEY_BUNDLE_ITEM, item)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        item = savedInstanceState.getSerializable(KEY_BUNDLE_ITEM) as TodoItem
        textViewLastModifiedTime.text = savedInstanceState.getString(KEY_BUNDLE_LAST_MODIFIED)
        textViewCreationTime.text = savedInstanceState.getString(KEY_BUNDLE_CREATION_TIME)
        editTextItemText.setText(savedInstanceState.getString(KEY_BUNDLE_EDIT_ITEM_TEXT))
        checkBoxItemEdit.isChecked = savedInstanceState.getBoolean(KEY_BUNDLE_ITEM_CHECKED)
    }

    companion object{
        const val MINUTES_IN_HOUR : Int = 60
        const val HOURS_IN_DAY : Int = 24
        private const val KEY_BUNDLE_LAST_MODIFIED = "key_bundle_last_modified"
        private const val KEY_BUNDLE_CREATION_TIME = "key_bundle_creation_time"
        private const val KEY_BUNDLE_EDIT_ITEM_TEXT = "key_bundle_edit_item_text"
        private const val KEY_BUNDLE_ITEM_CHECKED = "key_bundle_item_checked"
        private const val KEY_BUNDLE_ITEM = "key_bundle_item"
    }

}
package exercise.android.reemh.todo_items

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDateTime
import java.time.ZoneId

class EditTodoItemActivity : AppCompatActivity() {

    lateinit var item: TodoItem
    lateinit var textViewLastModifiedTime: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_todo_item)

        val editTextItemText: EditText = findViewById(R.id.editTextItemText)
        val checkBoxItemEdit: CheckBox = findViewById(R.id.checkBoxItemEdit)
        val textViewCreationTime: TextView = findViewById(R.id.textViewCreationTime)
        textViewLastModifiedTime = findViewById(R.id.textViewLastModifiedTime)

        // set initial views
        // get the item we are currently editing
        item = intent.getSerializableExtra(EXTRA_ROW_TODO_ITEM) as TodoItem
        // set the edit text and check box accordingly
        editTextItemText.setText(item.description)
        checkBoxItemEdit.isChecked = item.isDone
        // set the creation date text view
        val itemCreationDateStr = "Created on: ${item.creationTime.dayOfMonth}-${item.creationTime.monthValue}-${item.creationTime.year}"
        textViewCreationTime.text = itemCreationDateStr
        textViewCreationTime.isClickable = false
        // set the last modified text view
        setTextViewLastModifiedTime()

        // set on click listeners or editText and checkBox
        editTextItemText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val newDescription: String = editTextItemText.text.toString()
                item.description = newDescription
                item.updateLastModified(); // update last modified after changing text
            }
        })
    }

    /**
    If the last-modified time was less than a hour ago, the text is "<minutes> minutes ago"
    If the last-modified time was earlier than a hour, but today, the text is "Today at <hour>"
    If the last-modified time was yesterday or earlier, the text is "<date> at <hour>"
    Refresh the text only upon entry to the edit screen (this activity)
     */
    private fun setTextViewLastModifiedTime() {
        // get the fields of the current time
        val localDateTime: LocalDateTime = LocalDateTime.now(ZoneId.systemDefault())
        val currentMinute: Int = localDateTime.minute
        val currentHour: Int = localDateTime.hour
        val currentDay: Int = localDateTime.dayOfYear
        val currentYear: Int = localDateTime.year
        // get the fields of the last modified time
        val lastModifiedMinute: Int = item.lastModifiedTime.minute
        val lastModifiedHour: Int = item.lastModifiedTime.hour
        val lastModifiedDay: Int = item.lastModifiedTime.dayOfYear // from 1 to 365, 366 in leap year
        val lastModifiedYear: Int = item.lastModifiedTime.year

        val textViewLastModifiedText: String
        // item was modified in previous days in current year on in previous years
        if ((currentYear == lastModifiedYear && currentDay > lastModifiedDay) || (currentYear > lastModifiedYear)) {
            textViewLastModifiedText = "${item.lastModifiedTime.toLocalDate()} at $lastModifiedHour"
        }
        // else, item was last modified today in the current year
        // item was modified more than an hour ago
        else if ((currentHour > lastModifiedHour) && (currentMinute >= lastModifiedMinute)) {
            textViewLastModifiedText = "Today at $lastModifiedHour"
        }
        // item was modified less then an hour ago
        else {
            textViewLastModifiedText = "${currentMinute - lastModifiedMinute} minutes ago"
        }
        textViewLastModifiedTime.text = textViewLastModifiedText;

    }


}
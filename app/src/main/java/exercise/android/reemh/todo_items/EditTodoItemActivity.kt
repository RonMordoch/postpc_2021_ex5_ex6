package exercise.android.reemh.todo_items

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDateTime
import java.time.ZoneId

const val EXTRA_MODIFIED_ITEM : String = "MODIFIED_ITEM"

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
        // set the edit text and check box accordingly
        editTextItemText.setText(item.description)
        checkBoxItemEdit.isChecked = item.isDone
        // set the creation date and last-modified text views
        setTextViewCreationTime()
        setTextViewLastModifiedTime()

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
            returnResultToLaunchingActivity()
        }
    }

    private fun returnResultToLaunchingActivity() {
        val intentBack = Intent()
        intentBack.putExtra(EXTRA_MODIFIED_ITEM, item)
        setResult(RESULT_OK, intentBack)
    }

    /** Initialize the creationTime text view according to the item's creation date */
    private fun setTextViewCreationTime() {
        val itemCreationDateStr = "Created on: ${item.creationTime.dayOfMonth}-${item.creationTime.monthValue}-${item.creationTime.year}"
        textViewCreationTime.text = itemCreationDateStr
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
        // note that if the currentHour is less than lastModifiedHour because a day has passed,
        // e.g. currentHour is 1:10 (AM) and lastModifiedHour is 23:10, then we will catch it in the
        // previous condition
        else if ((currentHour > lastModifiedHour) && (currentMinute >= lastModifiedMinute)) {
            textViewLastModifiedText = "Today at $lastModifiedHour:$lastModifiedMinute"
        }
        // item was modified less then an hour ago
        else {
            // abs because we can have to cases when less than an hour passed:
            // 1. Same hour but currentMinute is larger , e.g. 15:30 -> 15:40
            //2. Different hour but currentMinute is smaller, e.g. 15:30 -> 16:10
            textViewLastModifiedText = "${kotlin.math.abs(lastModifiedMinute - currentMinute)} minutes ago"
        }
        textViewLastModifiedTime.text = textViewLastModifiedText;
    }


}
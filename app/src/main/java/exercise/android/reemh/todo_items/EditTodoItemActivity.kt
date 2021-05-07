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
        val currentDateTime: LocalDateTime = LocalDateTime.now(ZoneId.systemDefault())
        val duration: Duration = Duration.between(item.lastModifiedTime, currentDateTime)
        when {
            duration.toMinutes() < 60 -> {
                textViewLastModifiedTime.text = "${duration.toMinutes()} minutes ago"
            }
            duration.toHours() < 24 -> {
                textViewLastModifiedTime.text = "Today at ${item.lastModifiedTime.hour}"
            }
            else -> {
                textViewLastModifiedTime.text = "${item.lastModifiedTime.toLocalDate()} at ${item.lastModifiedTime.hour}"
            }
        }
    }

    private fun returnResultToLaunchingActivity() {
        val intentBack = Intent()
        intentBack.putExtra(EXTRA_MODIFIED_ITEM, item)
        setResult(RESULT_OK, intentBack)
    }

}
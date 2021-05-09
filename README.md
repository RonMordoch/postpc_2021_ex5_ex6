# TodoItems List

An Android exercise for developers teaching how to play around with RecyclerView and Adapter

I pledge the highest level of ethical principles in support of academic excellence. 
I ensure that all of my work reflects my own abilities and not those of someone else.

## Question (ex5):
We didn't define any UX flow to let users edit a description on an existing TODO item.
Which UX flow will you define?
In your response notice the following:
1. how easy is it for users to figure out this flow in their first usage? (for example, a glowing button is more discoverable then a swipe-left gesture)
2. how hard to implement will your solution be?
3. how consistent is this flow with regular "edit" flows in the Android world?

## Answer:
I will define an edit button, with a clear, intuitive design that lets the users infer its purpose, e.g. 
a button with the text 'EDIT' (like my delete button) or a button with the pencil icon drawable.
The button will be visible next to the other buttons in the row , and thus will be very clear for users to figure out in their first usage as it is visible.
It will be somewhat easy to implement the solution, we will need to set onClickListener for this button and launch a new activity
from the RecyclerView that will let the user edit the clicked item there, and send the information back from the launched activity into the previous one.
This solution will be consistent with the regular "edit" flows in Android and in general (for example web pages), as the pencil icon is used quite commonly for such purpose.


## Notes
My implementation is to save the holder data structure piece by piece which is not as efficient as
using GSON, however GSON raises issues when trying to deserialize the TodoItem's LocalDateTime objects. I have tried
the solutions offered online to no avail, and decided not to store them as strings and repeatedly convert them back and forth.

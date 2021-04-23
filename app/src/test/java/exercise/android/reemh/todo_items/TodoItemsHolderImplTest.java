package exercise.android.reemh.todo_items;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TodoItemsHolderImplTest
{

    @Test
    public void when_addingTodoItem_then_callingListShouldHaveThisItem() {
        // setup
        TodoItemsHolderImpl holderUnderTest = new TodoItemsHolderImpl();
        Assert.assertEquals(0, holderUnderTest.getCurrentItems().size());

        // test
        holderUnderTest.addNewInProgressItem("do shopping");

        // verify
        Assert.assertEquals(1, holderUnderTest.getCurrentItems().size());
    }

    @Test
    public void when_addingTodoItem_then_stateShouldBeInProgress() {
        TodoItemsHolderImpl holderUnderTest = new TodoItemsHolderImpl();
        for (int i = 0; i < 10; i++) {
            holderUnderTest.addNewInProgressItem("finish ex" + Integer.toString(i));
        }
        for (int i = 0; i < 10; i++) {
            Assert.assertFalse(holderUnderTest.getCurrentItems().get(i).getIsDone());
        }
    }

    @Test
    public void when_addingTodoItem_then_itemTextShouldBeCorrect() {
        TodoItemsHolderImpl holderUnderTest = new TodoItemsHolderImpl();
        for (int i = 0; i < 10; i++) {
            holderUnderTest.addNewInProgressItem("finish ex" + Integer.toString(i));
        }
        for (int i = 0; i < 10; i++) {
            // remember that items are inserted in the beginning
            Assert.assertEquals("finish ex" + Integer.toString(9-i), holderUnderTest.getCurrentItems().get(i).getText());
        }
    }

    @Test
    public void when_deletingToDoItem_then_itemShouldBeDeleted()
    {
        TodoItemsHolderImpl holderUnderTest = new TodoItemsHolderImpl();
        holderUnderTest.addNewInProgressItem("delete me if you dare");
        holderUnderTest.addNewInProgressItem("super important dont delete");
        Assert.assertEquals(2, holderUnderTest.getCurrentItems().size());
        TodoItem toDeleteItem = holderUnderTest.getCurrentItems().get(1);
        holderUnderTest.deleteItem(toDeleteItem);
        Assert.assertEquals(1, holderUnderTest.getCurrentItems().size());
    }

    @Test
    public void when_deleteItemNotInHolder_then_nothingShouldHappen()
    {
        TodoItemsHolderImpl holderUnderTest = new TodoItemsHolderImpl();
        holderUnderTest.addNewInProgressItem("in1");
        holderUnderTest.addNewInProgressItem("in2");
        TodoItem item = new TodoItem("outside");
        List<TodoItem> beforeDeleteCall = new ArrayList<>(holderUnderTest.getCurrentItems());
        holderUnderTest.deleteItem(item);
        Assert.assertEquals(beforeDeleteCall, holderUnderTest.getCurrentItems());

    }

    @Test
    public void when_markingItemWithWrongIndex_then_throwException()
    {
        TodoItemsHolderImpl holderUnderTest = new TodoItemsHolderImpl();
        holderUnderTest.addNewInProgressItem("in1");
        try
        {
            holderUnderTest.markItemInProgress(5);
        }
        catch (IndexOutOfBoundsException e)
        {
            // good :)
        }
        try
        {
            holderUnderTest.markItemDone(2);
        }
        catch (IndexOutOfBoundsException e)
        {
            // good :)
        }
    }


    @Test
    public void when_settingItemProgress_then_ProgressShouldBeCorrect()
    {
        TodoItemsHolderImpl holderUnderTest = new TodoItemsHolderImpl();
        holderUnderTest.addNewInProgressItem("in progress item");
        holderUnderTest.addNewInProgressItem("done item");
        holderUnderTest.addNewInProgressItem("shouldnt change because others");
        TodoItem inProgressItem = holderUnderTest.getCurrentItems().get(2),
                doneItem = holderUnderTest.getCurrentItems().get(1),
                otherItem = holderUnderTest.getCurrentItems().get(0);
        holderUnderTest.markItemDone(1);
        Assert.assertFalse(inProgressItem.getIsDone());
        Assert.assertFalse(otherItem.getIsDone());
        Assert.assertTrue(doneItem.getIsDone());
        // set back doneItem to in progress
        holderUnderTest.markItemInProgress(2);
        Assert.assertFalse(inProgressItem.getIsDone());
        Assert.assertFalse(otherItem.getIsDone());
        Assert.assertFalse(doneItem.getIsDone());
    }


    @Test
    public void whenMarkingItemDone_then_itemShouldBeInTheEnd() {
        TodoItemsHolderImpl holderUnderTest = new TodoItemsHolderImpl();
        holderUnderTest.addNewInProgressItem("first");
        holderUnderTest.addNewInProgressItem("second");
        holderUnderTest.addNewInProgressItem("third"); // third, second, first
        holderUnderTest.markItemDone(1); // third, first, second
        Assert.assertEquals("third", holderUnderTest.getCurrentItems().get(0).getText());
        Assert.assertEquals("first", holderUnderTest.getCurrentItems().get(1).getText());
        Assert.assertEquals("second", holderUnderTest.getCurrentItems().get(2).getText());
    }

    @Test
    public void whenAddingItems_and_markingAllDone_then_orderShouldBeReversed()
    {
        TodoItemsHolderImpl holderUnderTest = new TodoItemsHolderImpl();
        holderUnderTest.addNewInProgressItem("first after delete, third before");
        holderUnderTest.addNewInProgressItem("second after delete, second before");
        holderUnderTest.addNewInProgressItem("third after delete, first before");
        Assert.assertEquals("third after delete, first before", holderUnderTest.getCurrentItems().get(0).getText());
        Assert.assertEquals("second after delete, second before", holderUnderTest.getCurrentItems().get(1).getText());
        Assert.assertEquals("first after delete, third before", holderUnderTest.getCurrentItems().get(2).getText());
        holderUnderTest.markItemDone(0);
        holderUnderTest.markItemDone(0);
        holderUnderTest.markItemDone(0); // mark all in progress items as done which will be in beginning
        Assert.assertEquals("first after delete, third before", holderUnderTest.getCurrentItems().get(0).getText());
        Assert.assertEquals("second after delete, second before", holderUnderTest.getCurrentItems().get(1).getText());
        Assert.assertEquals("third after delete, first before", holderUnderTest.getCurrentItems().get(2).getText());
    }
}
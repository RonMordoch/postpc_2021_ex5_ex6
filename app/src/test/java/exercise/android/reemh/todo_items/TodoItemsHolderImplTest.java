package exercise.android.reemh.todo_items;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
            Assert.assertEquals("finish ex" + Integer.toString(9-i), holderUnderTest.getCurrentItems().get(i).getDescription());
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
    public void when_settingItemProgress_then_ProgressShouldBeCorrect()
    {
        TodoItemsHolderImpl holderUnderTest = new TodoItemsHolderImpl();
        holderUnderTest.addNewInProgressItem("in progress item");
        holderUnderTest.addNewInProgressItem("done item");
        holderUnderTest.addNewInProgressItem("shouldnt change because others");
        TodoItem inProgressItem = holderUnderTest.getCurrentItems().get(2),
                doneItem = holderUnderTest.getCurrentItems().get(1),
                otherItem = holderUnderTest.getCurrentItems().get(0);
        holderUnderTest.markItemDone(doneItem);
        Assert.assertFalse(inProgressItem.getIsDone());
        Assert.assertFalse(otherItem.getIsDone());
        Assert.assertTrue(doneItem.getIsDone());
        // set back doneItem to in progress
        holderUnderTest.markItemInProgress(doneItem);
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
        TodoItem second = holderUnderTest.getCurrentItems().get(1);
        holderUnderTest.markItemDone(second); // third, first, second
        Assert.assertEquals("third", holderUnderTest.getCurrentItems().get(0).getDescription());
        Assert.assertEquals("first", holderUnderTest.getCurrentItems().get(1).getDescription());
        Assert.assertEquals("second", holderUnderTest.getCurrentItems().get(2).getDescription());
    }

    @Test
    public void whenAddingItems_and_markingAllDone_then_orderShouldBeReversed()
    {
        TodoItemsHolderImpl holderUnderTest = new TodoItemsHolderImpl();
        holderUnderTest.addNewInProgressItem("first after delete, third before");
        TodoItem firstAfterDelete = holderUnderTest.getCurrentItems().get(0);
        holderUnderTest.addNewInProgressItem("second after delete, second before");
        TodoItem secondAfterDelete = holderUnderTest.getCurrentItems().get(0);
        holderUnderTest.addNewInProgressItem("third after delete, first before");
        TodoItem thirdAfterDelete = holderUnderTest.getCurrentItems().get(0);
        Assert.assertEquals("third after delete, first before", holderUnderTest.getCurrentItems().get(0).getDescription());
        Assert.assertEquals("second after delete, second before", holderUnderTest.getCurrentItems().get(1).getDescription());
        Assert.assertEquals("first after delete, third before", holderUnderTest.getCurrentItems().get(2).getDescription());
        holderUnderTest.markItemDone(thirdAfterDelete);
        holderUnderTest.markItemDone(secondAfterDelete);
        holderUnderTest.markItemDone(firstAfterDelete); // mark all in progress items as done which will be in beginning
        Assert.assertEquals("first after delete, third before", holderUnderTest.getCurrentItems().get(0).getDescription());
        Assert.assertEquals("second after delete, second before", holderUnderTest.getCurrentItems().get(1).getDescription());
        Assert.assertEquals("third after delete, first before", holderUnderTest.getCurrentItems().get(2).getDescription());
    }

    @Test
    public void when_allItemsInProgress_then_orderShouldBeByCreationDate() throws InterruptedException {
        TodoItemsHolderImpl holderUnderTest = new TodoItemsHolderImpl();
        holderUnderTest.addNewInProgressItem("created first");
        TodoItem createdFirst = holderUnderTest.getCurrentItems().get(0);
        TimeUnit.SECONDS.sleep(1);
        holderUnderTest.addNewInProgressItem("created second");
        TodoItem createdSecond = holderUnderTest.getCurrentItems().get(0);
        TimeUnit.SECONDS.sleep(1);
        holderUnderTest.addNewInProgressItem("created last");
        TodoItem createdLast = holderUnderTest.getCurrentItems().get(0);
        Assert.assertTrue(createdFirst.getCreationTime().isBefore(createdSecond.getCreationTime()));
        Assert.assertTrue(createdSecond.getCreationTime().isBefore(createdLast.getCreationTime()));
    }
}
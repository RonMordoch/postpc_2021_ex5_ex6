package exercise.android.reemh.todo_items;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;

public class TodoItemsHolderImplTest
{

  @Test
  public void when_addingTodoItem_then_callingListShouldHaveThisItem(){
    // setup
    TodoItemsHolderImpl holderUnderTest = new TodoItemsHolderImpl();
    Assert.assertEquals(0, holderUnderTest.getCurrentItems().size());

    // test
    holderUnderTest.addNewInProgressItem("do shopping");

    // verify
    Assert.assertEquals(1, holderUnderTest.getCurrentItems().size());
  }

  @Test
  public void test()
  {
    TodoItemsHolderImpl holderUnderTest = new TodoItemsHolderImpl();
    holderUnderTest.addNewInProgressItem("first");
    holderUnderTest.addNewInProgressItem("second");
    holderUnderTest.addNewInProgressItem("third"); // third, second, first
    holderUnderTest.markItemDone(1); // third, first, second
    Assert.assertEquals("third", holderUnderTest.getCurrentItems().get(0).getText());
    Assert.assertEquals("first", holderUnderTest.getCurrentItems().get(1).getText());
    Assert.assertEquals("second", holderUnderTest.getCurrentItems().get(2).getText());
  }

  // TODO: add at least 10 more tests to verify correct behavior of your implementation of `TodoItemsHolderImpl` class
}
package edu.union.csc260.project2gpta.tests;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import edu.union.csc260.project2gpta.model.BacklogItem;
import edu.union.csc260.project2gpta.model.Sprint;
import edu.union.csc260.project2gpta.model.Task;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SprintTests {

    private Sprint sprint;
    private BacklogItem backlogItem1;
    private BacklogItem backlogItem2;
    private Task task1;
    private Task task2;
    private Task task3;

    @Before
    public void setUp() {
        sprint = new Sprint("Test Sprint", LocalDate.of(2023, 5, 1), LocalDate.of(2023, 5, 11));
        backlogItem1 = new BacklogItem("Backlog Item Name",
        "Low Priority",
        "Backlog Item Description\n- Line 1\n- Line 2\n- etc...",
        LocalDate.of(2023,5,6),
        LocalDate.of(2023,5,8));

        backlogItem2 = new BacklogItem("Backlog Item Name",
        "Low Priority",
        "Backlog Item Description\n- Line 1\n- Line 2\n- etc...",
        LocalDate.of(2023,5,6),
        LocalDate.of(2023,5,8));

        task1 = new Task("task1","In Progress","desc");
        task2 = new Task("task2","In Progress","desc");
        task3 = new Task("task3","Done","desc");
        backlogItem1.addTask(task1);
        backlogItem1.addTask(task2);
        backlogItem2.addTask(task3);

        
    }

    @After
    public void tearDown(){
        sprint=null;
    }

    @Test
    public void testGetSprintName() {
        assertEquals("Test Sprint", sprint.getSprintName());
    }

    @Test
    public void testGetStartDate() {
        assertEquals(LocalDate.of(2023, 5, 1), sprint.getStartDate());
    }

    @Test
    public void testGetEndDate() {
        assertEquals(LocalDate.of(2023, 5, 11), sprint.getEndDate());
    }


    @Test
    public void testAddBacklog() {
        sprint.addBacklog(backlogItem1);
        assertTrue(sprint.getAllBacklogItems().contains(backlogItem1));
    }

    @Test
    public void testDeleteBacklog() {
        sprint.addBacklog(backlogItem1);
        assertTrue(sprint.getAllBacklogItems().contains(backlogItem1));

        sprint.deleteBacklog(backlogItem1);
        assertFalse(sprint.getAllBacklogItems().contains(backlogItem1));
    }

    @Test
    public void testGetSprintProgress() {
        assertEquals(0.0, sprint.getSprintProgress(), 0.001);
        sprint.addBacklog(backlogItem2);
        assertEquals(1.0, sprint.getSprintProgress(), 0.001);
    }

}

package edu.union.csc260.project2gpta.tests;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.Ignore;
import org.junit.Before;
import org.junit.After;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import edu.union.csc260.project2gpta.model.*;

import javafx.collections.ObservableList;

public class BacklogItemTests {

    private BacklogItem backlogItem;

    @Before
    public void setUp() {
        backlogItem = new BacklogItem("Test BacklogItem", "Medium", "Test Description", LocalDate.now(), LocalDate.now().plusDays(7));
    }

    @Test
    public void testSetName() {
        String newName = "Updated BacklogItem Name";
        backlogItem.setName(newName);
        assertEquals(newName, backlogItem.getName());
    }

    @Test
    public void testSetPriority() {
        String newPriority = "High";
        backlogItem.setPriority(newPriority);
        assertEquals(newPriority, backlogItem.getPriority());
    }

    @Test
    public void testSetBacklogItemDescription() {
        String newDescription = "Updated Test Description";
        backlogItem.setBacklogItemDescription(newDescription);
        assertEquals(newDescription, backlogItem.getBacklogItemDescription());
    }

    @Test
    public void testAddTask() {
        Task newTask = new Task("New Task");
        backlogItem.addTask(newTask);
        ObservableList<Task> tasks = backlogItem.getTasks();
        assertTrue(tasks.contains(newTask));
    }

    @Test
    public void testRemoveTask() {
        Task newTask = new Task("New Task");
        backlogItem.addTask(newTask);
        backlogItem.removeTask(newTask);
        ObservableList<Task> tasks = backlogItem.getTasks();
        assertFalse(tasks.contains(newTask));
    }

    @Test
    public void testEditTask() {
        Task originalTask = new Task("Original Task");
        backlogItem.addTask(originalTask);
        Task updatedTask = new Task("Updated Task");
        backlogItem.editTask(originalTask, updatedTask);
        ObservableList<Task> tasks = backlogItem.getTasks();
        assertFalse(tasks.contains(originalTask));
        assertTrue(tasks.contains(updatedTask));
    }

    @Test
    public void testGetProgress() {
        Task task1 = new Task("Task 1", "Done", "");
        Task task2 = new Task("Task 2", "In Progress", "");
        backlogItem.addTask(task1);
        backlogItem.addTask(task2);
        assertEquals(0.5, backlogItem.getProgress(), 0.0);
    }

    @Test
    public void testSetStartDate() {
        LocalDate newStartDate = LocalDate.now().minusDays(1);
        backlogItem.setStartDate(newStartDate);
        assertEquals(newStartDate, backlogItem.getStartDate());
    }

    @Test
    public void testSetEndDate() {
        LocalDate newEndDate = LocalDate.now().plusDays(14);
        backlogItem.setEndDate(newEndDate);
        assertEquals(newEndDate, backlogItem.getEndDate());
    }
}


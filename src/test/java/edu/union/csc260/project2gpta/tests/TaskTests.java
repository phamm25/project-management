package edu.union.csc260.project2gpta.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import edu.union.csc260.project2gpta.model.*;

@RunWith(MockitoJUnitRunner.class)
public class TaskTests {
    private Task task;

    @Before
    public void setUp() {
        task = new Task("Write unit tests", "To Do", "Write unit tests for Task class");
    }

    @Test
    public void testTaskName() {
        assertEquals("Write unit tests", task.getTaskName());
        task.setTaskName("Write more unit tests");
        assertEquals("Write more unit tests", task.getTaskName());
    }

    @Test
    public void testTaskStatus() {
        assertEquals("To Do", task.getTaskStatus());
        task.setTaskStatus("In Progress");
        assertEquals("In Progress", task.getTaskStatus());
    }

    @Test
    public void testTaskDescription() {
        assertEquals("Write unit tests for Task class", task.getTaskDescription());
        task.setTaskDescription("Write more unit tests for Task class");
        assertEquals("Write more unit tests for Task class", task.getTaskDescription());
    }

    @Test
    public void testTaskStatusValue() {
        assertTrue(task.getTaskStatus().equals("To Do"));
        assertFalse(task.getTaskStatus().equals("Invalid status"));
    }

    @Test
    public void testTaskStatusValueWithMockito() {
        Task mockTask = Mockito.mock(Task.class);
        when(mockTask.getTaskStatus()).thenReturn("To Do");

        assertTrue(mockTask.getTaskStatus().equals("To Do"));
        assertFalse(mockTask.getTaskStatus().equals("Invalid status"));

        verify(mockTask, times(2)).getTaskStatus();
    }
}
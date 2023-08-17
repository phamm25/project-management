package edu.union.csc260.project2gpta.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import edu.union.csc260.project2gpta.model.Group;
import edu.union.csc260.project2gpta.model.GroupManager;
import edu.union.csc260.project2gpta.model.Sprint;

public class GroupManagerTests {

    private GroupManager manager;
    private Group group;

    @Before
    public void setUp(){
        manager = new GroupManager("Garrett", "butlerg@union.edu", "Manager");
    }

    @Test
    public void testGetGroupList(){
        assertTrue(manager.getGroupList().isEmpty());
    }

    @Test
    public void testAddSprint(){
        Sprint sprint = mock(Sprint.class);
        when(sprint.getSprintName()).thenReturn("Example Sprint Name");

        manager.addSprint(sprint);

        assertEquals("Example Sprint Name", manager.getSprintList().get(0).getSprintName());
    }

    @Test
    public void testDeleteSprint(){
        Sprint sprint = mock(Sprint.class);
        manager.addSprint(sprint);
        manager.deleteSprint(sprint);

        assertTrue(manager.getSprintList().isEmpty());
    }

    @Test
    public void testGetDisplayName(){
        assertEquals("Garrett", manager.getDisplayName());
    }

    @Test
    public void testSetDisplayName(){
        manager.setDisplayName("PROgrammer");
        assertEquals("PROgrammer", manager.getDisplayName());
    }

    @Test
    public void testGetEmail(){
        assertEquals("butlerg@union.edu", manager.getEmail());
    }

    @Test
    public void testGetRole(){
        assertEquals("Manager", manager.getRole());
    }

    @Test
    public void testSetRole(){
        manager.setRole("God");
        assertEquals("God", manager.getRole());
    }
}
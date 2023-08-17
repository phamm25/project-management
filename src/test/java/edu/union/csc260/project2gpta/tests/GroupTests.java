package edu.union.csc260.project2gpta.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.Ignore;
import org.junit.Before;
import org.junit.After;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import edu.union.csc260.project2gpta.model.Group;
import edu.union.csc260.project2gpta.model.GroupManager;
import edu.union.csc260.project2gpta.model.GroupMember;
import edu.union.csc260.project2gpta.TestData.*;

public class GroupTests {

    private Group group1;
    private Group group2;

    @Before
    public void setUp() {
        group1 = edu.union.csc260.project2gpta.TestData.ExampleGroup.createTestGroup();

        group2 = edu.union.csc260.project2gpta.TestData.ExampleGroup.createTestGroup();
    }

    @After
    public void tearDown(){
        group1 = null;
        group2 = null;
    }

    @Test
    public void testGetManager(){
        assertEquals("Kristina", group1.getManager().getDisplayName());
    }

    @Test
    public void testSetManager(){
        GroupManager newManager = edu.union.csc260.project2gpta.TestData.ExampleGroupManager.createTestManager();
        GroupManager oldManager = group1.getManager();

        group1.setManager(newManager);

        assertNotSame(oldManager, group1.getManager());
    }

    @Test
    public void testContains(){
        GroupMember member = new GroupMember("Test", "Test");
        group1.addMember(member);

        assertTrue(group1.contains(member));

        GroupMember member2 = new GroupMember("G", "B");
        assertFalse(group1.contains(member2));
    }

    @Test
    public void testGetGroupName(){
        assertEquals("New Group", group1.getGroupName());
    }

    @Test
    public void testSetGroupName(){
        group1.setGroupName("Celtics");
        assertEquals("Celtics", group1.getGroupName());
    }

    @Test
    public void testGetGroupDescription(){
        assertEquals("Some Description", group1.getGroupDescription());
    }

    @Test
    public void testSetGroupDescription(){
        group1.setGroupDescription("Best Team");
        assertEquals("Best Team", group1.getGroupDescription());
    }

    @Test
    public void testAddMember(){
        GroupMember steve = new GroupMember("steve", "steve");
        group1.addMember(steve);

        assertTrue(group1.contains(steve));
        assertEquals("New Group", steve.getGroup().getGroupName());
    }

    @Test
    public void testDeleteMember(){
        GroupMember steve = new GroupMember("steve", "steve");
        group1.addMember(steve);
        group1.deleteMember(steve);

        assertFalse(group1.contains(steve));
        assertEquals(null, steve.getGroup());
    }

}
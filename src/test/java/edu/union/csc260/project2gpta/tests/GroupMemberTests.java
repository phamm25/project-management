package edu.union.csc260.project2gpta.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import edu.union.csc260.project2gpta.model.Group;
import edu.union.csc260.project2gpta.model.GroupMember;
import edu.union.csc260.project2gpta.model.Sprint;

public class GroupMemberTests {

    private GroupMember member;
    private Group group;

    @Before
    public void setUp(){
        member = new GroupMember("Garrett", "butlerg@union.edu", "Member");
    }

    @Test
    public void testSetGroup(){
        Group testGroup = mock(Group.class);
        when(testGroup.getGroupName()).thenReturn("New Group");

        member.setGroup(testGroup);
        assertEquals("New Group", member.getGroup().getGroupName());
    }

    @Test
    public void testAddSprint(){
        Sprint sprint = mock(Sprint.class);
        when(sprint.getSprintName()).thenReturn("Example Sprint Name");

        member.addSprint(sprint);
        assertEquals("Example Sprint Name", member.getSprintList().get(0).getSprintName());
    }

    @Test
    public void testDeleteSprint(){
        Sprint sprint = mock(Sprint.class);
        member.addSprint(sprint);
        member.deleteSprint(sprint);

        assertTrue(member.getSprintList().isEmpty());
    }

    @Test
    public void testGetDisplayName(){
        assertEquals("Garrett", member.getDisplayName());
    }

    @Test
    public void testSetDisplayName(){
        member.setDisplayName("Garret Butler");
        assertEquals("Garret Butler", member.getDisplayName());
    }

    @Test
    public void testGetEmail(){
        assertEquals("butlerg@union.edu", member.getEmail());
    }

    @Test
    public void testGetRole(){
        assertEquals("Member", member.getRole());
    }

    @Test
    public void testSetRole(){
        member.setRole("Member");
        assertEquals("Member", member.getRole());
    }
}
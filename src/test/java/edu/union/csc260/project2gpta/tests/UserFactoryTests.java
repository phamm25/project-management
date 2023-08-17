package edu.union.csc260.project2gpta.tests;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import edu.union.csc260.project2gpta.model.GroupManager;
import edu.union.csc260.project2gpta.model.GroupMember;
import edu.union.csc260.project2gpta.model.UserFactory;

@RunWith(MockitoJUnitRunner.class)
public class UserFactoryTests {

    @Test
    public void testTwoParameterMember() {
        GroupMember member = UserFactory.createStudent("mail@mail.com", "Member");
        assertEquals("mail@mail.com", member.getEmail());
    }

    @Test
    public void testTwoParameterManager() {
        GroupManager manager = UserFactory.createProfessor("mail@union.com", "Manager");
        assertEquals("mail@union.com", manager.getEmail());
    }

    @Test
    public void testThreeParameterMember() {
        GroupMember member = UserFactory.createStudent("Garrett", "mail@mail.com", "Member");
        assertEquals("Garrett", member.getDisplayName());
        assertEquals("mail@mail.com", member.getEmail());
    }

    @Test
    public void testThreeParameterManager() {
        GroupManager manager = UserFactory.createProfessor("Garrett", "mail@union.com", "Manager");
        assertEquals("Garrett", manager.getDisplayName());
        assertEquals("mail@union.com", manager.getEmail());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTwoParameterWrongRoleMember() {
        UserFactory.createStudent("mail@mail.com", "Manager");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTwoParameterWrongRoleManager() {
        UserFactory.createProfessor("mail@union.com", "Member");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testThreeParameterWrongRoleMember() {
        UserFactory.createStudent("Garrett", "mail@mail.com", "Manager");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testThreeParameterWrongRoleManager() {
        UserFactory.createProfessor("Garrett", "mail@union.com", "Member");
    }

    @Test
    public void testCreateStudentWithMockito() {
        GroupMember mockMember = Mockito.mock(GroupMember.class);
        when(mockMember.getEmail()).thenReturn("mail@mail.com");

        GroupMember member = UserFactory.createStudent("mail@mail.com", "Member");

        assertEquals(mockMember.getEmail(), member.getEmail());
        verify(mockMember, times(1)).getEmail();
    }

    @Test
    public void testCreateProfessorWithMockito() {
        GroupManager mockManager = Mockito.mock(GroupManager.class);
        when(mockManager.getEmail()).thenReturn("mail@union.com");

        GroupManager manager = UserFactory.createProfessor("mail@union.com", "Manager");

        assertEquals(mockManager.getEmail(), manager.getEmail());
        verify(mockManager, times(1)).getEmail();
    }
}
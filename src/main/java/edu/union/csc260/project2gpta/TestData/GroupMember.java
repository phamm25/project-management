package edu.union.csc260.project2gpta.TestData;

import edu.union.csc260.project2gpta.model.Sprint;

import edu.union.csc260.project2gpta.TestData.ListOfSprint;

import edu.union.csc260.project2gpta.model.User;
import javafx.collections.ObservableList;


public class GroupMember {
    public static User createTestUser() {
        ObservableList<Sprint> sprintList = ListOfSprint.createTestSprint();
        User user = edu.union.csc260.project2gpta.model.UserFactory.createStudent("phamm@union.edu","Student");

        for (Sprint s : sprintList){
            ((edu.union.csc260.project2gpta.model.GroupMember) user).addSprint(s);
        }

        return user;
    }
}

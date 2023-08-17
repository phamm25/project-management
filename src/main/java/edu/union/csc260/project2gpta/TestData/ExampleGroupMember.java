package edu.union.csc260.project2gpta.TestData;

import edu.union.csc260.project2gpta.model.Sprint;

import edu.union.csc260.project2gpta.model.User;
import edu.union.csc260.project2gpta.model.GroupMember;

import javafx.collections.ObservableList;


public class ExampleGroupMember {
    public static GroupMember createTestMember() {
        ObservableList<Sprint> sprintList = ListOfSprint.createTestSprint();
        User user = edu.union.csc260.project2gpta.model.UserFactory.createStudent("phamm@union.edu","Student");

        for (Sprint s : sprintList){
            ((GroupMember) user).addSprint(s);
        }

        return (GroupMember) user;
    }
}

package edu.union.csc260.project2gpta.TestData;

import edu.union.csc260.project2gpta.model.Sprint;

import edu.union.csc260.project2gpta.model.User;
import edu.union.csc260.project2gpta.model.GroupManager;
import javafx.collections.ObservableList;


public class ExampleGroupManager {
    public static GroupManager createTestManager() {
        ObservableList<Sprint> sprintList = ListOfSprint.createTestSprint();
        User user = edu.union.csc260.project2gpta.model.UserFactory.createProfessor("Prof","tenure@union.edu","Manager");

        for (Sprint s : sprintList){
            ((GroupManager) user).addSprint(s);
        }

        return (GroupManager) user;
    }
}

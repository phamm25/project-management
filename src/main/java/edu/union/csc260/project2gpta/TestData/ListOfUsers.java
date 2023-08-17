package edu.union.csc260.project2gpta.TestData;

import edu.union.csc260.project2gpta.model.Group;
import edu.union.csc260.project2gpta.model.GroupManager;
import edu.union.csc260.project2gpta.model.GroupMember;
import edu.union.csc260.project2gpta.model.Sprint;
import edu.union.csc260.project2gpta.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ListOfUsers {
    public static ObservableList<User> createUserList() {
        ObservableList<User> exampleUserList = FXCollections.observableArrayList();


        ObservableList<edu.union.csc260.project2gpta.model.Sprint> sprintList1 = ListOfSprint.createTestSprint();
        ObservableList<edu.union.csc260.project2gpta.model.Sprint> sprintList2 = ListOfSprint.createTestSprint();
        ObservableList<edu.union.csc260.project2gpta.model.Sprint> sprintList3 = ListOfSprint.createTestSprint();

        User user1 = edu.union.csc260.project2gpta.model.UserFactory.createStudent("Minh Pham","phamm@union.edu","Member");
        User user2 = edu.union.csc260.project2gpta.model.UserFactory.createStudent("Pam","phamm","Member");
        User user3 = edu.union.csc260.project2gpta.model.UserFactory.createProfessor("Kristina","kristina","Manager");
        User user4 = edu.union.csc260.project2gpta.model.UserFactory.createStudent("Pampam","phamm2","Member");

        Group group1 = new Group((GroupManager) user3, "CSC 497 - 1", "Groups for students with ML/NLP-related thesis.\nGroup has 3 members including professor.");
        Group group2 = new Group((GroupManager) user3, "CSC 497 - 2", "Groups for students with ML/NLP-related thesis.\nGroup has 2 members including professor.");
        Group group3 = new Group((GroupManager) user3, "CSC 497 - 3", "Groups for students with ML/NLP-related thesis.\nGroup has 1 members including professor.");

        group1.addMember((GroupMember) user1);
        group1.addMember((GroupMember) user2);

        group2.addMember((GroupMember) user4);


//        for (Sprint s : sprintList1){
//            ((edu.union.csc260.project2gpta.model.GroupMember) user1).addSprint(s);
//        }
        Sprint s = edu.union.csc260.project2gpta.TestData.Sprint.createTestSprint();
        ((edu.union.csc260.project2gpta.model.GroupMember) user1).addSprint(s);
        for (Sprint s1 : sprintList2){
            ((edu.union.csc260.project2gpta.model.GroupMember) user2).addSprint(s1);
        }
        for (Sprint s2 : sprintList3){
            ((edu.union.csc260.project2gpta.model.GroupManager) user3).addSprint(s2);
        }

        exampleUserList.add(user1);
        exampleUserList.add(user2);
        exampleUserList.add(user3);
        exampleUserList.add(user4);

        return exampleUserList;
    }
}

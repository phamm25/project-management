package edu.union.csc260.project2gpta.TestData;

import edu.union.csc260.project2gpta.model.GroupManager;
import edu.union.csc260.project2gpta.model.GroupMember;
import edu.union.csc260.project2gpta.model.User;
import edu.union.csc260.project2gpta.model.Group;


public class ExampleGroup {
    public static Group createTestGroup(){

        User user1 = edu.union.csc260.project2gpta.model.UserFactory.createStudent("Minh Pham","phamm@union.edu","Member");
        User user2 = edu.union.csc260.project2gpta.model.UserFactory.createStudent("Pam","phamm","Member");
        User user3 = edu.union.csc260.project2gpta.model.UserFactory.createProfessor("Kristina","kristina","Manager");

        Group testGroup = new Group((GroupManager)user3);
        testGroup.addMember((GroupMember)user1);
        testGroup.addMember((GroupMember)user2);


        return testGroup;
    }
}
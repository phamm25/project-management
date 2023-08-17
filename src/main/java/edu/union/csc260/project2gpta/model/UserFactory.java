package edu.union.csc260.project2gpta.model;

public class UserFactory {
    public static GroupMember createStudent(String email, String role) {
        if (role.equals("Member")){
            return new GroupMember(email, role);
        }else{
            throw new IllegalArgumentException("Invalid role for Student");
        }
    }

    public static GroupMember createStudent(String name,String email, String role) {
        if (role.equals("Member")){
            return new GroupMember(name, email, role);
        }else{
            throw new IllegalArgumentException("Invalid role for Student");
        }
    }

    public static GroupManager createProfessor(String email, String role) {
        if (role.equals("Manager")){
            return new GroupManager(email, role);
        }else{
            throw new IllegalArgumentException("Invalid role for Professor");
        }
    }

    public static GroupManager createProfessor(String name, String email, String role) {
        if (role.equals("Manager")){
            return new GroupManager(name, email, role);
        }else{
            throw new IllegalArgumentException("Invalid role for Professor");
        }
    }
}

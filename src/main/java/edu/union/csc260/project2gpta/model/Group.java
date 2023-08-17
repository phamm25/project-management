package edu.union.csc260.project2gpta.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

public class Group {
    private UUID groupID;
    private StringProperty groupName;

    private ListProperty<GroupMember> groupMember;

    private ObjectProperty<GroupManager> manager;

    private StringProperty description;

    public Group(GroupManager creator) {
        this.groupID = UUID.randomUUID();
        this.groupName = new SimpleStringProperty("New Group");
        this.manager = new SimpleObjectProperty<>(null);
        this.setManager(creator);
        this.description = new SimpleStringProperty("Some Description");
        this.groupMember = new SimpleListProperty<>(FXCollections.observableList(new ArrayList<>()));
//        this.manager.get().addGroup(this);  // add the group to creator
    }

    public Group(GroupManager creator, String groupName, String groupDescription) {
        this.groupID = UUID.randomUUID();
        this.groupName = new SimpleStringProperty(groupName);
        this.manager = new SimpleObjectProperty<>(null);
        this.setManager(creator);
        this.description = new SimpleStringProperty(groupDescription);
        this.groupMember = new SimpleListProperty<>(FXCollections.observableList(new ArrayList<>()));
    }

    public String getId() {
        return this.groupID.toString();
    }

    public void setId(String id) {
        this.groupID = UUID.fromString(id);
    }

    public GroupManager getManager() {
        return manager.get();
    }

    /**
     * Set manager for this group. Public method in case the group changes professor in the future.
     * @param manager
     */
    public void setManager(GroupManager manager) {
        for (Group g : manager.getGroupList()){
            if (g.getGroupName().equals(this.getGroupName())){
                throw new IllegalArgumentException("Group name already existed for the manager!");
            }
        }
        this.manager.set(manager);
        


        String url = "jdbc:mysql://127.0.0.1:3333/group3_db";
        String username = "singhala";
        String password = "Un!on2638391";
   
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String sqlUpdateManager = "UPDATE `groups` SET `groupmanagerid` = ? WHERE `groupid` = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdateManager);
            preparedStatement.setString(1, manager.getId());
            preparedStatement.setString(2, this.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        manager.addGroup(this);
        

    }

    public boolean contains(GroupMember member){
        return groupMember.get().contains(member);
    }

    public String getGroupName() {
        return groupName.get();
    }

    public void setGroupName(String name) {
        groupName.set(name);
        String url = "jdbc:mysql://127.0.0.1:3333/group3_db";
        String username = "singhala";
        String password = "Un!on2638391";
   
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String sqlUpdateName = "UPDATE `groups` SET `groupname` = ? WHERE `groupid` = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdateName);
            preparedStatement.setString(1, name);
             preparedStatement.setString(2, this.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public String getGroupDescription() {
        return description.get();
        
    }

    public void setGroupDescription(String desc) {
        description.set(desc);
        String url = "jdbc:mysql://127.0.0.1:3333/group3_db";
        String username = "singhala";
        String password = "Un!on2638391";
   
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String sqlUpdateDesc = "UPDATE `groups` SET `groupdesc` = ? WHERE `groupid` = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdateDesc);
            preparedStatement.setString(1, desc);
             preparedStatement.setString(2, this.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void addMember(GroupMember member) {
        groupMember.add(member);
        member.setGroup(this);
    
        String url = "jdbc:mysql://127.0.0.1:3333/group3_db";
        String username = "singhala";
        String password = "Un!on2638391";
    
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String sql2 = "SELECT * FROM users WHERE userid = ?";
            PreparedStatement statement2 = connection.prepareStatement(sql2);
            statement2.setString(1, member.getId());
            ResultSet resultSet = statement2.executeQuery();

            if (!resultSet.next()){
                String sql = "INSERT INTO users(userid, groupid, displayName, email, role) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(sql);

                statement.setString(1, member.getId());
                statement.setString(2, this.getId()); // Assuming a method getGroupId() exists for the group object
                statement.setString(3, member.getDisplayName());
                statement.setString(4, member.getEmail());
                statement.setString(5, member.getRole());
                statement.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void deleteMember(GroupMember member) {
        groupMember.remove(member);
        member.setGroup(null);
    
        String url = "jdbc:mysql://127.0.0.1:3333/group3_db";
        String username = "singhala";
        String password = "Un!on2638391";
        
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String sqlUpdateDesc = "UPDATE `users` SET `groupid` = NULL WHERE `userid` = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdateDesc);
            preparedStatement.setString(1, member.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }
    
    
    

    public ListProperty<GroupMember> getGroupMember(){
        return groupMember;
    }
}

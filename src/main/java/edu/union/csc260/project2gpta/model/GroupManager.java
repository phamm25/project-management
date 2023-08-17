package edu.union.csc260.project2gpta.model;
import java.sql.*;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.UUID;

public class GroupManager implements User {

    private UUID userID;
    private StringProperty displayName;
    private StringProperty email;

    private StringProperty role;

    private ListProperty<Group> groupList;

    private ListProperty<Sprint> sprintList;

    public GroupManager(String email, String role) {
        this.userID = UUID.randomUUID();
        this.email = new SimpleStringProperty(email);
        this.role = new SimpleStringProperty(role);
        this.displayName = new SimpleStringProperty("User");
        this.sprintList = new SimpleListProperty<>(FXCollections.observableList(new ArrayList<>()));
        this.groupList = new SimpleListProperty<>(FXCollections.observableList(new ArrayList<>()));
    }

    public GroupManager(String displayName, String email, String role) {
        this(email,role);
        this.setDisplayName(displayName);
    }

    public String getId() {
        return this.userID.toString();
    }

    public void setId(String id) {
        this.userID = UUID.fromString(id);
    }

    public String getDisplayName() {
        return displayName.get();
    }

    public void setDisplayName(String displayName) {
        this.displayName.set(displayName);
        String url = "jdbc:mysql://127.0.0.1:3333/group3_db";
            String username = "singhala";
            String password = "Un!on2638391";
   
            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                String sqlUpdatename = "UPDATE `users` SET `displayName` = ? WHERE `userid` = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdatename);
                preparedStatement.setString(1, displayName);
                preparedStatement.setString(2, this.getId());
                preparedStatement.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

    }
    public String getEmail() {
        return email.get();
    }

    public String getRole() {
        return role.get();
    }

    public void setRole(String role) {
        this.role.set(role);
        String url = "jdbc:mysql://127.0.0.1:3333/group3_db";
            String username = "singhala";
            String password = "Un!on2638391";
   
            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                String sqlUpdatename = "UPDATE `users` SET `role` = ? WHERE `userid` = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdatename);
                preparedStatement.setString(1, role);
                preparedStatement.setString(2, this.getId());
                preparedStatement.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
    }

    public void addGroup(Group group) {
        groupList.add(group);
    
        String url = "jdbc:mysql://127.0.0.1:3333/group3_db";
        String username = "singhala";
        String password = "Un!on2638391";
    
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            // SQL code to insert a new group into the "groups" table
            String insertGroup = "INSERT INTO `groups` (groupid, groupname, groupdesc, groupmanagerid) VALUES (?, ?, ? ,?)";
            PreparedStatement insertGroupStatement = connection.prepareStatement(insertGroup);
            insertGroupStatement.setString(1, group.getId());
            insertGroupStatement.setString(2, group.getGroupName());
            insertGroupStatement.setString(3, group.getGroupName());
            insertGroupStatement.setString(4, this.getId());

            insertGroupStatement.executeUpdate();
    
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    

    public void deleteGroup(Group group){
        groupList.remove(group);
    
        String url = "jdbc:mysql://127.0.0.1:3333/group3_db";
        String username = "singhala";
        String password = "Un!on2638391";
    
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            // SQL code to set all group ids to null where group id equals group.getId()
            String updateUsersGroupIds = "UPDATE users SET groupid = NULL WHERE groupid = ?";
            PreparedStatement updateUsersGroupIdsStatement = connection.prepareStatement(updateUsersGroupIds);
            updateUsersGroupIdsStatement.setString(1, group.getId());
            updateUsersGroupIdsStatement.executeUpdate();
            
            // SQL code to delete the group from "groups" table
            String deleteGroup = "DELETE FROM groups WHERE groupid = ?";
            PreparedStatement deleteGroupStatement = connection.prepareStatement(deleteGroup);
            deleteGroupStatement.setString(1, group.getId());
            deleteGroupStatement.executeUpdate();
    
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    

    public void addSprint(Sprint sprint) {
        sprintList.add(sprint);
    
        String url = "jdbc:mysql://127.0.0.1:3333/group3_db";
        String username = "singhala";
        String password = "Un!on2638391";
    
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String sql = "INSERT INTO sprints (sprintid, name, startdate, enddate, reflection,parentuserid) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            
            statement.setString(1, sprint.getSprintID());
            statement.setString(2, sprint.getSprintName());
            statement.setDate(3, java.sql.Date.valueOf(sprint.getStartDate()));
            statement.setDate(4, java.sql.Date.valueOf(sprint.getEndDate()));
            statement.setString(5, sprint.getReflection());
            statement.setString(56, this.getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    

    public void deleteSprint(Sprint sprint){
        sprintList.remove(sprint);
    
        String url = "jdbc:mysql://127.0.0.1:3333/group3_db";
        String username = "singhala";
        String password = "Un!on2638391";
    
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String deleteTasks = "DELETE tasks FROM tasks INNER JOIN backlogs ON tasks.backlogid = backlogs.backlogid WHERE backlogs.sprintid = ?";
            PreparedStatement deleteTasksStatement = connection.prepareStatement(deleteTasks);
            deleteTasksStatement.setString(1, sprint.getSprintID());
            deleteTasksStatement.executeUpdate();
    
            String deleteBacklogs = "DELETE FROM backlogs WHERE getSprintID = ?";
            PreparedStatement deleteBacklogsStatement = connection.prepareStatement(deleteBacklogs);
            deleteBacklogsStatement.setString(1, sprint.getSprintID());
            deleteBacklogsStatement.executeUpdate();
    
            String deleteSprint = "DELETE FROM sprints WHERE sprintid = ?";
            PreparedStatement deleteSprintStatement = connection.prepareStatement(deleteSprint);
            deleteSprintStatement.setString(1, sprint.getSprintID());
            deleteSprintStatement.executeUpdate();
    
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public ObservableList<Sprint> getSprintList(){
        return sprintList;
    }

    public ObservableList<Group> getGroupList(){
        return groupList;
    }
}

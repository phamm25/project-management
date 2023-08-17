package edu.union.csc260.project2gpta.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

import java.util.ArrayList;
import java.util.UUID;

public class GroupMember implements User {

    private UUID userID;
    private StringProperty displayName;
    private StringProperty email;

    private StringProperty role;

    private ListProperty<Sprint> sprintList;

    private ObjectProperty<Group> group;



//    public GroupMember(String email) {
//        this.displayName = SimpleStringProperty("Some name");
//        this.email = new SimpleStringProperty(email);
//        this.role = null;
//        this.sprintList = new SimpleListProperty<>(FXCollections.observableList(new ArrayList<>()));
//    }

    public GroupMember(String email, String role) {
        this.userID = UUID.randomUUID();
        this.displayName = new SimpleStringProperty("User");
        this.email = new SimpleStringProperty(email);
        this.role = new SimpleStringProperty(role);
        this.sprintList = new SimpleListProperty<>(FXCollections.observableList(new ArrayList<>()));
        this.group = new SimpleObjectProperty<>(null);
    }

    public GroupMember(String displayName, String email, String role) {
        this(email, role);
        this.setDisplayName(displayName);
    }

    public String getId() {
        return this.userID.toString();
    }

    public void setId(String id) {
        this.userID = UUID.fromString(id);
    }

    public Group getGroup() {
        return this.group.get();
    }

    public void setGroup(Group g) {
        String gid = null;
        if (g == null){
            gid = this.group.get().getId();
        }
        this.group.set(g);

        String url = "jdbc:mysql://127.0.0.1:3333/group3_db";
        String username = "singhala";
        String password = "Un!on2638391";
    
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String sql = "UPDATE `users` set groupid = ? WHERE userid = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            if (g != null){
                statement.setString(1, g.getId());
            }else{
                statement.setString(1, gid);
            }
            statement.setString(2, this.getId()); 
            statement.executeUpdate();
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
            String sql2 = "SELECT * FROM `sprints` WHERE sprintid = ?";
            PreparedStatement statement2 = connection.prepareStatement(sql2);
            statement2.setString(1, sprint.getSprintID());
            ResultSet resultSet = statement2.executeQuery();

            if (!resultSet.next()){
                String sql = "INSERT INTO sprints (sprintid, name, startdate, enddate, reflection,parentuserid) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(sql);

                statement.setString(1, sprint.getSprintID());
                statement.setString(2, sprint.getSprintName());
                statement.setDate(3, java.sql.Date.valueOf(sprint.getStartDate()));
                statement.setDate(4, java.sql.Date.valueOf(sprint.getEndDate()));
                statement.setString(5, sprint.getReflection());
                statement.setString(6, this.getId());
                statement.executeUpdate();
            }
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
    

    public String getDisplayName() {
        return displayName.get();
    }

    public StringProperty displayNameProperty() {
        return displayName;
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

    public StringProperty emailProperty() {
        return email;
    }

    public String getRole() {
        return role.get();
    }

    public StringProperty roleProperty() {
        return role;
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

    public ObservableList<Sprint> getSprintList(){
        return sprintList;
    }
}
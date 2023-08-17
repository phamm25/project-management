package edu.union.csc260.project2gpta.model;

import java.time.LocalDate;
import java.util.UUID;
import java.sql.*;

import javafx.beans.property.*;
import javafx.collections.FXCollections;


public class Task {
    private StringProperty taskName;
    private StringProperty taskStatus;  // Unassigned, To Do, In progress
    private StringProperty taskDescription;
//    private ObjectProperty<BacklogItem> backlogItem;
    private UUID taskID;

//    public Task(String taskName, String taskStatus, BacklogItem backlogItem) {
//        this.taskName = new SimpleStringProperty(taskName);
//        this.taskStatus = new SimpleStringProperty(taskStatus != null ? taskStatus : "To Do");
//        this.taskDescription = new SimpleStringProperty(""); // initialize taskDescription
//        this.backlogItem = new SimpleObjectProperty<>(backlogItem);
//    }

    public Task(String taskName) {
        this.taskName = new SimpleStringProperty(taskName);
        this.taskStatus = new SimpleStringProperty("Unassigned");
        this.taskDescription = new SimpleStringProperty("");
        this.taskID = UUID.randomUUID();
    }

    public Task(String name, String status, String description) {
        this(name);
        this.taskStatus = new SimpleStringProperty(status);
        this.taskDescription = new SimpleStringProperty(description);
        this.taskID = UUID.randomUUID();
    }

    public String getId() {
        return this.taskID.toString();
    }
    public String getTaskId(){
        return String.valueOf(taskID);
    }

    public void setId(String id) {
        this.taskID = UUID.fromString(id);}

    public void setTaskId(String id){
        this.taskID = UUID.fromString(id);
    }




    public String getTaskName() {
        return taskName.get();
    }

    public void setTaskName(String taskName) {
        this.taskName.set(taskName);
        String url = "jdbc:mysql://127.0.0.1:3333/group3_db";
            String username = "singhala";
            String password = "Un!on2638391";
   
            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                String sqlUpdateName = "UPDATE `tasks` SET `name` = ? WHERE `taskid` = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdateName);
                preparedStatement.setString(1, taskName);
                preparedStatement.setString(2, this.getTaskId());
                preparedStatement.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

    }

    public StringProperty taskNameProperty() {
		return taskName;
	}
    

    public StringProperty taskStatusProperty() {
        return taskStatus;
    }

    public String getTaskStatus() {
        return taskStatus.get();
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus.set(taskStatus);
        String url = "jdbc:mysql://127.0.0.1:3333/group3_db";
            String username = "singhala";
            String password = "Un!on2638391";
   
            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                String sqlUpdateName = "UPDATE `tasks` SET `status` = ? WHERE `taskid` = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdateName);
                preparedStatement.setString(1, taskStatus);
                preparedStatement.setString(2, this.getTaskId());
                preparedStatement.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
    }

    public StringProperty taskDescriptionProperty() {
        return taskDescription;
    }

    public String getTaskDescription() {
        return taskDescription.get();
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription.set(taskDescription);
        String url = "jdbc:mysql://127.0.0.1:3333/group3_db";
            String username = "singhala";
            String password = "Un!on2638391";
   
            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                String sqlUpdateName = "UPDATE `tasks` SET `description` = ? WHERE `taskid` = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdateName);
                preparedStatement.setString(1, taskDescription);
                preparedStatement.setString(2, this.getTaskId());
                preparedStatement.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
    }
}
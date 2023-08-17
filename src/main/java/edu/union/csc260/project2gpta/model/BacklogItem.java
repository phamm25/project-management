package edu.union.csc260.project2gpta.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;


public class BacklogItem {
    private StringProperty name;
    private StringProperty priority;
    private ListProperty<Task> tasks;
    private ObjectProperty<LocalDate> startDate;
    private ObjectProperty<LocalDate> endDate;
    private StringProperty backlogItemDescription;
    private UUID backlogID;

    public BacklogItem(String name, String priority, String backlogItemDescription, LocalDate startDate, LocalDate endDate) {
        this.name = new SimpleStringProperty(name);
        this.priority = new SimpleStringProperty(priority);
        this.tasks = new SimpleListProperty<>(FXCollections.observableList(new ArrayList<>()));
        this.startDate = new SimpleObjectProperty<>(startDate);
        this.endDate = new SimpleObjectProperty<>(endDate);
        this.backlogItemDescription = new SimpleStringProperty(backlogItemDescription);
        this.backlogID = UUID.randomUUID();
    }

    public String getBacklogId(){
        return String.valueOf(backlogID);
    }

    public void setBacklogId(String id){
        this.backlogID = UUID.fromString(id);
    }

    public StringProperty nameProperty() {
        return name;
    }


    public String getId() {
        return this.backlogID.toString();
    }

    public void setId(String id) {
        this.backlogID = UUID.fromString(id);
    }
    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
        String url = "jdbc:mysql://127.0.0.1:3333/group3_db";
            String username = "singhala";
            String password = "Un!on2638391";
   
            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                String sqlUpdateName = "UPDATE `backlogs` SET `name` = ? WHERE `backlogid` = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdateName);
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, this.getBacklogId());
                preparedStatement.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

    }

    public StringProperty priorityProperty() {
        return priority;
    }

    public String getPriority() {
        return priority.get();
    }

    public void setPriority(String priority) {
        this.priority.set(priority);
        String url = "jdbc:mysql://127.0.0.1:3333/group3_db";
        String username = "singhala";
        String password = "Un!on2638391";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String sqlUpdatePriority = "UPDATE `backlogs` SET `priority` = ? WHERE `backlogid` = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdatePriority);
            preparedStatement.setString(1, priority);
            preparedStatement.setString(2, this.getBacklogId());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    public StringProperty backlogItemDescriptionProperty() {
        return backlogItemDescription;
    }

    public ListProperty<Task> taskListProperty() {
        return tasks;
    }
    public String getBacklogItemDescription() {
        return backlogItemDescription.get();
    }

    public void setBacklogItemDescription(String backlogItemDescription) {
        this.backlogItemDescription.set(backlogItemDescription);
        String url = "jdbc:mysql://127.0.0.1:3333/group3_db";
        String username = "singhala";
        String password = "Un!on2638391";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String sqlUpdateDesc = "UPDATE `backlogs` SET `backlogItemDescription` = ? WHERE `backlogid` = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdateDesc);
            preparedStatement.setString(1, backlogItemDescription);
            preparedStatement.setString(2, this.getBacklogId());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
    }

    public ObservableList<Task> getTasks() {
        return tasks.get();
    }

    public void setTasks(List<Task> tasks) {
        this.tasks.setAll(tasks);
    
        String url = "jdbc:mysql://127.0.0.1:3333/group3_db";
        String username = "singhala";
        String password = "Un!on2638391";
    
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            for(Task task : tasks){
                String taskId = task.getTaskId();
                String status = task.getTaskStatus();
                String description = task.getTaskDescription();
                String sql = "UPDATE tasks SET status = ?, description = ? WHERE taskid = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, status);
                statement.setString(2, description);
                statement.setString(3, taskId);
    
                statement.executeUpdate();
                
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    

    public void addTask(Task task) {
        tasks.add(task);
    
        String url = "jdbc:mysql://127.0.0.1:3333/group3_db";
        String username = "singhala";
        String password = "Un!on2638391";
    
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String taskId = task.getTaskId();
            String status = task.getTaskStatus();
            String description = task.getTaskDescription();
            String name = task.getTaskName();
            String backlogId = this.getBacklogId();

            String sql2 = "SELECT * FROM tasks WHERE taskid = ?";
            PreparedStatement statement2 = connection.prepareStatement(sql2);
            statement2.setString(1, taskId);
            ResultSet resultSet = statement2.executeQuery();


            if (!resultSet.next()){
                String sql = "INSERT INTO tasks (taskid, name, status, description, parentbacklogid) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, taskId);
                statement.setString(2, name);
                statement.setString(3, status);
                statement.setString(4, description);
                statement.setString(5, backlogId);
                statement.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    

    public void removeTask(Task task) {
        tasks.remove(task);
    
        String url = "jdbc:mysql://127.0.0.1:3333/group3_db";
        String username = "singhala";
        String password = "Un!on2638391";
    
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String taskId = task.getTaskId();
            String sql = "DELETE FROM tasks WHERE taskid = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, taskId);
            statement.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void editTask(Task taskToEdit, Task updatedTask) {
        removeTask(taskToEdit);
        addTask(updatedTask);
    }

    public double getProgress() {
        int totalTasks = tasks.size();
        if (totalTasks == 0) {
            return 0;
        }

        int completedTasks = 0;
        for (Task task : tasks) {
            if ("Done".equals(task.getTaskStatus())) {
                completedTasks++;
            }
        }

        return (double) completedTasks / totalTasks;
    }

    public LocalDate getStartDate() {
        return startDate.get();
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate.set(startDate);
            String url = "jdbc:mysql://127.0.0.1:3333/group3_db";
            String username = "singhala";
            String password = "Un!on2638391";
   
            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                String sqlUpdateReflection = "UPDATE `backlogs` SET `startDate` = ? WHERE `backlogid` = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdateReflection);
                preparedStatement.setDate(1, java.sql.Date.valueOf(startDate));
                preparedStatement.setString(2, this.getBacklogId());
                preparedStatement.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
    }

    public LocalDate getEndDate() {
        return endDate.get();
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate.set(endDate);
            String url = "jdbc:mysql://127.0.0.1:3333/group3_db";
            String username = "singhala";
            String password = "Un!on2638391";
   
            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                String sqlUpdateReflection = "UPDATE `backlogs` SET `endDate` = ? WHERE `backlogid` = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdateReflection);
                preparedStatement.setDate(1, java.sql.Date.valueOf(endDate));
                preparedStatement.setString(2, this.getBacklogId());
                preparedStatement.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
    }

    public boolean contains(Task task){
        return this.tasks.contains(task);
    }

    public static boolean isPriorityValid(String prio){
        return (prio.equals("Low") || prio.equals("Medium") || prio.equals("High"));
    }

    public enum Priority {
        LOW,
        MEDIUM,
        HIGH
    }
}
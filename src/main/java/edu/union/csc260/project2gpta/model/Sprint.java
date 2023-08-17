package edu.union.csc260.project2gpta.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.sql.*;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;

import edu.union.csc260.project2gpta.model.BacklogItem;
import javafx.scene.control.Alert;


public class Sprint {
    private final StringProperty sprintName;

    private UUID sprintID;

    private final ListProperty<BacklogItem> sprintBackLog;
    private final ObjectProperty<LocalDate> startDate;
    private final ObjectProperty<LocalDate> endDate;

    private StringProperty sprintReflection;

    public Sprint(String name) {
        this.sprintName = new SimpleStringProperty(name);
        this.sprintReflection = new SimpleStringProperty("");
        this.sprintBackLog = new SimpleListProperty<>(FXCollections.observableList(new ArrayList<>()));
        this.startDate = new SimpleObjectProperty<>();
        this.endDate = new SimpleObjectProperty<>();
        this.sprintID = UUID.randomUUID();
    }
    
    public Sprint(String name, LocalDate startDate, LocalDate endDate) {
        this(name);
        this.setStartDate(startDate);
        this.setEndDate(endDate);
    }

   public String getSprintID(){return String.valueOf(sprintID);}

   public void setSprintID(String id){
       sprintID = UUID.fromString(id);
   }
    public String getId() {
        return this.sprintID.toString();
    }
    public void setSprintName(String name) {
        this.sprintName.set(name);
        String url = "jdbc:mysql://127.0.0.1:3333/group3_db";
            String username = "singhala";
            String password = "Un!on2638391";
   
            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                String sqlUpdateReflection = "UPDATE `sprints` SET `name` = ? WHERE `sprintid` = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdateReflection);
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, this.getSprintID());
                preparedStatement.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

    }
    public void setId(String id) {
        this.sprintID = UUID.fromString(id);
    }
    public String getSprintName() {
        return sprintName.get();
    }

    public void setReflection(String reflection) {
        double sprintProgress = getSprintProgress();
        if (sprintProgress == 1){
            this.sprintReflection.set(reflection);
            String url = "jdbc:mysql://127.0.0.1:3333/group3_db";
            String username = "singhala";
            String password = "Un!on2638391";
   
            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                String sqlUpdateReflection = "UPDATE `sprints` SET `reflection` = ? WHERE `sprintid` = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdateReflection);
                preparedStatement.setString(1, reflection);
                preparedStatement.setString(2, this.getSprintID());
                preparedStatement.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Sprint Incomplete");
            alert.setHeaderText("Sprint Is Not Completed");
            alert.setContentText("Sprint needs to be completed and has at least one task. Please finish all tasks in sprint, then add reflection");
            alert.showAndWait();
        }
    }

    public StringProperty sprintReflectionProperty() {
        return sprintName;
    }

    public String getReflection() {
        return sprintReflection.get();
    }

    public StringProperty sprintNameProperty() {
        return sprintName;
    }

    public ObjectProperty<LocalDate> startDateProperty() {
        return startDate;
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
                String sqlUpdateReflection = "UPDATE `sprints` SET `startdate` = ? WHERE `sprintid` = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdateReflection);
                preparedStatement.setDate(1, java.sql.Date.valueOf(startDate));
                preparedStatement.setString(2, this.getSprintID());
                preparedStatement.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
    }

    public ObjectProperty<LocalDate> endDateProperty() {
        return endDate;
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
            String sqlUpdateReflection = "UPDATE `sprints` SET `enddate` = ? WHERE `sprintid` = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdateReflection);
                preparedStatement.setDate(1, java.sql.Date.valueOf(endDate));
                preparedStatement.setString(2, this.getSprintID());
                preparedStatement.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
    }

    public int getTotalBacklogItems(){
        return sprintBackLog.size();
    }

    /**
     * Get progress of sprint. Progress = average(sprint's backlog items' progress)
     * @return double as progress
     */
    public double getSprintProgress(){
        if (getTotalBacklogItems() == 0) {
            return 0;
        }else{
            double totalProgress = 0;
            for (BacklogItem item : sprintBackLog){
                totalProgress += item.getProgress();
            }
            return totalProgress/getTotalBacklogItems();
        }
    }

    public ListProperty<BacklogItem> getAllBacklogItems(){
        return sprintBackLog;
    }

    public void addBacklog(BacklogItem item) {
        sprintBackLog.add(item);
    
        String url = "jdbc:mysql://127.0.0.1:3333/group3_db";
        String username = "singhala";
        String password = "Un!on2638391";
    
        try (Connection connection = DriverManager.getConnection(url, username, password)) {

            String sql2 = "SELECT * FROM backlogs WHERE backlogid = ?";
            PreparedStatement statement2 = connection.prepareStatement(sql2);
            statement2.setString(1, item.getBacklogId());
            ResultSet resultSet = statement2.executeQuery();

            if (!resultSet.next()){
                String sql = "INSERT INTO backlogs (backlogid, name, priority, backlogItemDescription, startDate, endDate, parentsprintid) VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(sql);

                statement.setString(1, item.getBacklogId());
                statement.setString(2, item.getName());
                statement.setString(3, item.getPriority());
                statement.setString(4, item.getBacklogItemDescription());
                statement.setDate(5,java.sql.Date.valueOf(item.getStartDate()));
                statement.setDate(6,java.sql.Date.valueOf(item.getEndDate()));
                statement.setString(7, this.getSprintID());

                statement.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    

    public void deleteBacklog(BacklogItem item) {
        sprintBackLog.remove(item);
    
        String url = "jdbc:mysql://127.0.0.1:3333/group3_db";
        String username = "singhala";
        String password = "Un!on2638391";
    
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String sql = "DELETE FROM backlogs WHERE backlogid = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
    
            statement.setString(1, item.getBacklogId());
    
            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }}
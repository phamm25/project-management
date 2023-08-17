package edu.union.csc260.project2gpta.controller;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import edu.union.csc260.project2gpta.MainApp;
import edu.union.csc260.project2gpta.model.Sprint;
import edu.union.csc260.project2gpta.model.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class SprintEditController {

    private Sprint sprint; // the task being edited

    @FXML
    private TextField sprintNameField;

    @FXML
    private TextField startDateField;

    @FXML
    private TextField endDateField;

    @FXML
    private TextArea reflectionArea;

    // Reference to the main application.
    private MainApp mainApp;

    public SprintEditController() {
    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void initialize() {
        
    }

    private void updateFieldsFromSprint() {
        

        if (this.sprint != null) {
            // Fill the labels with info from the person object.
            sprintNameField.setText(sprint.getSprintName());
            reflectionArea.setText(sprint.getReflection());

            // Check if the start date is null and set a placeholder date if it is
            if (sprint.getStartDate() == null) {
                startDateField.setText("01.01.0001");
            } else {
                startDateField.setText(sprint.getStartDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
            }

            // Check if the end date is null and set a placeholder date if it is
            if (sprint.getEndDate() == null) {
                endDateField.setText("01.01.0001");
            } else {
                endDateField.setText(sprint.getEndDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
            }

        } else {
            sprintNameField.setText("Sprint X");
            startDateField.setText("dd.mm.yyyy");
            endDateField.setText("dd.mm.yyyy");
            reflectionArea.setText("");
        
        }
    }


    @FXML
    private void saveSprint() {
        // Get values from input fields
        String sprintName = sprintNameField.getText();
        String sprintReflection = reflectionArea.getText();
        if (sprintName.equals("")){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Empty value");
            alert.setHeaderText("No sprint name");
            alert.setContentText("Please name the sprint!");
            alert.showAndWait();
        }else{
            try{
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                LocalDate sprintStart = LocalDate.parse(startDateField.getText(), formatter);
                LocalDate sprintEnd = LocalDate.parse(endDateField.getText(), formatter);

                if (sprintEnd.isBefore(sprintStart)){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.initOwner(mainApp.getPrimaryStage());
                    alert.setTitle("Invalid input");
                    alert.setHeaderText("Date invalid");
                    alert.setContentText("Please enter end date >= start date");
                    alert.showAndWait();
                }
                else{
                    // Update sprint object
                    if (sprint!=null){
                        sprint.sprintNameProperty().set(sprintName);;
                        sprint.setStartDate(sprintStart);
                        sprint.setEndDate(sprintEnd);
                        sprint.setReflection(sprintReflection);}
                    else{
                        if (!sprintReflection.equals("")){
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.initOwner(mainApp.getPrimaryStage());
                            alert.setTitle("Invalid input");
                            alert.setHeaderText("Sprint reflection invalid");
                            alert.setContentText("Sprint reflection is not allowed at creation");
                            alert.showAndWait();
                        }else{
                            Sprint newSprint = new Sprint(sprintName, sprintStart, sprintEnd);
                            mainApp.addSprintData(newSprint);

                            // Close the window
                            Stage stage = (Stage) sprintNameField.getScene().getWindow();
                            stage.close();
                        }
                    }
                }
            }catch(java.lang.RuntimeException error){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.initOwner(mainApp.getPrimaryStage());
                alert.setTitle("Invalid input");
                alert.setHeaderText("Sprint date format invalid");
                alert.setContentText("Sprint date format is invalid. Please fill date in dd.MM.yyyy format!");
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void cancel() {
        // Close the window
        Stage stage = (Stage) sprintNameField.getScene().getWindow();
        stage.close();
    }

    public void setSprint(Sprint sprint) {
        this.sprint = sprint;
        updateFieldsFromSprint();
    }
}
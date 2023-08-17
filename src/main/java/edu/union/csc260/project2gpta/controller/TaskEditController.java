package edu.union.csc260.project2gpta.controller;

import edu.union.csc260.project2gpta.MainApp;
import edu.union.csc260.project2gpta.model.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class TaskEditController {

    private Task task; // the task being edited

    @FXML
    private TextField taskNameField;

    @FXML
    private TextArea taskDescriptionField;

    @FXML
    private Label backlogItemNameLabel;

    @FXML
    private RadioButton unassignedStatus;

    @FXML
    private RadioButton toDoStatus;

    @FXML
    private RadioButton inProgressStatus;

    @FXML
    private RadioButton toTestStatus;

    @FXML
    private RadioButton doneStatus;

    private ToggleGroup statusToggleGroup;

    // Reference to the main application.
    private MainApp mainApp;

    private MainDashboardController dashboardController;

    private BacklogItemOverviewController backlogController;

    private String selectedTaskStatus;


    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void setMainDashboardController(MainDashboardController controller) {
        this.dashboardController = controller;
    }

    public void setBacklogController(BacklogItemOverviewController controller){
        this.backlogController = controller;
    }

    public void initialize() {
        statusToggleGroup = new ToggleGroup();
        unassignedStatus.setToggleGroup(statusToggleGroup);
        toDoStatus.setToggleGroup(statusToggleGroup);
        inProgressStatus.setToggleGroup(statusToggleGroup);
        toTestStatus.setToggleGroup(statusToggleGroup);
        doneStatus.setToggleGroup(statusToggleGroup);

        statusToggleGroup.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
            RadioButton selectedStatus = (RadioButton) newToggle;
            selectedTaskStatus = selectedStatus.getText();
//            task.setTaskStatus(selectedStatus.getText());
        });
    }

    public void setTask(Task task) {
        this.task = task;
        showTaskDetails(task);
    }

    private void showTaskDetails(Task task){
        if (task != null){
            taskNameField.setText(task.getTaskName());
            taskDescriptionField.setText(task.getTaskDescription());

            // Update the selected status RadioButton
            switch (task.getTaskStatus()) {
                case "Unassigned" -> statusToggleGroup.selectToggle(unassignedStatus);
                case "To Do" -> statusToggleGroup.selectToggle(toDoStatus);
                case "In Progress" -> statusToggleGroup.selectToggle(inProgressStatus);
                case "To Test" -> statusToggleGroup.selectToggle(toTestStatus);
                case "Done" -> statusToggleGroup.selectToggle(doneStatus);
            }
        }else{
            this.task = new Task("<Example Name>", "Unassigned", "<Example Description>");
            taskNameField.setText(this.task.getTaskName());
            taskDescriptionField.setText(this.task.getTaskDescription());
            statusToggleGroup.selectToggle(unassignedStatus);
        }
    }

//    private void updateFieldsFromTask() {
//        if (task != null){
//            taskNameField.setText(task.getTaskName());
//            taskDescriptionField.setText(task.getTaskDescription());
//
//            // Update the selected status RadioButton
//            switch (task.getTaskStatus()) {
//                case "Unassigned" -> statusToggleGroup.selectToggle(unassignedStatus);
//                case "To Do" -> statusToggleGroup.selectToggle(toDoStatus);
//                case "In Progress" -> statusToggleGroup.selectToggle(inProgressStatus);
//                case "To Test" -> statusToggleGroup.selectToggle(toTestStatus);
//                case "Done" -> statusToggleGroup.selectToggle(doneStatus);
//            }
//        }else{
//
//        }
//    }

    public void setBacklogItemName(String backlogItemName) {
        backlogItemNameLabel.setText(backlogItemName);
    }

    @FXML
    private void saveTask() {
        if (mainApp.isEditAuthorized()){
            dashboardController.setCurrentBacklog(dashboardController.findBacklogByTask(task));

            // Get values from input fields
            String taskName = taskNameField.getText();
            String taskDescription = taskDescriptionField.getText();

            // Update task object
            task.setTaskName(taskName);
            task.setTaskDescription(taskDescription);
            task.setTaskStatus(selectedTaskStatus);

            // Update task to correct column:
            dashboardController.addTaskToBacklogItem(this.task);

            // Close the window
            Stage stage = (Stage) taskNameField.getScene().getWindow();
            stage.close();

            // if opened from backlogEdit, not from mainDashboard
            if (backlogController!=null) {
                backlogController.taskListView.refresh();
            }
        }
    }

    @FXML
    private void cancel() {
        // Close the window
        Stage stage = (Stage) taskNameField.getScene().getWindow();
        stage.close();
    }
}
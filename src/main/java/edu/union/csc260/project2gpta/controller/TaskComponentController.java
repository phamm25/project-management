package edu.union.csc260.project2gpta.controller;

import edu.union.csc260.project2gpta.MainApp;
import edu.union.csc260.project2gpta.model.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class TaskComponentController {

    @FXML
    private Label taskNameLabel;

    @FXML
    private Label taskStatusLabel;

    @FXML
    private VBox taskBox;

    private MainApp mainApp;

    private MainDashboardController dashboardController;
    private Task task; // Add field to hold Task object

    public TaskComponentController() {
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void setDashboardController(MainDashboardController controller) {
        this.dashboardController = controller;
    }

    public void initialize() {
        taskBox.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                mainApp.showTaskEditPopUp(task, dashboardController,null); // Pass the task object to the method
            }
        });
    }

    public void setTask(Task task) {
        this.task = task; // Set the task field
        taskNameLabel.setText(task.getTaskName());
        taskStatusLabel.setText(task.getTaskStatus());

        // Remove old status style class if exists
        taskBox.getStyleClass().removeIf(s -> s.startsWith("task-"));
        // Add new status style class
        switch (task.getTaskStatus()) {
            case "To Do" -> taskBox.getStyleClass().add("task-toDo");
            case "In Progress" -> taskBox.getStyleClass().add("task-inProgress");
            case "To Test" -> taskBox.getStyleClass().add("task-toTest");
            case "Done" -> taskBox.getStyleClass().add("task-done");
            default -> {
            }
        }
    }
}
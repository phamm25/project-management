package edu.union.csc260.project2gpta.controller;

import edu.union.csc260.project2gpta.MainApp;
import edu.union.csc260.project2gpta.model.Sprint;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.ListProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import edu.union.csc260.project2gpta.MainApp;
import edu.union.csc260.project2gpta.model.BacklogItem;
import edu.union.csc260.project2gpta.model.Task;

public class BacklogItemOverviewController {
    @FXML
    private TextArea nameLabel;
    @FXML
    private TextField priorityLabel;
    @FXML
    private TextField startDateLabel;
    @FXML
    private TextField progressLabel;
    @FXML
    private TextField endDateLabel;
    @FXML
    private TextArea descriptionLabel;
    @FXML
    public ListView<Task> taskListView;

    private MainApp mainApp;
    private ObservableList<Task> taskList;
    private BacklogItem backlogItem;

    private MainDashboardController dashboardController;


    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public BacklogItemOverviewController(){
        this.backlogItem = new BacklogItem(null, null, null, null, null);
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        // Initialize the task table columns
        setListViews();
        taskListView.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Task item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getTaskName());
                }
            }
        });
        backlogItem.taskListProperty().addListener(
            (observable, oldValue, newValue) -> setListViews());

        // handler:
        taskListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Task selectedTask = taskListView.getSelectionModel().getSelectedItem();
                if (selectedTask != null) {
                    this.mainApp.showTaskEditPopUp(selectedTask, this.dashboardController, this);
                }
            }
        });
    }


    public void setBacklogItem(BacklogItem backlogItem) {
        showBacklogItemDetails(backlogItem);
    }

    @FXML
    private void handleAdd() {
        if (mainApp.isEditAuthorized()){
            this.mainApp.showTaskEditPopUp(null, this.dashboardController, this);
        }
    }

    @FXML
    private void handleDelete() {
        if (mainApp.isEditAuthorized()){
            int selectedIndex =  taskListView.getSelectionModel().getSelectedIndex();
            Task item = taskListView.getSelectionModel().getSelectedItem();

            if (selectedIndex >= 0) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.initOwner(mainApp.getPrimaryStage());
                alert.setTitle("Confirmation");
                alert.setHeaderText("Delete Task");
                alert.setContentText("Are you sure you want to delete this task?");

                ButtonType yesButton = new ButtonType("Yes");
                ButtonType noButton = new ButtonType("No");

                alert.getButtonTypes().setAll(yesButton, noButton);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == yesButton) {
                    this.backlogItem.removeTask(item);
                    dashboardController.removeTaskFromAllColumn(item);
                    setListViews();
                    showBacklogItemDetails(backlogItem);
                }
            } else {
                // Nothing selected.
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.initOwner(mainApp.getPrimaryStage());
                alert.setTitle("No Selection");
                alert.setHeaderText("No Task Selected");
                alert.setContentText("Please select a task in the list.");
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void handleSave() {
        if (mainApp.isEditAuthorized()){
            // Get values from input fields
            String backlogName = nameLabel.getText();
            String backlogDescription = descriptionLabel.getText();
            String backlogPriority = priorityLabel.getText();

            if (backlogName.equals("")) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.initOwner(mainApp.getPrimaryStage());
                alert.setTitle("Empty value");
                alert.setHeaderText("No backlog name");
                alert.setContentText("Please name the backlog!");
                alert.showAndWait();
            } else if (!BacklogItem.isPriorityValid(backlogPriority)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.initOwner(mainApp.getPrimaryStage());
                alert.setTitle("Invalid input");
                alert.setHeaderText("Priority invalid");
                alert.setContentText("Please enter 'Low', 'Medium' or 'High' for priority!");
                alert.showAndWait();
            } else {
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

                    LocalDate start = LocalDate.parse(startDateLabel.getText(), formatter);
                    LocalDate end = LocalDate.parse(endDateLabel.getText(), formatter);

                    if (end.isBefore(start)){
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.initOwner(mainApp.getPrimaryStage());
                        alert.setTitle("Invalid input");
                        alert.setHeaderText("Date invalid");
                        alert.setContentText("Please enter end date >= start date");
                        alert.showAndWait();
                    }else{
                        if (backlogItem != null) {
                            this.backlogItem.setName(backlogName);
                            this.backlogItem.setBacklogItemDescription(backlogDescription);
                            this.backlogItem.setPriority(backlogPriority);
                            this.backlogItem.setStartDate(start);
                            this.backlogItem.setEndDate(end);
                            dashboardController.addBacklogToCurrentSprint(backlogItem);
                        }
                        // Close the window
                        Stage stage = (Stage) nameLabel.getScene().getWindow();
                        stage.close();
                        dashboardController.backlogItemsListView.refresh();
                    }
                } catch (java.lang.RuntimeException error) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.initOwner(mainApp.getPrimaryStage());
                    alert.setTitle("Invalid input");
                    alert.setHeaderText("Backlog date format invalid");
                    alert.setContentText("Backlog date format is invalid. Please fill date in dd.MM.yyyy format!");
                    alert.showAndWait();
                }
            }
        }
    }


    /**
     * Fills all text fields to show details about the sprint.
     * If the specified sprint is null, all text fields are cleared.
     *
     * @param backlogItem the sprint or null
     */
    private void showBacklogItemDetails(BacklogItem backlogItem) {
        if (backlogItem != null) {
            // Fill the labels with info from the person object.
            this.backlogItem = backlogItem;
            nameLabel.setText(backlogItem.getName());
            priorityLabel.setText(backlogItem.getPriority());
            String progress = String.valueOf(backlogItem.getProgress()*100)+"%";
            progressLabel.setText(progress);

            // Check if the start date is null and set a placeholder date if it is
            if (backlogItem.getStartDate() == null) {
                startDateLabel.setText("01.01.0001");
            } else {
                startDateLabel.setText(backlogItem.getStartDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
            }

            // Check if the end date is null and set a placeholder date if it is
            if (backlogItem.getEndDate() == null) {
                endDateLabel.setText("01.01.0001");
            } else {
                endDateLabel.setText(backlogItem.getEndDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
            }

            descriptionLabel.setText(backlogItem.getBacklogItemDescription());
            taskList.addAll(backlogItem.getTasks());
        } else {
            nameLabel.setText("BacklogItem");
            priorityLabel.setText("Low");
            progressLabel.setText("0.0");
            startDateLabel.setText("dd.MM.yyyy");
            endDateLabel.setText("dd.MM.yyyy");
            descriptionLabel.setText("<Description Empty>");
        
        }
    }
    /**
     * Set items of list views to list of tasks and backlog items
     */
    private void setListViews(){
        // Initialize the ObservableLists
        taskList = FXCollections.observableArrayList();

        // Set the ObservableLists to the ListViews
        taskListView.setItems(taskList);
       
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

    }

    public void setDashboardController(MainDashboardController controller) {
        this.dashboardController = controller;

    }
}

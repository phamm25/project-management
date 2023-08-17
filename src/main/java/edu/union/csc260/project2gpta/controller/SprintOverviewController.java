package edu.union.csc260.project2gpta.controller;
import edu.union.csc260.project2gpta.MainApp;
import edu.union.csc260.project2gpta.model.Sprint;


import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.util.Duration;




public class SprintOverviewController {
    @FXML
    private ListView<Sprint> sprintListView;

    @FXML
    private Label currentSprintTotalBacklogItems;

    @FXML
    private Label sprintStatus;

    @FXML
    private ProgressBar currentSprintProgress;

    @FXML
    private Label userSprintOverview;

    // Reference to the main application.
    private MainApp mainApp;

    // timeline handle for progress bar animation
    Timeline timeline;


    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public SprintOverviewController(){
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
         // Initialize the sprint list with names.
        sprintListView.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Sprint item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getSprintName());
                }
            }
        });

        // Clear person details.
        showSprintDetails(null);

        // Listen for selection changes and show the person details when changed.
        sprintListView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showSprintDetails(newValue));
        sprintListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Sprint selectedSprint = sprintListView.getSelectionModel().getSelectedItem();
                if (selectedSprint != null) {
                    this.mainApp.showMainDashboard(selectedSprint);
                }
            }
        });
    }

    /**
     * Fills all text fields to show details about the sprint.
     * If the specified sprint is null, all text fields are cleared.
     *
     * @param sprint the sprint or null
     */
    private void showSprintDetails(Sprint sprint) {
        if (sprint != null) {
            // Fill the labels with info from the person object.
            currentSprintTotalBacklogItems.setText(Integer.toString(sprint.getTotalBacklogItems()));
            sprintStatus.setText("Total Backlog Items");

            // Animation for progress bar
            timeline.stop();
            timeline = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(currentSprintProgress.progressProperty(), currentSprintProgress.getProgress())),
                    new KeyFrame(Duration.seconds(1), new KeyValue(currentSprintProgress.progressProperty(), sprint.getSprintProgress()))
            );
            timeline.play();


        } else {
            // sprint is null, 0 for everything.
            currentSprintTotalBacklogItems.setText("");
            sprintStatus.setText("Please select a sprint!");

            // Animation for progress bar
            timeline = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(currentSprintProgress.progressProperty(), 0)),
                    new KeyFrame(Duration.seconds(1), new KeyValue(currentSprintProgress.progressProperty(), 1))
            );
            timeline.setAutoReverse(true);
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        }
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        userSprintOverview.setText(mainApp.getCurrentUser().getDisplayName() + "'s Sprints");

        // Add observable list data to the table
        sprintListView.setItems(mainApp.getSprintData());
    }

    public void showAddSprint(){
        if (mainApp.isEditAuthorized()){
            this.mainApp.showSprintEditPopUp(null);
        }
    }
}

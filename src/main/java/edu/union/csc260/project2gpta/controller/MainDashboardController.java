package edu.union.csc260.project2gpta.controller;

import edu.union.csc260.project2gpta.model.BacklogItem;
import edu.union.csc260.project2gpta.model.Sprint;
import edu.union.csc260.project2gpta.utils.Util;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import edu.union.csc260.project2gpta.MainApp;
import edu.union.csc260.project2gpta.model.Task;

import java.util.Optional;

/**
 * Controller for the component that shows the list of addresses on one side and provides details
 * for a selected address on the other.
 *
 * @author Marco Jakob
 */
public class MainDashboardController {

    @FXML
    public ListView<BacklogItem> backlogItemsListView;
    @FXML
    private ListView<Task> toDoListView;
    @FXML
    private ListView<Task> inProgressListView;
    @FXML
    private ListView<Task> toTestListView;
    @FXML
    private ListView<Task> doneListView;

    private ObservableList<BacklogItem> backlogItemsList;
    private ObservableList<Task> toDoList;
    private ObservableList<Task> inProgressList;
    private ObservableList<Task> toTestList;
    private ObservableList<Task> doneList;

    private ObservableList<BacklogItem> lowPriorityBacklog;
    private ObservableList<BacklogItem> mediumPriorityBacklog;
    private ObservableList<BacklogItem> highPriorityBacklog;

    // ComboBox to select a sprint
    @FXML
    private ComboBox<Sprint> sprintComboBox;

    // The list of sprints
    private ObservableList<Sprint> sprintData;

    // Reference to the main application.
    private MainApp mainApp;

    private Util util;

    // current sprint use my this dashboard
    private Sprint currentSprint;

    private BacklogItem currentBacklog;

    /**
     * Is called by the showDashBoard method in mainApp to set mainApp for current Dashboard
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        this.util = mainApp.util;
        this.util.setDashboardController(this);

        // Init sprint Data and DropDown
        sprintData = mainApp.getSprintData();
        sprintComboBox.setItems(sprintData);

        // DropDown listener
        sprintComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setCurrentSprint(newValue);
            }
        });
    }

    /**
     * Is called by the showDashBoard method in mainApp to set Sprint for current Dashboard
     *
     * @param sprint
     */
    public void setCurrentSprint(Sprint sprint){
        this.currentSprint = sprint;
        setListViews();
        reorderBacklogItemsListView();
        fillStatusColumns();
    }

    /**
     * Retrieves items from sprint's backlog item and sorts them by priority
     */
    private void reorderBacklogItemsListView(){
        // add backlog Items to backlog columns
        if (currentSprint != null){
            for (BacklogItem backlogItem : currentSprint.getAllBacklogItems()){
                String priority = backlogItem.getPriority();
                switch (priority) {
                    case "Low" -> lowPriorityBacklog.add(backlogItem);
                    case "Medium" -> mediumPriorityBacklog.add(backlogItem);
                    case "High" -> highPriorityBacklog.add(backlogItem);
                    default -> throw new IllegalArgumentException("Priority level for Backlog Item invalid");
                }
            }
        }

        // Sort by Priority
        backlogItemsList.addAll(highPriorityBacklog);
        backlogItemsList.addAll(mediumPriorityBacklog);
        backlogItemsList.addAll(lowPriorityBacklog);
    }

    @FXML
    private void addBacklogItem() {
        if (mainApp.isEditAuthorized()){
            this.mainApp.showBacklogItemEdit(null, this);
        }
    }

    @FXML
    private void initialize() {
        // Initialize the backlog item list with names.
        backlogItemsListView.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(BacklogItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });


        backlogItemsListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                BacklogItem selectedItem = backlogItemsListView.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    // Todo: add param backlogItem to showBacklogItem
//                    setCurrentBacklog(selectedItem);
                    this.mainApp.showBacklogItemEdit(selectedItem, this);
                }
            }
        });

        // Dropdown cells init
        sprintComboBox.setCellFactory(param -> new ListCell<Sprint>() {
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

        // parse sprintName
        sprintComboBox.setButtonCell(new ListCell<Sprint>() {
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

//        toDoListView.setOnMouseClicked(event -> {
//            if (event.getClickCount() == 2) {
////                BacklogItem selectedItem = backlogItemsListView.getSelectionModel().getSelectedItem();
//                Task selectedTask = toDoListView.getSelectionModel().getSelectedItem();
//                if (selectedTask != null) {
//                    // Todo: add param backlogItem to showBacklogItem
//                    BacklogItem selectedItem = findBacklogByTask(selectedTask);
//                    setCurrentBacklog(selectedItem);
//                    this.mainApp.showBacklogItemEdit(selectedItem, this);
//                }
//            }
//        });

        // Set custom cell factories for each ListView
        createTaskCellFactory(toDoListView);
        createTaskCellFactory(inProgressListView);
        createTaskCellFactory(toTestListView);
        createTaskCellFactory(doneListView);
    }


    /**
     * Set items of list views to list of tasks and backlog items
     */
    private void setListViews(){
        // Initialize the ObservableLists
        backlogItemsList = FXCollections.observableArrayList();
        toDoList = FXCollections.observableArrayList();
        inProgressList = FXCollections.observableArrayList();
        toTestList = FXCollections.observableArrayList();
        doneList = FXCollections.observableArrayList();

        lowPriorityBacklog = FXCollections.observableArrayList();
        mediumPriorityBacklog = FXCollections.observableArrayList();
        highPriorityBacklog = FXCollections.observableArrayList();

        // Set the ObservableLists to the ListViews
        backlogItemsListView.setItems(backlogItemsList);
        toDoListView.setItems(toDoList);
        inProgressListView.setItems(inProgressList);
        toTestListView.setItems(toTestList);
        doneListView.setItems(doneList);
    }

    private void createTaskCellFactory(ListView<Task> listView) {
        listView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Task task, boolean empty) {
                super.updateItem(task, empty);

                if (empty || task == null) {
                    setText(null);
                    setGraphic(null);
                } else {

                    VBox taskBox = util.createTaskComponent(task);
                    setGraphic(taskBox);
                }
            }
        });
    }

    /**
     * Assign the given task to corresponding status columns (To do, In Progress, etc)
     * @param task
     */
    public void updateTaskToStatusColumn(Task task){
        removeTaskFromAllColumn(task);
        String newStatus = task.getTaskStatus();
        switch (newStatus) {
            case "To Do" -> toDoList.add(task);
            case "In Progress" -> inProgressList.add(task);
            case "To Test" -> toTestList.add(task);
            case "Done" -> doneList.add(task);
        }
    }

    /**
     * Assign the tasks in sprint to corresponding status columns (To do, In Progress, etc)
     *
     * @param
     */
    private void fillStatusColumns(){
        for (BacklogItem backlog : this.currentSprint.getAllBacklogItems()){
            for (Task task : backlog.getTasks()){
                updateTaskToStatusColumn(task);
            }
        }
    }

    public void removeTaskFromAllColumn(Task task) {
        toDoList.remove(task);
        toTestList.remove(task);
        inProgressList.remove(task);
        doneList.remove(task);
    }

    private void deleteBacklogItemsFromColumn(BacklogItem backlog){
        for (Task task : backlog.getTasks()){
            task.setTaskStatus("Unassigned");
            updateTaskToStatusColumn(task);
        }
    }

    public void backToSprintOverview() {
        mainApp.showSprintOverview();
    }

    public void openSprintEdit(){
        if (mainApp.isEditAuthorized()){
            mainApp.showSprintEditPopUp(currentSprint);
        }
    }

    public void stageBacklogItems(){
        if (mainApp.isEditAuthorized()){
            BacklogItem item = backlogItemsListView.getSelectionModel().getSelectedItem();
            if (item != null){
                for (Task task : item.getTasks()){
                    if (task.getTaskStatus().equals("Unassigned")){
                        task.setTaskStatus("To Do");
                        updateTaskToStatusColumn(task);
                    }
                }
            }else{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.initOwner(mainApp.getPrimaryStage());
                alert.setTitle("No Selection");
                alert.setHeaderText("No Backlog Item Selected");
                alert.setContentText("Please select a backlog item in the table.");
                alert.showAndWait();
            }
        }
    }

    public void deleteBacklogItems(){
        if (mainApp.isEditAuthorized()){
            int selectedIndex =  backlogItemsListView.getSelectionModel().getSelectedIndex();
            BacklogItem item = backlogItemsListView.getSelectionModel().getSelectedItem();
            if (selectedIndex >= 0) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.initOwner(mainApp.getPrimaryStage());
                alert.setTitle("Confirmation");
                alert.setHeaderText("Delete Backlog Item");
                alert.setContentText("Are you sure you want to delete this backlog item?");

                ButtonType yesButton = new ButtonType("Yes");
                ButtonType noButton = new ButtonType("No");

                alert.getButtonTypes().setAll(yesButton, noButton);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == yesButton) {
                    this.currentSprint.deleteBacklog(item);
                    deleteBacklogItemsFromColumn(item);
                    backlogItemsListView.getItems().remove(selectedIndex);
                }
            } else {
                // Nothing selected.
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.initOwner(mainApp.getPrimaryStage());
                alert.setTitle("No Selection");
                alert.setHeaderText("No Backlog Item Selected");
                alert.setContentText("Please select a backlog item in the table.");
                alert.showAndWait();
            }
        }
    }

    public void addBacklogToCurrentSprint(BacklogItem item){
        if (!backlogItemsList.contains(item)){
            backlogItemsList.add(item);
            this.currentSprint.addBacklog(item);
        }
    }

    public void addTaskToBacklogItem(Task task){
        if (!currentBacklog.contains(task)){
            currentBacklog.addTask(task);
        }
        updateTaskToStatusColumn(task);
    }
    public void setCurrentBacklog(BacklogItem backlog){
        this.currentBacklog = backlog;
    }

    public BacklogItem findBacklogByTask(Task task){
        for (BacklogItem item : backlogItemsList){
            for (Task temp : item.getTasks()){
                if (temp.equals(task)){
                    return item;
                }
            }
        }
        return null;
    }
}

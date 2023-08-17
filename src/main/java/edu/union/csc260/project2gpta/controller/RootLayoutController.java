package edu.union.csc260.project2gpta.controller;


import edu.union.csc260.project2gpta.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;

import java.io.File;

/**
 * The controller for the root layout. The root layout provides the basic
 * application layout containing a menu bar and space where other JavaFX
 * elements can be placed.
 *
 * @author Marco Jakob, Kristina Striegnitz
 */
public class RootLayoutController {

    // Reference to the main application
    private MainApp mainApp;

    @FXML
    private Menu currentUserNavbar;

    @FXML
    private MenuItem groupOverviewItem;

    @FXML
    private MenuItem logOutItem;

    @FXML
    private void initialize(){
        setUserNavbarVisability(false);
        setCurrentUserNameToNavBar("");
    }

    public void setUserNavbarVisability(boolean val){
        groupOverviewItem.setDisable(!val);
        logOutItem.setDisable(!val);
    }

    public void setCurrentUserNameToNavBar(String name){
        currentUserNavbar.setText(name);
    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Creates an empty address book.
     */
    @FXML
    private void handleNew() {
//        mainApp.clearSignedInUserInfo();
    }

    /**
     * Opens a FileChooser to let the user select an address book to load.
     */
    @FXML
    private void handleOpen() {
        FileChooser fileChooser = new FileChooser();

        // Show open file dialog
        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

        if (file != null) {
            mainApp.loadSprintListDataFromFile(file);
        }
    }

    /**
     * Saves the file to the person file that is currently open. If there is no
     * open file, the "save as" dialog is shown.
     */
    @FXML
    private void handleSave() {
        File sprintListFile = mainApp.getSprintListFilePath();
        if (sprintListFile != null) {
            mainApp.saveSprintListToFile(sprintListFile);
        } else {
            handleSaveAs();
        }
    }

    /**
     * Opens a FileChooser to let the user select a file to save to.
     */
    @FXML
    private void handleSaveAs() {
        FileChooser fileChooser = new FileChooser();

        // Show save file dialog
        File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());

        if (file != null) {
            mainApp.saveSprintListToFile(file);
        }
    }

    /**
     * Opens an about dialog.
     */
    @FXML
    private void handleAbout() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Application");
        alert.setHeaderText("About");
        String aboutText = "Author: GPTA\nGitlab team: Garrett Pam Tenny - Ansh";
        aboutText += "\nModifications by: None";
        alert.setContentText(aboutText);

        alert.showAndWait();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        System.exit(0);
    }

    public void showGroupOverview() {
        this.mainApp.showGroupOverview();
    }

    public void handleSignOut(){
        mainApp.signOut();
    }
}

package edu.union.csc260.project2gpta.controller;

import edu.union.csc260.project2gpta.MainApp;
import edu.union.csc260.project2gpta.model.Group;
import edu.union.csc260.project2gpta.model.GroupManager;
import edu.union.csc260.project2gpta.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreateGroupController {

    private MainApp mainApp;

    @FXML
    private TextField groupName;

    @FXML
    private TextArea groupDescription;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    private void setUpExampleData(){
        groupName.setText("<Example Group Name>");
        groupDescription.setText("<Example Group Description>\nLine1: asbdkhajsd\nLine2: lakjsdajsd");
    }

    @FXML
    private void initialize(){
        setUpExampleData();
    }


    @FXML
    private void addGroupHandler(){
        GroupManager manager = (GroupManager) mainApp.getSignedInUser();
        Group group = new Group(manager, groupName.getText(), groupDescription.getText());
        cancelHandler();
        mainApp.showGroupOverview();
    }

    @FXML
    private void cancelHandler(){
        Stage stage = (Stage) groupName.getScene().getWindow();
        stage.close();
    }
}
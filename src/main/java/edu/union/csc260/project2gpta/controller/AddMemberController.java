package edu.union.csc260.project2gpta.controller;

import edu.union.csc260.project2gpta.MainApp;
import edu.union.csc260.project2gpta.model.Group;
import edu.union.csc260.project2gpta.model.GroupMember;
import edu.union.csc260.project2gpta.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddMemberController {

    private MainApp mainApp;

    @FXML
    private ComboBox<Group> groupList;

    @FXML
    private TextField userEmail;

    private Group currentGroup;

    private void setCurrentGroup(Group group){
        currentGroup = group;
    }

    /**
     *
     * @param email
     * @return Group member if email is valid, null if email has role "manager",
     * user already in group or no user found with this email
     */
    // Todo: change this to check with sql
    private GroupMember findUserWithEmail(String email){
        // finding user with given email
        for (User user : mainApp.getUserList()){
            if (user.getEmail().equals(email)){
                if (user instanceof GroupMember && ((GroupMember) user).getGroup()==null){
                    return (GroupMember) user;
                }else {
                    return null;    // role manager or user had a group already
                }
            }
        }
        return null;
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        groupList.setItems(mainApp.getGroupData());

        // DropDown listener
        groupList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setCurrentGroup(newValue);
            }
        });
    }

    @FXML
    private void initialize(){
        groupList.setCellFactory(param -> new ListCell<Group>() {
            @Override
            protected void updateItem(Group item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getGroupName());
                }
            }
        });
    }

    public void addMemberHandler(){
        if (currentGroup == null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Invalid Selection");
            alert.setHeaderText("Group undefined");
            alert.setContentText("Please select a group!");
            alert.showAndWait();
        }else{
            User user = findUserWithEmail(userEmail.getText());
            if (user == null){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.initOwner(mainApp.getPrimaryStage());
                alert.setTitle("Invalid user");
                alert.setHeaderText("User Invalid");
                alert.setContentText("Please select a valid user: \n1) That had signed up\n2) With 'Member' role\n3) And is not a member of any group");
                alert.showAndWait();
            }else{
                currentGroup.addMember((GroupMember) user);
                Stage stage = (Stage) userEmail.getScene().getWindow();
                stage.close();
                mainApp.showGroupOverview();
            }
        }
    }

    public void cancelHandler(){
        Stage stage = (Stage) userEmail.getScene().getWindow();
        stage.close();
    }
}
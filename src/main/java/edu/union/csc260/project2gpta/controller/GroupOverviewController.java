package edu.union.csc260.project2gpta.controller;

import edu.union.csc260.project2gpta.MainApp;
import edu.union.csc260.project2gpta.model.GroupManager;
import edu.union.csc260.project2gpta.model.GroupMember;
import edu.union.csc260.project2gpta.model.Group;
import edu.union.csc260.project2gpta.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Optional;

public class GroupOverviewController {

    // Need to update the <User> into <Group>; none right now
    @FXML
    private TreeView<String> groupTreeView;

    @FXML
    private Label displayName;

    @FXML
    private Label email;

    @FXML
    private Label role;

    @FXML
    private Label userGroupName;

    private MainApp mainApp;

    private User currentUser;

    private Group currentGroup;

    private Object currentItem;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        userGroupName.setText(mainApp.getSignedInUser().getDisplayName() + "'s Groups");
        initializeTreeView();
    }

    private void setUndefineUser(){
        displayName.setText("No user chosen");
        this.email.setText("example@union.edu");
        role.setText("Undefined");
    }

    private void setCurrentUser(String email){
        if (email == null){
            currentUser = null;
            setUndefineUser();
        }else{
            for (Group group : mainApp.getGroupData()){
                GroupManager manager = group.getManager();
                if (manager.getEmail().equals(email)){
                    currentUser = manager;
                    displayName.setText(manager.getDisplayName());
                    this.email.setText(manager.getEmail());
                    role.setText(manager.getRole());
                }else{
                    for (GroupMember member : group.getGroupMember()){
                        if (member.getEmail().equals(email)){
                            currentUser = member;
                            displayName.setText(member.getDisplayName());
                            this.email.setText(member.getEmail());
                            role.setText(member.getRole());
                        }
                    }
                }
            }
        }
    }

    /**
     *
     * */
    public void initializeTreeView() {
        TreeItem<String> rootItem = new TreeItem<>("root");

        for (Group group : mainApp.getGroupData()){
            TreeItem<String> parentItem = new TreeItem<>("Group: " + group.getGroupName());
            parentItem.setExpanded(true);
            parentItem.getChildren().add(new TreeItem<String>("Manager: " + group.getManager().getDisplayName() + " | " + group.getManager().getEmail()));
            for (GroupMember member : group.getGroupMember()){
                TreeItem<String> childItem = new TreeItem<>("Member: " + member.getDisplayName() + " | " + member.getEmail());
                parentItem.getChildren().add(childItem);
            }
            rootItem.getChildren().add(parentItem);
        }

        groupTreeView.setRoot(rootItem);
        groupTreeView.setShowRoot(false);

        // Add event listener to the groupTreeView
        groupTreeView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                TreeItem<String> selectedItem = groupTreeView.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    String itemValue = selectedItem.getValue();
                    if (itemValue.contains("Member") || itemValue.contains("Manager")){
                        String email = itemValue.substring(itemValue.indexOf(" | ") + 3);
                        setCurrentUser(email);
                    }else{
                        setCurrentUser(null);
                    }
                }
            }else if (event.getClickCount() == 2){
                TreeItem<String> selectedItem = groupTreeView.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    String itemValue = selectedItem.getValue();
                    if (itemValue.contains("Member") || itemValue.contains("Manager")){
                        String email = itemValue.substring(itemValue.indexOf(" | ") + 3);
                        setCurrentUser(email);
                        mainApp.setCurrentUser(this.currentUser);
                        mainApp.showSprintOverview();
                    }
                }
            }
        });
    }

    /**
     * Main Init
     * */
    public void initialize(){
        setUndefineUser();
    }

    public void showSprintOverview(){
        this.mainApp.showSprintOverview();
    }

    public void showCreateGroupPopUp(){
        if (mainApp.isManagerAuthorized()){
            this.mainApp.showCreateGroupPopUp();
        }
    }

    public void showAddMemberPopUp(){
        if (mainApp.isManagerAuthorized()){
            this.mainApp.showAddMemberPopUp();
        }
    }

    public void handleRemoveMember() {
        if (mainApp.isManagerAuthorized()){
            if (currentUser == null || currentUser instanceof GroupManager){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.initOwner(mainApp.getPrimaryStage());
                alert.setTitle("Invalid selection");
                alert.setHeaderText("Remove member failed");
                alert.setContentText("Cannot remove the chosen group member. Please select a valid member");
                alert.showAndWait();
            }else{
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.initOwner(mainApp.getPrimaryStage());
                alert.setTitle("Confirmation");
                alert.setHeaderText("Delete Group Member");
                alert.setContentText("Are you sure you want to delete this group member?");

                ButtonType yesButton = new ButtonType("Yes");
                ButtonType noButton = new ButtonType("No");

                alert.getButtonTypes().setAll(yesButton, noButton);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == yesButton) {
                    ((GroupMember) currentUser).getGroup().deleteMember((GroupMember) currentUser);
                    TreeItem<String> selectedItem = groupTreeView.getSelectionModel().getSelectedItem();
                    if (selectedItem != null) {
                        // Get the parent TreeItem
                        TreeItem<String> parentItem = selectedItem.getParent();
                        if (parentItem != null) {
                            // Remove the selected item from its parent's children list
                            parentItem.getChildren().remove(selectedItem);
                        }
                    }
                    setCurrentUser(null);
                }
            }
        }
    }
}
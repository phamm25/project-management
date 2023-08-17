package edu.union.csc260.project2gpta.controller;

import edu.union.csc260.project2gpta.MainApp;
import edu.union.csc260.project2gpta.model.GroupManager;
import edu.union.csc260.project2gpta.model.GroupMember;
import edu.union.csc260.project2gpta.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AuthFormController {

    @FXML
    private VBox signInForm;

    @FXML
    private VBox signUpForm;

    @FXML
    private TextField signInEmail;

    @FXML
    private TextField signUpName;

    @FXML
    private TextField signUpEmail;

    @FXML
    private ComboBox roleSelection;

    private Stage dialogStage;


    private MainApp mainApp;

    @FXML
    private void switchToSignUp() {
        signInForm.setVisible(false);
        signUpForm.setVisible(true);
    }

    @FXML
    private void switchToSignIn() {
        signUpForm.setVisible(false);
        signInForm.setVisible(true);
    }

    @FXML
    private void handleSignIn() {
        // Handle sign in
        boolean authenticated = false;
        User currentUser = null;
        for (User u : mainApp.getUserList()){
            if (u.getEmail().equals(signInEmail.getText())){
                authenticated = true;
                currentUser = u;
            }
        }
        if (authenticated){
            mainApp.signIn(currentUser);
            mainApp.showSprintOverview();
        }else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Authentication failed");
            alert.setHeaderText("Wrong credentials");
            alert.setContentText("User email invalid. Please enter a valid email or sign up!");
            alert.showAndWait();
        }

    }

    @FXML
    private void handleSignUp() {
        // Handle sign up
        boolean emailExisted = false;
        String email = signUpEmail.getText();

        for (User u : mainApp.getUserList()){
            if (u.getEmail().equals(email)){
                emailExisted = true;
            }
        }
        if (emailExisted){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Signup Failed");
            alert.setHeaderText("Email duplication");
            alert.setContentText("Email existed! Please use a different email.");
            alert.showAndWait();
        }else if (!(email.contains("@union.edu") || email.contains("@gmail.com"))){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Signup Failed");
            alert.setHeaderText("Email invalid");
            alert.setContentText("Please enter an email with @gmail.com or @union.edu");
            alert.showAndWait();
        }else if (roleSelection.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Signup Failed");
            alert.setHeaderText("Invalid Role");
            alert.setContentText("Please select a role for user");
            alert.showAndWait();
        }else{
            String role =  roleSelection.getSelectionModel().getSelectedItem().toString();
            User user;

            if (role.equals("Student")){
                if (!signUpName.getText().equals("")){
                    String name = signUpName.getText();
                    user = new GroupMember(name, email, role);
                }else{
                    user = new GroupMember(email, role);
                }
            }else {
                if (!signUpName.getText().equals("")){
                    String name = signUpName.getText();
                    user = new GroupManager(name, email, role);
                }else{
                    user = new GroupManager(email, role);
                }
            }
            mainApp.addNewUser(user);
            mainApp.signIn(user);
            mainApp.showSprintOverview();
        }
    }


    public void setMainApp(MainApp mainApp){
        this.mainApp = mainApp;
    }
}
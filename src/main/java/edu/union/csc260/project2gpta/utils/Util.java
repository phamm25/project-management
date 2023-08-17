package edu.union.csc260.project2gpta.utils;

import edu.union.csc260.project2gpta.MainApp;
import edu.union.csc260.project2gpta.controller.MainDashboardController;
import edu.union.csc260.project2gpta.controller.TaskComponentController;
import edu.union.csc260.project2gpta.model.Task;
import edu.union.csc260.project2gpta.model.GroupMember;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class Util {
    MainApp mainApp;

    MainDashboardController dashboardController;

    public VBox createTaskComponent(Task task){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/TaskComponent.fxml"));
            VBox taskComponent = (VBox) loader.load();

            TaskComponentController controller = loader.getController();
            controller.setMainApp(this.mainApp); // set mainApp here
            controller.setDashboardController(dashboardController);
            controller.setTask(task);

            return taskComponent;
        } catch (IOException error) {
            error.printStackTrace();
            return null;
        }
    }

    public void setMainApp(MainApp app){
        this.mainApp = app;
    }

    public void setDashboardController(MainDashboardController controller){
        this.dashboardController = controller;
    }

    public GroupMember createUserObject(String displayName, String email, String role){
        return new GroupMember(displayName, email,role);
    }
}

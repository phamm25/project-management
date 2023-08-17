package edu.union.csc260.project2gpta;

import edu.union.csc260.project2gpta.TestData.ListOfUsers;
import edu.union.csc260.project2gpta.controller.*;
import edu.union.csc260.project2gpta.model.*;
import edu.union.csc260.project2gpta.utils.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.Parent;


import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.sql.*;
import java.io.*;
import java.time.LocalDate;
import java.util.Formatter;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.util.prefs.Preferences;


public class MainApp extends javafx.application.Application {

    private Stage primaryStage;
    private BorderPane rootLayout;

    private RootLayoutController rootLayoutController;

    public Util util;

    /**
     * The data as an observable list of Sprints.
     */
    private ObservableList<Sprint> sprintData = FXCollections.observableArrayList();

    private ObservableList<Group> groupData = FXCollections.observableArrayList();

    // Use Test Data
//    private ObservableList<Sprint> sprintData = ListOfSprint.createTestSprint();

    // this will be use to get current user's sprint overview
    private User currentUser = null;

    // this will be use to get currently signed up user
    private User signedInUser = null;



    // only here for now to authorize whether the user exists.
    // Todo: When connect with sql, delete this and replace with sql authentication whether user exists.
    private ObservableList<User> userList = FXCollections.observableArrayList();

    private ObservableList<Group> groupList = FXCollections.observableArrayList();

    /**
     * Returns the data as an observable list of Sprints.
     * @return
     */
    public ObservableList<Sprint> getSprintData() {
        return sprintData;
    }

    public ObservableList<User> getUserList() {
        return userList;
    }

    public ObservableList<Group> getGroupData() {
        return groupData;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public User getSignedInUser(){
        return signedInUser;
    }

    private void setSprintData(ObservableList<Sprint> sprints) {
        sprintData = FXCollections.observableArrayList();
        sprintData.clear();
        sprintData = sprints;
    }

    private void setGroupData(User user){
        groupData.clear();
        if (user instanceof GroupManager){
            groupData = ((GroupManager) user).getGroupList();
        }else{
            groupData.add(((GroupMember) user).getGroup());
        }
    }

    public void signIn(User user){
        signedInUser = user;
        setGroupData(signedInUser);
        setCurrentUser(signedInUser);
        rootLayoutController.setCurrentUserNameToNavBar(signedInUser.getDisplayName());
        rootLayoutController.setUserNavbarVisability(true);
    }

    public void signOut(){
        clearSignedInUserInfo();
        rootLayoutController.setCurrentUserNameToNavBar("");
        rootLayoutController.setUserNavbarVisability(false);
        showAuthForm();
    }

    public void setCurrentUser(User user) {
        currentUser = user;
        setSprintData(currentUser.getSprintList());
    }

    public void addSprintData(Sprint sprint) {
        sprintData.add(sprint);
    }

    public void addNewUser(User user) {
        userList.add(user);
    }

    public void clearSignedInUserInfo() {
        this.sprintData = FXCollections.observableArrayList();;
        this.groupData = FXCollections.observableArrayList();;
        currentUser = null;
        signedInUser = null;
    }

    public boolean isEditAuthorized(){
        if (!signedInUser.equals(currentUser)){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(getPrimaryStage());
            alert.setTitle("Unauthorized");
            alert.setHeaderText("Invalid action");
            alert.setContentText("You can only edit your information");
            alert.showAndWait();
            return false;
        }else{
            return true;
        }
    }

    public boolean isManagerAuthorized(){
        if (!(signedInUser instanceof GroupManager)){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(getPrimaryStage());
            alert.setTitle("Unauthorized");
            alert.setHeaderText("Not a group Manager");
            alert.setContentText("You need to be a group manager to perform this action!");
            alert.showAndWait();
            return false;
        }else{
            return true;
        }
    }

    public boolean isSignedIn(){
        return getCurrentUser() != null;
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("MainApp");

        loadDataFromDB();
        
        // Set up utilities
        setUpUtils();

        initRootLayout();
        showAuthForm();
    }

   
    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml")); // relative path
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout,900,600); // big window
            primaryStage.setScene(scene);

            // Give the controller access to the main app.
            RootLayoutController controller = loader.getController();
            rootLayoutController = controller;
            controller.setMainApp(this);

            primaryStage.show();
        }
        catch (IOException error){
            error.printStackTrace();
        }
//         Try to load last opened sprint file.
//        File file = getSprintListFilePath();
//        if (file != null) {
//            loadSprintListDataFromFile(file);
//        }
    }

    /**
     * Set the stage to the AuthForm
     */
    public void showAuthForm() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/AuthForm.fxml"));
            AnchorPane authForm = loader.load();

            // Set person overview into the center of root layout.
            rootLayout.setCenter(authForm);

            // Set the person into the controller.
            AuthFormController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showGroupOverview() {
        try {
            // Load group overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/GroupOverview.fxml"));
            AnchorPane groupOverview = loader.load();

            // Set group overview into the center of root layout.
            rootLayout.setCenter(groupOverview);

            // Give the controller access to the main app.
            GroupOverviewController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showCreateGroupPopUp() {
        try{
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Create Group");
            FXMLLoader popupLoader = new FXMLLoader(MainApp.class.getResource("view/CreateGroupPopUp.fxml"));
            Parent popupRoot = popupLoader.load();

            CreateGroupController controller = popupLoader.getController();
            controller.setMainApp(this);

            Scene popupScene = new Scene(popupRoot, 400, 300);
            popupStage.setScene(popupScene);
            popupStage.showAndWait();
        }

        catch (IOException error){
            error.printStackTrace();
        }
    }

    public void showAddMemberPopUp() {
        try{
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Add Member");
            FXMLLoader popupLoader = new FXMLLoader(MainApp.class.getResource("view/AddMemberPopUp.fxml"));
            Parent popupRoot = popupLoader.load();

            AddMemberController controller = popupLoader.getController();
            controller.setMainApp(this);

            Scene popupScene = new Scene(popupRoot, 350, 150);
            popupStage.setScene(popupScene);
            popupStage.showAndWait();
        }
        catch (IOException error){
            error.printStackTrace();
        }
    }

    /**
     * Shows the Sprint Overview AnchorPane inside the root layout.
     */
    public void showSprintOverview(){
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/SprintOverview.fxml"));
            AnchorPane sprintOverview = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            rootLayout.setCenter(sprintOverview);

            // Give the controller access to the main app.
            SprintOverviewController controller = loader.getController();
            controller.setMainApp(this);
        }
        catch (IOException error){
            error.printStackTrace();
        }
    }


    /**
     * Shows the MainDashboard GridPane inside the root layout.
     */
    public void showMainDashboard(Sprint sprint){
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/Dashboard/mainDashboard.fxml"));
            GridPane mainDashboard = loader.load();

            // Set person overview into the center of root layout.
            rootLayout.setCenter(mainDashboard);

            // Give the controller access to the dashboard.
            MainDashboardController controller = loader.getController();
            controller.setMainApp(this);
            controller.setCurrentSprint(sprint);
        }
        catch (IOException error){
            error.printStackTrace();
        }
    }
    /**
     * Creates a new popup and displays Sprint Edit.
     */
    public void showSprintEditPopUp(Sprint sprint) {
        try{
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Sprint Edit");
            FXMLLoader popupLoader = new FXMLLoader(MainApp.class.getResource("view/PopUpEdit/SprintEdit.fxml"));
            Parent popupRoot = popupLoader.load();

            SprintEditController controller = popupLoader.getController();
            controller.setMainApp(this);
            controller.setSprint(sprint);

            Scene popupScene = new Scene(popupRoot, 400, 400);
            popupStage.setScene(popupScene);
            popupStage.showAndWait();

        }
        catch (IOException error){
            error.printStackTrace();
        }
    }

    public void showTaskEditPopUp(Task task, MainDashboardController dashboardController, BacklogItemOverviewController backlogItemOverviewController) {
        try{
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Task Edit");
            FXMLLoader popupLoader = new FXMLLoader(MainApp.class.getResource("view/PopUpEdit/TaskEdit.fxml"));
            Parent popupRoot = popupLoader.load();

            TaskEditController controller = popupLoader.getController();
            controller.setTask(task);
            controller.setMainApp(this);
            controller.setMainDashboardController(dashboardController);
            controller.setBacklogController(backlogItemOverviewController);

            Scene popupScene = new Scene(popupRoot, 400, 300);
            popupStage.setScene(popupScene);
            popupStage.showAndWait();
        }
        catch (IOException error){
            error.printStackTrace();
        }
    }

    public void showBacklogItemEdit(BacklogItem backlogItem, MainDashboardController dashboardController){
        // BacklogItem edit:
        try{
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Backlog Item Edit Window");

            FXMLLoader popupLoader = new FXMLLoader(MainApp.class.getResource("view/PopUpEdit/BacklogItemEdit.fxml"));
            Parent popupRoot = popupLoader.load();
            Scene popupScene = new Scene(popupRoot, 1000, 500);

            // get the controller for the FXML file
            BacklogItemOverviewController controller = popupLoader.getController();
            controller.setBacklogItem(backlogItem);
            controller.setMainApp(this);
            controller.setDashboardController(dashboardController);

            popupStage.setScene(popupScene);
            popupStage.showAndWait();
        }
        catch (IOException error){
            error.printStackTrace();
        }
    }

    /**
     * Set up utilities for this app
     */
    public void setUpUtils(){
        util = new Util();
        util.setMainApp(this);
    }

    /**
     * Returns the person file preference, i.e. the file that was last opened.
     * The preference is read from the OS specific registry. If no such
     * preference can be found, null is returned.
     *
     * @return
     */
    public File getSprintListFilePath() {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        String filePath = prefs.get("filePath", null);
        if (filePath != null) {
            return new File(filePath);
        } else {
            return null;
        }
    }

    /**
     * Sets the file path of the currently loaded file. The path is persisted in
     * the OS specific registry.
     *
     * @param file the file or null to remove the path
     */
    public void setSprintListFilePath(File file) {
        
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        if (file != null) {
            prefs.put("filePath", file.getPath());

            // Update the stage title.
            primaryStage.setTitle("MainApp - " + file.getName());
        } else {
            prefs.remove("filePath");

            // Update the stage title.
            primaryStage.setTitle("MainApp");
        }
    }

    /**
     * Saves the current person data to the specified file.
     *
     * @param file
     */
    public void saveSprintListToFile(File file) {
        try {
            Formatter formatter = new Formatter(file);
            StringBuilder data = new StringBuilder();
            for (Sprint sprint : sprintData) {
                List<String> allSprint = List.of(sprint.getSprintName(), DateUtil.format(sprint.getStartDate()),
                        DateUtil.format(sprint.getEndDate()));
                String s = String.join("/sep/", allSprint);
                data.append("\n/sprint/").append(s);

                for (BacklogItem backlogItem : sprint.getAllBacklogItems()){
                    List<String> allBackLog = List.of(backlogItem.getName(), backlogItem.getPriority(),
                            backlogItem.getBacklogItemDescription(), DateUtil.format(backlogItem.getStartDate()),
                            DateUtil.format(backlogItem.getEndDate()));
                    String s1 = String.join("/sep/", allBackLog);
//                    formatter.format(s + s1);
//                    formatter.format(s + "\n/task/");
                    data.append("\n/backlog/").append(s1);

                    for (Task task : backlogItem.getTasks()){
                        List<String> allTasks = List.of(task.getTaskName(), task.getTaskStatus(),
                                task.getTaskDescription());
                        String s2 = String.join("/sep/", allTasks);
//                        formatter.format(s + s2);
//                        formatter.format(s2 + "\n");
                        data.append("\n/task/").append(s2);
                    }
                }
            }
            formatter.format(data.toString());
            formatter.close();
            // Save the file path to the registry.
            setSprintListFilePath(file);
        } catch (Exception e) { // catches ANY exception
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not save data");
            alert.setContentText("Could not save data to file:\n" + file.getPath());

            alert.showAndWait();
        }
    }

/**
 * Saves the current sprint data to the specified database.
 * For each sprint in sprintData, if the sprint's id is null, it inserts a new sprint into the sprints table in the database, and sets the new id for the sprint variable.
 * Otherwise, it updates the corresponding sprint in the sprints table.
 * The same process is applied for the BacklogItems and Tasks associated with each Sprint.
 */
public void saveSprintListToDB() {
    try {
        String url = "jdbc:mysql://127.0.0.1:3333/group3_db";
        String username = "singhala";
        String password = "Un!on2638391";
        
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            for (Sprint sprint : sprintData) {
                if (sprint.getId() == null) {
                    try (PreparedStatement statement = connection.prepareStatement("INSERT INTO `sprints` (`name`, `startdate`, `enddate`) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                        statement.setString(1, sprint.getSprintName());
                        statement.setDate(2, java.sql.Date.valueOf(sprint.getStartDate()));
                        statement.setDate(3, java.sql.Date.valueOf(sprint.getEndDate()));
                        statement.executeUpdate();
                        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
//                                sprint.setId(generatedKeys.getInt(1));
                            }
                        }
                    }
                } else {
                    try (PreparedStatement statement = connection.prepareStatement("UPDATE `sprints` SET `name` = ?, `startdate` = ?, `enddate` = ? WHERE `sprintid` = ?")) {
                        statement.setString(1, sprint.getSprintName());
                        statement.setDate(2, java.sql.Date.valueOf(sprint.getStartDate()));
                        statement.setDate(3, java.sql.Date.valueOf(sprint.getEndDate()));
//                        statement.setInt(4, sprint.getId());
                        statement.executeUpdate();
                    }
                }

                for (BacklogItem backlogItem : sprint.getAllBacklogItems()){
                    if (backlogItem.getId() == null) {
                        try (PreparedStatement statement2 = connection.prepareStatement("INSERT INTO `backlogs` (`name`, `priority`, `backlogItemDescription`, `startDate`, `endDate`, `parentsprintid`) VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                            statement2.setString(1, backlogItem.getName());
                            statement2.setString(2, backlogItem.getPriority());
                            statement2.setString(3, backlogItem.getBacklogItemDescription());
                            statement2.setDate(4, java.sql.Date.valueOf(backlogItem.getStartDate()));
                            statement2.setDate(5, java.sql.Date.valueOf(backlogItem.getEndDate()));
//                            statement2.setInt(6, sprint.getId());
                            statement2.executeUpdate();
                            try (ResultSet generatedKeys = statement2.getGeneratedKeys()) {
                                if (generatedKeys.next()) {
//                                    backlogItem.setId(generatedKeys.getInt(1));
                                }
                            }
                        }
                    } else {
                        try (PreparedStatement statement2 = connection.prepareStatement("UPDATE `backlogs` SET `name` = ?, `priority` = ?, `backlogItemDescription` = ?, `startDate` = ?, `endDate` = ? WHERE `backlogid` = ?")) {
                            statement2.setString(1, backlogItem.getName());
                            statement2.setString(2, backlogItem.getPriority());
                            statement2.setString(3, backlogItem.getBacklogItemDescription());
                            statement2.setDate(4, java.sql.Date.valueOf(backlogItem.getStartDate()));
                            statement2.setDate(5, java.sql.Date.valueOf(backlogItem.getEndDate()));
//                            statement2.setInt(6, backlogItem.getId());
                            statement2.executeUpdate();
                        }
                    }

                    for (Task task : backlogItem.getTasks()){
                        if (task.getId() == null) {
                            try (PreparedStatement statement3 = connection.prepareStatement("INSERT INTO `tasks` (`name`, `status`, `description`, `parentbacklogid`) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                                statement3.setString(1, task.getTaskName());
                                statement3.setString(2, task.getTaskStatus());
                                statement3.setString(3, task.getTaskDescription());
//                                statement3.setInt(4, backlogItem.getId());
                                statement3.executeUpdate();
                                try (ResultSet generatedKeys = statement3.getGeneratedKeys()) {
                                    if (generatedKeys.next()) {
//                                        task.setId(generatedKeys.getInt(1));
                                    }
                                }
                            }
                        } else {
                            try (PreparedStatement statement3 = connection.prepareStatement("UPDATE `tasks` SET `name` = ?, `status` = ?, `description` = ? WHERE `taskid` = ?")) {
                                statement3.setString(1, task.getTaskName());
                                statement3.setString(2, task.getTaskStatus());
                                statement3.setString(3, task.getTaskDescription());
//                                statement3.setInt(4, task.getId());
                                statement3.executeUpdate();
                            }
                        }
                    }
                }
            }
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
}


    

    /**
     * Loads person data from the specified file. The current person data will
     * be replaced.
     *
     * @param file
     */
    public void loadSprintListDataFromFile(File file) {
        try {
            sprintData.clear();

            // parse file to string
            Scanner scanner = new Scanner(file);
            StringBuilder stringBuilder = new StringBuilder();
            while (scanner.hasNextLine()) {
                stringBuilder.append(scanner.nextLine());
                stringBuilder.append(System.lineSeparator());
            }
            scanner.close();
            String content = stringBuilder.toString();

            // retrieves data
            String[] data = content.split("\n/sprint/");
            for (int s = 1; s < data.length; s++){
                String stringSprint = data[s];
                String[] data2 = stringSprint.split("\n/backlog/");
                String[] sprintInfo = data2[0].split("/sep/");

                Sprint newSprint = new Sprint(sprintInfo[0],
                        DateUtil.parse(sprintInfo[1]),
                        DateUtil.parse(sprintInfo[2]));
                
                sprintData.add(newSprint);
                for (int b = 1; b < data2.length; b++){
                    String stringBacklog = data2[b];
                    String[] data3 = stringBacklog.split("\n/task/");
                    String[] backlogInfo = data3[0].split("/sep/");
                    BacklogItem newBacklog = new BacklogItem(backlogInfo[0],
                            backlogInfo[1],
                            backlogInfo[2],
                            DateUtil.parse(backlogInfo[3]),
                            DateUtil.parse(backlogInfo[4]));
                    newSprint.addBacklog(newBacklog);
                    for (int t = 1; t < data3.length; t++){
                        String stringTask = data3[t];
                        String[] taskInfo = stringTask.split("/sep/");

                        Task newTask = new Task(taskInfo[0], taskInfo[1], taskInfo[2]);
                        newBacklog.addTask(newTask);
                    }
                }
            }

            setSprintListFilePath(file);
        } catch (Exception e) { // catches ANY exception
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load data");
            alert.setContentText("Could not load data from file:\n" + file.getPath());
            alert.showAndWait();
        }
    }

    private void loadSprintsToUserFromDB(User user, String currentUserId, Connection connection){
        try{
            Statement statement = getNewSqlStatement(connection);
            ResultSet rs = statement.executeQuery("SELECT * FROM `sprints` WHERE `parentuserid` = '" + currentUserId + "'");
            while (rs.next()) {
                String sprintId = rs.getString("sprintid");
                String sprintName = rs.getString("name");
                String sprintReflection = rs.getString("reflection");
                LocalDate sprintStart = DateUtil.parse(rs.getString("startdate"));
                LocalDate sprintEnd = DateUtil.parse(rs.getString("enddate"));

                Sprint sprint = new Sprint(sprintName, sprintStart, sprintEnd);
                sprint.setId(sprintId);
                if (sprintReflection != null){
                    sprint.setReflection(sprintReflection);
                }
                user.addSprint(sprint);
                Statement statement2 = getNewSqlStatement(connection);
                ResultSet rs2 = statement2.executeQuery("SELECT * FROM `backlogs` WHERE `parentsprintid` = '" + sprintId + "'");
                while (rs2.next()){
                    String backlogId = rs2.getString("backlogid");
                    String backlogName = rs2.getString("name");
                    String backlogPriority = rs2.getString("priority");
                    String backlogDesc = rs2.getString("backlogItemDescription");
                    LocalDate backlogStart = DateUtil.parse(rs2.getString("startDate"));
                    LocalDate backlogEnd = DateUtil.parse(rs2.getString("endDate"));

                    BacklogItem backlog = new BacklogItem(backlogName, backlogPriority, backlogDesc, backlogStart, backlogEnd);
                    backlog.setId(backlogId);
                    sprint.addBacklog(backlog);

                    Statement statement3 = getNewSqlStatement(connection);
                    ResultSet rs3 = statement3.executeQuery("SELECT * FROM `tasks` WHERE `parentbacklogid` = '" + backlogId + "'");
                    while (rs3.next()){
                        String taskId = rs3.getString("taskid");
                        String taskName = rs3.getString("name");
                        String taskStatus = rs3.getString("status");
                        String taskDesc = rs3.getString("description");

                        Task task = new Task(taskName, taskStatus, taskDesc);
                        task.setId(taskId);
                        backlog.addTask(task);
                    }
                }
            }

        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load data");
            alert.setContentText("Could not load sprints data for users from database: \n" + e.getMessage());
            alert.showAndWait();
        }

    }

    private Statement getNewSqlStatement(Connection connection) throws SQLException {
        return connection.createStatement();
    }

    public void loadDataFromDB() {
        try {
            String url = "jdbc:mysql://127.0.0.1:3333/group3_db";
            String username = "singhala";
            String password = "Un!on2638391";
            Connection connection;
            System.out.println("======================");
            System.out.println("Connecting database...");
            try {
                connection = DriverManager.getConnection(url, username, password);
            } catch (SQLException e) {
                throw new IllegalStateException("Cannot connect the database!", e);
            }
            System.out.println("Database connected!");

            Statement statement = getNewSqlStatement(connection);
            ResultSet rs = statement.executeQuery("SELECT * FROM `groups`");;

            while (rs.next()) {
                String creatorID = rs.getString("groupmanagerid");
                String groupID = rs.getString("groupid");
                Statement statement2 = getNewSqlStatement(connection);
                ResultSet rs2 = statement2.executeQuery("SELECT * FROM `users` WHERE `userid` = '" + creatorID + "'");
                Group group;
                GroupManager manager = null;

                while (rs2.next()){
                    String managerDisplayName = rs2.getString("displayName");
                    String managerEmail = rs2.getString("email");
                    String managerRole = rs2.getString("role");

                    if (managerDisplayName != null){
                        manager = new GroupManager(managerDisplayName, managerEmail, managerRole);
                    }else{
                        manager = new GroupManager(managerEmail, managerRole);
                    }
                }
                if (manager != null) {
                    manager.setId(creatorID);
                    userList.add(manager);
                    loadSprintsToUserFromDB(manager, creatorID, connection);
                }
                String groupName = rs.getString("groupname");
                String groupDescription = rs.getString("groupdesc");

                if (groupName == null || groupDescription == null){
                    group = new Group(manager);
                }else{
                    group = new Group(manager, groupName, groupDescription);
                }
                group.setId(groupID);
                groupList.add(group);

                Statement statement3 = getNewSqlStatement(connection);
                ResultSet rs3 = statement3.executeQuery("SELECT * FROM `users` WHERE `groupid` = '" + groupID + "'");
                while (rs3.next()){
                    String memberRole = rs3.getString("role");
                    if (memberRole.equals("Member")){
                        GroupMember member;
                        String memberId = rs3.getString("userid");
                        String memberDisplayName = rs3.getString("displayName");
                        String memberEmail = rs3.getString("email");


                        if (memberDisplayName != null){
                            member = new GroupMember(memberDisplayName, memberEmail, memberRole);
                        }else{
                            member = new GroupMember(memberEmail, memberRole);
                        }
                        member.setId(memberId);
                        userList.add(member);
                        loadSprintsToUserFromDB(member,memberId, connection);
                        group.addMember(member);
                    }
                }
                Statement statement4 = getNewSqlStatement(connection);
                ResultSet rs4 = statement4.executeQuery("SELECT * FROM `users` WHERE `groupid` IS NULL");
                while (rs4.next()){
                    User newUser;

                    String userIDD = rs4.getString("userid");
                    String userDisplayName = rs4.getString("displayName");
                    String userEmail = rs4.getString("email");
                    String userRole = rs4.getString("role");

                    if (userRole.equals("Member")){
                        if (userDisplayName != null){
                            newUser = new GroupMember(userDisplayName, userEmail, userRole);
                        }else{
                            newUser = new GroupMember(userEmail, userRole);
                        }
                    } else{
                        if (userDisplayName != null){
                            newUser = new GroupManager(userDisplayName, userEmail, userRole);
                        }else{
                            newUser = new GroupManager(userEmail, userRole);
                        }
                    }
                    newUser.setId(userIDD);
                    userList.add(newUser);
                    loadSprintsToUserFromDB(newUser, userIDD, connection);
                }
            }
            connection.close();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load data");
            alert.setContentText("Could not load data from database: \n" + e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
        }
    }
    



    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }
}
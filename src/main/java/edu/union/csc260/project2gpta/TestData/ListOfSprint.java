package edu.union.csc260.project2gpta.TestData;

import java.time.LocalDate;

import edu.union.csc260.project2gpta.model.Sprint;
import edu.union.csc260.project2gpta.model.BacklogItem;
import edu.union.csc260.project2gpta.model.Task;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ListOfSprint {
    public static ObservableList<Sprint> createTestSprint(){
        ObservableList<Sprint> exampleSprintList = FXCollections.observableArrayList();

        //  creating sprints:
        Sprint sprint1 = new Sprint(
                "Sprint 1",
                LocalDate.of(2023,5,6),
                LocalDate.of(2023,5,17)
        );
        Sprint sprint2 = new Sprint(
                "Sprint 2",
                LocalDate.of(2023,4,6),
                LocalDate.of(2023,4,8)
        );
        Sprint sprint3 = new Sprint(
                "Sprint 3",
                LocalDate.of(2023,1,6),
                LocalDate.of(2023,4,22)
        );

        //  creating BacklogItems:
        BacklogItem backlogItem1 = new BacklogItem(
                "Backlog Item Name 1",
                "Low",
                "Backlog Item Description\n- Line 1\n- Line 2\n- etc...",
                LocalDate.of(2023,5,6),
                LocalDate.of(2023,5,12)
        );
        BacklogItem backlogItem2 = new BacklogItem(
                "Backlog Item Name 2",
                "Medium",
                "Backlog Item Description\n- Line 1\n- Line 2\n- etc...",
                LocalDate.of(2023,4,6),
                LocalDate.of(2023,4,8)
        );
        BacklogItem backlogItem3 = new BacklogItem(
                "Backlog Item Name 3",
                "High",
                "Backlog Item Description\n- Line 1\n- Line 2\n- etc...",
                LocalDate.of(2023,3,6),
                LocalDate.of(2023,3,8)
        );

        //  creating Tasks:
        Task task1 = new Task("Task 1", "To Do", "asdasd\nasdasdas\nasdasd");
        Task task2 = new Task("Task 2", "In Progress", "asdasd\nasdasdas\nasdasd");
        Task task3 = new Task("Task 3", "To Test", "asdasd\nasdasdas\nasdasd");
        Task task4 = new Task("Task 4", "Done", "asdasd\nasdasdas\nasdasd");
        Task task5 = new Task("Task 5", "To Do", "asdasd\nasdasdas\nasdasd");
        Task task6 = new Task("Task 6", "In Progress", "asdasd\nasdasdas\nasdasd");
        Task task7 = new Task("Task 7", "To Test", "asdasd\nasdasdas\nasdasd");
        Task task8 = new Task("Task 8", "Done", "asdasd\nasdasdas\nasdasd");
        Task task9 = new Task("Task 9", "To Do", "asdasd\nasdasdas\nasdasd");
        Task task10 = new Task("Task 10", "In Progress", "asdasd\nasdasdas\nasdasd");
        Task task11 = new Task("Task 11", "To Test", "asdasd\nasdasdas\nasdasd");
        Task task12 = new Task("Task 12", "Done", "asdasd\nasdasdas\nasdasd");
        Task task13 = new Task("Task 13", "Unassigned", "asdasd\nasdasdas\nasdasd");
        Task task14 = new Task("Task 14", "Unassigned", "asdasd\nasdasdas\nasdasd");
        Task task15 = new Task("Task 15", "Unassigned", "asdasd\nasdasdas\nasdasd");

        // adding task to BacklogItems:
        backlogItem1.addTask(task1);
        backlogItem1.addTask(task2);
        backlogItem1.addTask(task3);
        backlogItem1.addTask(task4);
        backlogItem1.addTask(task13);

        backlogItem2.addTask(task5);
        backlogItem2.addTask(task6);
        backlogItem2.addTask(task7);
        backlogItem2.addTask(task8);
        backlogItem2.addTask(task14);

        backlogItem3.addTask(task9);
        backlogItem3.addTask(task10);
        backlogItem3.addTask(task11);
        backlogItem3.addTask(task12);
        backlogItem3.addTask(task15);

        //  adding backlog to Sprint:
        sprint1.addBacklog(backlogItem1);
        sprint1.addBacklog(backlogItem2);
        sprint1.addBacklog(backlogItem3);

        //  adding sprint to Sprint List:
        exampleSprintList.add(sprint1);
        exampleSprintList.add(sprint2);
        exampleSprintList.add(sprint3);

        return exampleSprintList;
    }
}

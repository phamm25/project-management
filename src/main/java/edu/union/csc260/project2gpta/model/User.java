package edu.union.csc260.project2gpta.model;

import javafx.collections.ObservableList;

public interface User{
    public String getDisplayName();

    public void setDisplayName(String displayName);

    public String getEmail();

    public String getRole();

    public void setRole(String role);

    public ObservableList<Sprint> getSprintList();

    public String getId();

    public void addSprint(Sprint sprint);

    public void setId(String id);
}

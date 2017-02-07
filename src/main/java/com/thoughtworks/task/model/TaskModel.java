package com.thoughtworks.task.model;

import java.util.Date;

public class TaskModel implements Comparable<TaskModel> {
    private Long id;
    private String name;
    private boolean isCompleted;
    private Date timeCreated;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) { this.id = id; }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getIsCompleted() {
        return this.isCompleted;
    }

    public void setIsCompleted(boolean isCompleted) { this.isCompleted = isCompleted; }

    public Date getTimeCreated() {
        return this.timeCreated;
    }

    public void setTimeCreated(Date timeCreated) { this.timeCreated = timeCreated; }

    @Override
    public int compareTo(TaskModel taskModel) {
        return this.id.compareTo(taskModel.getId());
    }
}
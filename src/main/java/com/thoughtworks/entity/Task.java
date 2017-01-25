package com.thoughtworks.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "TASK")
public class Task {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME", nullable = false, length = 256)
    private String name;

    @Column(name = "IS_COMPLETED", nullable = false)
    private boolean isCompleted;

    @Column(name = "TIME_CREATED", nullable = false)
    private Date timeCreated;

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getIsCompleted() {
        return this.isCompleted;
    }

    public void setIsCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public Date getTimeCreated() {
        return this.timeCreated;
    }
}

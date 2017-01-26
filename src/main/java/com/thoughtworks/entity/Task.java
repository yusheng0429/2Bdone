package com.thoughtworks.entity;

import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "TASK")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME", nullable = false, length = 256)
    private String name;

    @Column(name = "IS_COMPLETED", nullable = false)
    private boolean status;

    @CreatedDate
    @Column(name = "TIME_CREATED", updatable = false, insertable = true)
    private Date timeCreated = new Date();

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
        return this.status;
    }

    public void setIsCompleted(boolean isCompleted) {
        this.status = isCompleted;
    }

    public Date getTimeCreated() {
        return this.timeCreated;
    }
}

package com.thoughtworks.task.dao;

import com.thoughtworks.task.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByIsCompleted(boolean isCompleted);
    Task findById(Long id);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE Task t SET t.isCompleted = :isCompleted WHERE t.isCompleted != :isCompleted")
    int updateIsCompleted(@Param("isCompleted") boolean isCompleted);
}

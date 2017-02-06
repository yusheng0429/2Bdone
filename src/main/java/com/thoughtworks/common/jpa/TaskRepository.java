package com.thoughtworks.common.jpa;

import com.thoughtworks.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByStatus(boolean isCompleted);
    Task findById(Long id);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE Task t SET t.status = :isCompleted WHERE t.status != :isCompleted")
    int updateStatus(@Param("isCompleted") boolean isCompleted);
}

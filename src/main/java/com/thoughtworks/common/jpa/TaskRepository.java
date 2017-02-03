package com.thoughtworks.common.jpa;

import com.thoughtworks.entity.Task;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface TaskRepository extends PagingAndSortingRepository<Task, String> {
    List<Task> findByStatus(boolean isCompleted);
    Task findById(Long id);
}

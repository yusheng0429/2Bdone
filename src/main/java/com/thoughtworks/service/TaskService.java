package com.thoughtworks.service;

import com.thoughtworks.common.jpa.TaskRepository;
import com.thoughtworks.entity.Task;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    public List<Task> getAllTasks() {
        return Lists.newArrayList(taskRepository.findAll(new Sort(Sort.Direction.ASC, "id")));
    }

    public List<Task> getTasks(boolean isCompleted) {
        return Lists.newArrayList(taskRepository.findByStatus(isCompleted));
    }

    public Task addTask(String name) {
        Task task = new Task();
        task.setName(name);
        task.setIsCompleted(false);
        taskRepository.save(task);
        return task;
    }

    public boolean deleteTask(Long id) {
        boolean result = false;
        Task task = taskRepository.findById(id);
        if (task != null) {
            taskRepository.delete(id);
            result = true;
        }
        return result;
    }
}

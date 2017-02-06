package com.thoughtworks.task.service;

import com.thoughtworks.common.exception.NotFoundException;
import com.thoughtworks.common.jpa.TaskRepository;
import com.thoughtworks.task.entity.Task;
import com.thoughtworks.task.mapper.TaskMapper;
import com.thoughtworks.task.model.TaskModel;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskMapper taskMapper;

    public List<TaskModel> getAllTasks() {
        return getTaskModels(taskRepository.findAll(new Sort(Sort.Direction.ASC, "id")));
    }

    public List<TaskModel> getTasks(boolean isCompleted) {
        return getTaskModels(taskRepository.findByStatus(isCompleted));
    }

    public Task addTask(String name) {
        Task task = new Task();
        task.setName(name);
        task.setStatus(false);
        taskRepository.save(task);
        return task;
    }

    public void deleteTask(Long id) throws NotFoundException {
        Task task = taskRepository.findById(id);
        if (task != null) {
            taskRepository.delete(id);
        } else {
            throw new NotFoundException(getTaskIdNotFoundErrorMessage(id));
        }
    }

    public TaskModel changeTaskStatus(Long id, boolean isCompleted) throws NotFoundException {
        Task task = taskRepository.findById(id);
        if (task == null) {
            throw new NotFoundException(getTaskIdNotFoundErrorMessage(id));
        }
        task.setStatus(isCompleted);
        taskRepository.save(task);
        return taskMapper.map(task, TaskModel.class);
    }

    public List<TaskModel> changeAllTaskStatus(boolean isCompleted) {
        taskRepository.updateStatus(isCompleted);
        return getTaskModels(taskRepository.findByStatus(isCompleted));
    }

    private String getTaskIdNotFoundErrorMessage(Long id) {
        return "Task not found with id: " + id;
    }

    private List<TaskModel> getTaskModels(List<Task> tasks) {
        List<TaskModel> taskModels = new ArrayList<TaskModel>();
        for (Task task : tasks) {
            taskModels.add(taskMapper.map(task, TaskModel.class));
        }
        return taskModels;
    }
}

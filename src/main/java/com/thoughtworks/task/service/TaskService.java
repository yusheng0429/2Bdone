package com.thoughtworks.task.service;

import com.thoughtworks.common.exception.ForbiddenException;
import com.thoughtworks.common.exception.NotFoundException;
import com.thoughtworks.task.dao.TaskRepository;
import com.thoughtworks.task.entity.Task;
import com.thoughtworks.task.mapper.TaskMapper;
import com.thoughtworks.task.model.TaskModel;
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
        return getTaskModels(taskRepository.findByIsCompleted(isCompleted));
    }

    public TaskModel addTask(String name) {
        Task task = new Task();
        task.setName(name);
        task.setIsCompleted(false);
        taskRepository.save(task);
        return taskMapper.map(task, TaskModel.class);
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
        task.setIsCompleted(isCompleted);
        taskRepository.save(task);
        return taskMapper.map(task, TaskModel.class);
    }

    public List<TaskModel> changeAllTaskStatus(boolean isCompleted) {
        taskRepository.updateIsCompleted(isCompleted);
        return getTaskModels(taskRepository.findByIsCompleted(isCompleted));
    }

    public void deleteTasks(boolean isCompleted) throws ForbiddenException {
        if (!isCompleted) {
            throw new ForbiddenException(getDeleteForbiddenErrorMessage());
        }
    }

    private String getTaskIdNotFoundErrorMessage(Long id) {
        return "Task not found with id: " + id;
    }
    private String getDeleteForbiddenErrorMessage() { return "Active tasks cannot be removed"; }

    private List<TaskModel> getTaskModels(List<Task> tasks) {
        List<TaskModel> taskModels = new ArrayList<TaskModel>();
        for (Task task : tasks) {
            taskModels.add(taskMapper.map(task, TaskModel.class));
        }
        return taskModels;
    }
}

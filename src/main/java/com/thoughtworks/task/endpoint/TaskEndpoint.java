package com.thoughtworks.task.endpoint;

import com.thoughtworks.common.exception.ForbiddenException;
import com.thoughtworks.common.exception.NotFoundException;
import com.thoughtworks.task.model.TaskModel;
import com.thoughtworks.task.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskEndpoint {
    @Autowired
    private TaskService taskService;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @RequestMapping(path = "/{isCompleted}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List> getAllTasks(@PathVariable boolean isCompleted) {
        return ResponseEntity.ok(taskService.getTasks(isCompleted));
    }

    @RequestMapping(path = "/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TaskModel> addTask(@RequestParam(value = "name", required = true) String name) {
        return ResponseEntity.ok(taskService.addTask(name));
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public void deleteTask(@PathVariable("id") Long id) throws NotFoundException {
        taskService.deleteTask(id);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TaskModel> changeTaskStatus(@PathVariable("id") Long id,
                                 @RequestBody(required = true) TaskModel task) throws NotFoundException {
        return ResponseEntity.ok(taskService.changeTaskStatus(id, task.getIsCompleted()));
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List> changeAllTaskStatus(@RequestParam(value = "isCompleted", required = true) boolean isCompleted) {
        return ResponseEntity.ok(taskService.changeAllTaskStatus(isCompleted));
    }

    @RequestMapping(method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteTasks(@RequestParam(value = "isCompleted", required = true) boolean isCompleted) throws ForbiddenException {
        taskService.deleteTasks(isCompleted);
    }
}
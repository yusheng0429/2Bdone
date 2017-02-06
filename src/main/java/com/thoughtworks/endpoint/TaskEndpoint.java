package com.thoughtworks.endpoint;

import com.thoughtworks.common.exception.NotFoundException;
import com.thoughtworks.entity.Task;
import com.thoughtworks.service.TaskService;
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

    @RequestMapping(path = "/{isActive}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List> getAllTasks(@PathVariable boolean isActive) {
        return ResponseEntity.ok(taskService.getTasks(!isActive));
    }

    @RequestMapping(path = "/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Task> addTask(@RequestParam(value = "name", required = true) String name) {
        return ResponseEntity.ok(taskService.addTask(name));
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public void deleteTask(@PathVariable("id")Long id) throws NotFoundException {
        taskService.deleteTask(id);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Task> changeTaskStatus(@PathVariable("id") Long id,
                                 @RequestBody(required = true) Task task) throws NotFoundException {
        return ResponseEntity.ok(taskService.changeTaskStatus(id, task.getStatus()));
    }
}
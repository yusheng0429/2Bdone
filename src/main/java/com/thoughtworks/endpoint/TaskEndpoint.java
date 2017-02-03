package com.thoughtworks.endpoint;

import com.thoughtworks.entity.Task;
import com.thoughtworks.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskEndpoint {
    @Autowired
    private TaskService taskService;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @RequestMapping(path = "/{isActive}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List> getAllTasks(@PathVariable boolean isActive) {
        return ResponseEntity.ok(taskService.getTasks(!isActive));
    }

    @RequestMapping(path = "/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Task> addTask(@RequestParam(value = "name", required = true) String name) {
        return ResponseEntity.ok(taskService.addTask(name));
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteTask(@PathVariable("id")Long id) {
        if (taskService.deleteTask(id)) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found with id: " + id.toString());
        }
    }
}
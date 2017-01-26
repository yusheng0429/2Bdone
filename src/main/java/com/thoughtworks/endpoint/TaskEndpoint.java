package com.thoughtworks.endpoint;

import com.thoughtworks.entity.Task;
import com.thoughtworks.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskEndpoint {
    @Autowired
    private TaskService taskService;

    @RequestMapping(path = "/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks().toString());
    }
}

package com.thoughtworks.common.jpa;

import com.thoughtworks.entity.Task;
import org.springframework.data.repository.CrudRepository;

public interface TaskRepository extends CrudRepository<Task, String> {
}

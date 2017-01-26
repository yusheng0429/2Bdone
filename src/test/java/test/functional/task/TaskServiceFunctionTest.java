package test.functional.task;

import com.thoughtworks.ToBdoneApplication;
import com.thoughtworks.common.constant.EnvProfile;
import com.thoughtworks.common.jpa.TaskRepository;
import com.thoughtworks.entity.Task;
import com.thoughtworks.service.TaskService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@Rollback
@Transactional
@ActiveProfiles({EnvProfile.TEST})
@SpringBootTest(classes = ToBdoneApplication.class)
public class TaskServiceFunctionTest {
    @Autowired
    private TaskService taskSevice;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private List<Task> defaultTasks;

    @Before
    private void initializeTasks() {
        Task task = new Task();
        task.setName("Have breakfast");
        task.setIsCompleted(true);
        saveDefaultTask(task);

        task = new Task();
        task.setName("Read the book");
        task.setIsCompleted(false);
        saveDefaultTask(task);

        task = new Task();
        task.setName("Go to work");
        task.setIsCompleted(true);
        saveDefaultTask(task);

        task = new Task();
        task.setName("Go to the grocery store");
        task.setIsCompleted(false);
        saveDefaultTask(task);
    }

    private void saveDefaultTask(Task task) {
        taskRepository.save(task);
        defaultTasks.add(task);
    }

    @Test
    public void should_getAllTasks_return_tasks() {
        //given
        //when
        List<Task> tasks = taskSevice.getAllTasks();
        //then
        assertThat(tasks).isEqualTo(defaultTasks);
    }
}

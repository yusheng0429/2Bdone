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

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@Rollback
@Transactional
@ActiveProfiles({EnvProfile.TEST})
@SpringBootTest(classes = ToBdoneApplication.class)
public class TaskServiceFunctionTest {
    List<Task> defaultTasks = new ArrayList<Task>();
    private int defaultActiveTaskCount = 0;
    private int defaultCompletedTaskCount = 0;

    @Autowired
    private TaskService taskSevice;

    @Autowired
    private TaskRepository taskRepository;

    @Before
    public void setUp() {
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
        if (task.getIsCompleted()) {
            defaultCompletedTaskCount++;
        } else {
            defaultActiveTaskCount++;
        }
    }

    @Test
    public void should_getAllTasks_return_tasks() {
        //given
        //when
        List<Task> tasks = taskSevice.getAllTasks();
        //then
        assertThat(tasks).isEqualTo(defaultTasks);
    }
    
    @Test
    public void should_getTasks_return_active_tasks_when_given_false() {
        //given
        boolean isCompleted = false;
        //when
        List<Task> tasks = taskSevice.getTasks(isCompleted);
        //then
        assertThat(tasks.size()).isEqualTo(defaultActiveTaskCount);
        for (Task task : tasks) {
            assertThat(task.getIsCompleted()).isFalse();
        }
    }

    @Test
    public void should_getTasks_return_completed_tasks_when_given_true() {
        //given
        boolean isCompleted = true;
        //when
        List<Task> tasks = taskSevice.getTasks(isCompleted);
        //then
        assertThat(tasks.size()).isEqualTo(defaultCompletedTaskCount);
        for (Task task : tasks) {
            assertThat(task.getIsCompleted()).isTrue();
        }
    }
}

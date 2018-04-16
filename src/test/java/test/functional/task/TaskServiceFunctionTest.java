package test.functional.task;

import com.thoughtworks.ToBdoneApplication;
import com.thoughtworks.common.constant.EnvProfile;
import com.thoughtworks.common.exception.ForbiddenException;
import com.thoughtworks.common.exception.NotFoundException;
import com.thoughtworks.task.dao.TaskRepository;
import com.thoughtworks.task.entity.Task;
import com.thoughtworks.task.mapper.TaskMapper;
import com.thoughtworks.task.model.TaskModel;
import com.thoughtworks.task.service.TaskService;
import org.junit.After;
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
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@Rollback
@Transactional
@ActiveProfiles({EnvProfile.TEST})
@SpringBootTest(classes = ToBdoneApplication.class)
public class TaskServiceFunctionTest {
    List<TaskModel> defaultTaskModels = new ArrayList<TaskModel>();
    private int defaultActiveTaskCount = 0;
    private int defaultCompletedTaskCount = 0;

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskMapper taskMapper;

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
        defaultTaskModels.add(taskMapper.map(task, TaskModel.class));
        if (task.getIsCompleted()) {
            defaultCompletedTaskCount++;
        } else {
            defaultActiveTaskCount++;
        }
    }

    @Test
    public void should_getAllTasks_return_taskModels() {
        //given
        //when
        List<TaskModel> taskModels = taskService.getAllTasks();
        //then
        assertThat(taskModels.size()).isEqualTo(defaultTaskModels.size());
        Collections.sort(defaultTaskModels);
        for (int i = 0; i < taskModels.size(); i++) {
            assertThat(taskModels.get(i).compareTo(defaultTaskModels.get(i))).isZero();
        }
    }

    @Test
    public void should_getTasks_return_active_taskModels_when_given_false() {
        //given
        boolean isCompleted = false;
        //when
        List<TaskModel> taskModels = taskService.getTasks(isCompleted);
        //then
        assertThat(taskModels.size()).isEqualTo(defaultActiveTaskCount);
        for (TaskModel taskModel : taskModels) {
            assertThat(taskModel.getIsCompleted()).isFalse();
        }
    }

    @Test
    public void should_getTasks_return_completed_taskModels_when_given_true() {
        //given
        boolean isCompleted = true;
        //when
        List<TaskModel> taskModels = taskService.getTasks(isCompleted);
        //then
        assertThat(taskModels.size()).isEqualTo(defaultCompletedTaskCount);
        for (TaskModel taskModel : taskModels) {
            assertThat(taskModel.getIsCompleted()).isTrue();
        }
    }

    @Test
    public void should_addTask_return_a_created_taskModel() {
        //given
        String name = "Go to gym";
        //when
        TaskModel taskModel = taskService.addTask(name);
        List<Task> tasks = taskRepository.findAll();
        //then
        assertThat(tasks.size()).isEqualTo(defaultTaskModels.size() + 1);
        assertThat(taskModel.getIsCompleted()).isFalse();
    }

    @Test
    public void should_deleteTask_throw_exception_when_given_nonexisting_id() {
        //given
        Long id = 0L;
        //when
        try {
            taskService.deleteTask(id);
        } catch (Exception ex) {
            //then
            assertThat(ex.getClass()).isEqualTo(NotFoundException.class);
            assertThat(ex.getMessage()).isEqualTo(getTaskIdNotFoundErrorMessage(id));
        }
    }

    @Test
    public void should_deleteTask_delete_specific_taskModel_when_given_existing_id() {
        //given
        TaskModel taskModel = defaultTaskModels.get(0);
        Long id = taskModel.getId();
        //when
        try {
            taskService.deleteTask(id);
        } catch (Exception ex) {
            assert (false);
        }
        List<Task> tasks = taskRepository.findAll();
        //then
        assertThat(tasks.size()).isEqualTo(defaultTaskModels.size() - 1);
        tasks.forEach(task -> assertThat(task.getId()).isNotEqualTo(id));
    }

    @Test
    public void should_changeTaskStatus_throw_exception_when_given_nonexisting_id() {
        //given
        Long id = 0L;
        //when
        try {
            TaskModel taskModel = taskService.changeTaskStatus(id, true);
        } catch (Exception ex) {
            //then
            assertThat(ex.getClass()).isEqualTo(NotFoundException.class);
            assertThat(ex.getMessage()).isEqualTo(getTaskIdNotFoundErrorMessage(id));
        }
    }

    @Test
    public void should_changeTaskStatus_return_completed_taskModel() {
        //given
        TaskModel taskModel = defaultTaskModels.get(1);
        Long id = taskModel.getId();
        boolean isCompleted = true;
        boolean isContained = false;
        //when
        try {
            taskModel = taskService.changeTaskStatus(id, isCompleted);
        } catch (Exception ex) {
            assert (false);
        }
        List<Task> tasks = taskRepository.findByIsCompleted(isCompleted);
        for (Task task : tasks) {
            if (task.getId().equals(id)) {
                isContained = true;
            }
        }
        //then
        assertThat(taskModel.getIsCompleted()).isTrue();
        assertThat(isContained).isTrue();
        assertThat(tasks.size()).isEqualTo(defaultCompletedTaskCount + 1);
    }

    @Test
    public void should_changeTaskStatus_return_active_taskModel() {
        //given
        TaskModel taskModel = defaultTaskModels.get(2);
        Long id = taskModel.getId();
        boolean isCompleted = false;
        boolean isContained = false;
        //when
        try {
            taskModel = taskService.changeTaskStatus(id, isCompleted);
        } catch (Exception ex) {
            assert (false);
        }
        List<Task> tasks = taskRepository.findByIsCompleted(isCompleted);
        for (Task task : tasks) {
            if (task.getId().equals(id)) {
                isContained = true;
            }
        }
        //then
        assertThat(taskModel.getIsCompleted()).isFalse();
        assertThat(isContained).isTrue();
        assertThat(tasks.size()).isEqualTo(defaultActiveTaskCount + 1);
    }

    @Test
    public void should_changeAllTaskStatus_return_completed_taskModels() {
        //given
        List<TaskModel> taskModels = null;
        boolean isCompleted = true;
        //when
        try {
            taskModels = taskService.changeAllTaskStatus(isCompleted);
        } catch (Exception ex) {
            assert (false);
        }
        List<Task> tasks = taskRepository.findAll();
        //then
        assertThat(tasks.size()).isEqualTo(defaultTaskModels.size());
        assertThat(taskModels.size()).isEqualTo(defaultTaskModels.size());
        for (TaskModel taskModel : taskModels) {
            assertThat(taskModel.getIsCompleted()).isTrue();
        }
        for (Task task : tasks) {
            assertThat(task.getIsCompleted()).isTrue();
        }
    }

    @Test
    public void should_changeAllTaskStatus_return_active_taskModels() {
        //given
        List<TaskModel> taskModels = null;
        boolean isCompleted = false;
        //when
        try {
            taskModels = taskService.changeAllTaskStatus(isCompleted);
        } catch (Exception ex) {
            assert (false);
        }
        List<Task> tasks = taskRepository.findAll();
        //then
        assertThat(tasks.size()).isEqualTo(defaultTaskModels.size());
        assertThat(taskModels.size()).isEqualTo(defaultTaskModels.size());
        for (TaskModel taskModel : taskModels) {
            assertThat(taskModel.getIsCompleted()).isFalse();
        }
        for (Task task : tasks) {
            assertThat(task.getIsCompleted()).isFalse();
        }
    }

    @Test
    public void should_deleteTasks_throw_exception_when_given_false() {
        //given
        boolean isCompleted = false;
        //when
        try {
            taskService.deleteTasks(isCompleted);
        } catch (Exception ex) {
            //then
            assertThat(ex.getClass()).isEqualTo(ForbiddenException.class);
            assertThat(ex.getMessage()).isEqualTo(getDeleteForbiddenErrorMessage());
        }
    }

    @Test
    public void should_deleteTasks_return_true_when_given_true() {
        //given
        boolean isCompleted = true;
        //when
        try {
            taskService.deleteTasks(isCompleted);
        } catch (Exception ex) {
            assert (false);
        }
        List<Task> completedTasks = taskRepository.findByIsCompleted(true);
        //then
        assertThat(completedTasks.size()).isZero();
    }

    private String getTaskIdNotFoundErrorMessage(Long id) {
        return "Task not found with id: " + id;
    }

    private String getDeleteForbiddenErrorMessage() {
        return "Active tasks cannot be removed";
    }
}

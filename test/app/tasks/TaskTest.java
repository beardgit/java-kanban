package app.tasks;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TaskTest {

    @Test
    void testEqualityById() {
        Task task1 = new Task(1, "Task 1", "Description");
        Task task2 = new Task(1, "Task 2", "Another Description");

        Assertions.assertEquals(task1, task2); // Равны по id
        Assertions.assertNotSame(task1, task2); // Но это разные объекты
    }

}
package app.tasks;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SubtaskTest {

    @Test
    void testEqualityById() {
        Epic epic = new Epic("Epic Name", "Description");
        Subtask subtask1 = new Subtask("Subtask 1", "Description", epic.getId());
        Subtask subtask2 = new Subtask("Subtask 2", "Another Description", epic.getId());

        subtask1.setId(1);
        subtask2.setId(1);

        assertEquals(subtask1, subtask2); // Равны по id
        assertNotSame(subtask1, subtask2); // Но это разные объекты
    }
}
package app.tasks;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EpicTest {

    @Test
    void testEqualityById() {
        Epic epic1 = new Epic("Epic 1", "Description");
        Epic epic2 = new Epic("Epic 2", "Another Description");

        epic1.setId(1);
        epic2.setId(1);

        assertEquals(epic1, epic2); // Равны по id
        assertNotSame(epic1, epic2); // Но это разные объекты
    }
}
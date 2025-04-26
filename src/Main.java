
import app.manager.FileBackedTaskManager;
import app.manager.Managers;
import app.tasks.Epic;
import app.tasks.Subtask;
import app.tasks.Task;

import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {

        // Создаем временный файл для сохранения данных
        Path tempFilePath = Path.of("tasks.csv");

        // Создаем экземпляр FileBackedTaskManager
        FileBackedTaskManager taskManager = new FileBackedTaskManager(Managers.getDefaultHistory(), tempFilePath);

        // Добавляем задачи
        Task task = new Task("Пойти в магазин", "Купить продукты для ужина");
        taskManager.appendTask(task);

        Epic epic = new Epic("Уборка дома", "Провести генеральную уборку квартиры");
        taskManager.appendEpic(epic);

        Subtask subtask1 = new Subtask("Почистить рыбу", "Подготовить рыбу для приготовления ужина", epic.getId());
        taskManager.appendSubtask(subtask1);

        Subtask subtask2 = new Subtask("Пропылесосить ковёр", "Пропылесосить все комнаты", epic.getId());
        taskManager.appendSubtask(subtask2);

        System.out.println("Initial tasks:");
        printTasks(taskManager);

        // Сохраняем данные в файл
        taskManager.save();
        System.out.println("Data saved to file.");

        // Загружаем данные из файла
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFilePath.toFile());
        System.out.println("Data loaded from file:");

        // Проверяем загруженные задачи
        printTasks(loadedManager);

        // Удаляем задачу и подзадачу
        loadedManager.deleteTask(task.getId());
        loadedManager.deleteSubtask(subtask1.getId());

        System.out.println("After deletion:");
        printTasks(loadedManager);

        // Обновляем эпик
        Epic updatedEpic = loadedManager.getEpicById(epic.getId());
        if (updatedEpic != null) {
            updatedEpic.setName("Генеральная уборка дома");
            loadedManager.updateEpic(updatedEpic);
        }

        System.out.println("After update:");
        printTasks(loadedManager);

        // Очищаем все задачи
        loadedManager.clearTasks();
        loadedManager.clearEpics();
        loadedManager.clearSubtasks();

        System.out.println("After clearing all tasks:");
        printTasks(loadedManager);
    }

    private static void printTasks(FileBackedTaskManager taskManager) {
        System.out.println("Tasks: " + taskManager.getAllTasks());
        System.out.println("Epics: " + taskManager.getAllEpic());
        System.out.println("Subtasks: " + taskManager.getAllSubtasks());
        System.out.println();
    }
}


import app.enumeration.StatusTasks;
import app.manager.FileBackedTaskManager;
import app.manager.Managers;
import app.tasks.Epic;
import app.tasks.Subtask;
import app.tasks.Task;

import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;

public class Main {
    public static void main(String[] args) {
        // Создаем менеджер задач с историей просмотров
        FileBackedTaskManager taskManager = new FileBackedTaskManager(Managers.getDefaultHistory(), Path.of("tasks.csv"));

        // Создаем задачу
        Task task1 = new Task("Задача 1", "Описание задачи 1",
                Instant.parse("2023-10-01T10:00:00Z"), Duration.ofMinutes(30));
        taskManager.appendTask(task1);

        // Создаем эпик
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        taskManager.appendEpic(epic1);

        // Создаем подзадачи для эпика
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", epic1.getId(),
                Instant.parse("2023-10-01T11:00:00Z"), Duration.ofMinutes(20));
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", epic1.getId(),
                Instant.parse("2023-10-01T11:30:00Z"), Duration.ofMinutes(25));
        taskManager.appendSubtask(subtask1);
        taskManager.appendSubtask(subtask2);

        // Получаем задачи и эпики по ID
        System.out.println("Получение задачи по ID:");
        System.out.println(taskManager.getTaskById(task1.getId()));
        System.out.println("Получение эпика по ID:");
        System.out.println(taskManager.getEpicById(epic1.getId()));

        // Обновляем статус задачи
        task1.setStatus(StatusTasks.DONE);
        taskManager.updateTask(task1);

        // Проверяем статус эпика
        System.out.println("Статус эпика после обновления подзадач:");
        System.out.println(taskManager.getEpicById(epic1.getId()).getStatus());

        // Получаем список всех задач
        System.out.println("Все задачи:");
        taskManager.getAllTasks().forEach(System.out::println);

        // Получаем список всех эпиков
        System.out.println("Все эпики:");
        taskManager.getAllEpic().forEach(System.out::println);

        // Получаем список всех подзадач
        System.out.println("Все подзадачи:");
        taskManager.getAllSubtasks().forEach(System.out::println);

        // Получаем историю просмотров
        System.out.println("История просмотров:");
        taskManager.getHistory().forEach(System.out::println);

        // Получаем список задач по приоритетам
        System.out.println("Задачи по приоритетам:");
        taskManager.getPrioritizedTasks().forEach(System.out::println);

        // Удаляем задачу
        System.out.println("Удаление задачи:");
        Task removedTask = taskManager.deleteTask(task1.getId());
        if (removedTask != null) {
            System.out.println("Удалена задача: " + removedTask);
        } else {
            System.out.println("Задача с указанным ID не найдена.");
        }

        // Сохраняем состояние в файл
        taskManager.save();

        // Загружаем состояние из файла
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(Path.of("tasks.csv").toFile());
        System.out.println("Загруженные задачи:");
        loadedManager.getAllTasks().forEach(System.out::println);
        System.out.println("Загруженные эпики:");
        loadedManager.getAllEpic().forEach(System.out::println);
        System.out.println("Загруженные подзадачи:");
        loadedManager.getAllSubtasks().forEach(System.out::println);
    }
}
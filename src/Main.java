import app.manager.HistoryManager;

import app.manager.Managers;
import app.manager.TaskManager;
import app.tasks.Epic;
import app.tasks.Subtask;
import app.tasks.Task;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();

        // Создание задач
        Task task1 = taskManager.appendTask(new Task("Task 1", "Description"));
        Task task2 = taskManager.appendTask(new Task("Task 2", "Description"));
        Epic epic3 = taskManager.appendEpic(new Epic("Epic 3", "Description"));
        Epic epic4 = taskManager.appendEpic(new Epic("Epic 4", "Description"));
        Epic epic5 = taskManager.appendEpic(new Epic("Epic 5", "Description"));
        Epic epic6 = taskManager.appendEpic(new Epic("Epic 6", "Description"));
        Epic epic7 = taskManager.appendEpic(new Epic("Epic 7", "Description"));
        Task task8 = taskManager.appendTask(new Task("Task 8", "Description"));
        Task task9 = taskManager.appendTask(new Task("Task 9", "Description"));
        Task task10 = taskManager.appendTask(new Task("Task 10", "Description"));
        Task task11 = taskManager.appendTask(new Task("Task 11", "Description"));
        Epic epic12 = taskManager.appendEpic(new Epic("Epic 12", "Description"));
        Subtask subtask1 = taskManager.appendSubtask(new Subtask("Subtask 1", "Description", epic12));

        // Получение задач (добавление в историю)
        taskManager.getTaskById(task1.getId());
        taskManager.getEpicById(epic3.getId());
        taskManager.getEpicById(epic4.getId());
        taskManager.getEpicById(epic5.getId());
        taskManager.getEpicById(epic7.getId());
        taskManager.getTaskById(task8.getId());
        taskManager.getTaskById(task9.getId());
        taskManager.getTaskById(task10.getId());
        taskManager.getTaskById(task11.getId());
        taskManager.getEpicById(epic12.getId());
        taskManager.getSubtaskById(subtask1.getId());


        System.out.println("История:");
        for (Task task : historyManager.getHistory()) {
//            История должна быть пуста. не к истории taskManager
            System.out.println(task);
        }
        System.out.println(taskManager.getHistory());


    }
}


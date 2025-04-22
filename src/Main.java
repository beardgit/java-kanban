
import app.manager.Managers;
import app.manager.TaskManager;
import app.tasks.Epic;
import app.tasks.Subtask;
import app.tasks.Task;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = Managers.getDefault();

        Task task1 = taskManager.appendTask(new Task("Задача 1", "Описание"));
        Task task2 = taskManager.appendTask(new Task("Задача 2", "Описание"));

        // Создаем Эпик с подзадачами
        Epic epicWithSubtasks = taskManager.appendEpic(new Epic("Эпик с подзадачами", "Описание"));
        Subtask subtask1 = taskManager.appendSubtask(new Subtask("Подзадача 1", "Описание", epicWithSubtasks));
        Subtask subtask2 = taskManager.appendSubtask(new Subtask("Подзадача 2", "Описание", epicWithSubtasks));
        Subtask subtask3 = taskManager.appendSubtask(new Subtask("Подзадача 3", "Описание", epicWithSubtasks));

        Epic epicWithoutSubtasks = taskManager.appendEpic(new Epic("Эпик без подзадач", "Описание"));

        System.out.println("Запросы к задачам:");
        taskManager.getTaskById(task1.getId());
        taskManager.getEpicById(epicWithSubtasks.getId());
        taskManager.getSubtaskById(subtask1.getId());
        taskManager.getSubtaskById(subtask2.getId());
        taskManager.getTaskById(task2.getId());
        taskManager.getEpicById(epicWithoutSubtasks.getId());
        taskManager.getSubtaskById(subtask3.getId());

        System.out.println("История:");
        printHistory(taskManager);
        System.out.println("\nУдаляем задачу Задача 1:");
        taskManager.deleteTask(task1.getId());
        System.out.println("История:");
        printHistory(taskManager);

        System.out.println("\nУдаляем эпик с подзадачами:");
        taskManager.deleteEpic(epicWithSubtasks.getId());
        System.out.println("История:");

        printHistory(taskManager);
    }

    private static void printHistory(TaskManager taskManager) {
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }
    }
}


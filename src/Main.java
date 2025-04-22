
import app.manager.Managers;
import app.manager.TaskManager;
import app.tasks.Epic;
import app.tasks.Subtask;
import app.tasks.Task;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = Managers.getDefault();

        Task task1 = taskManager.appendTask(new Task("Task 1", "Description"));
        Task task2 = taskManager.appendTask(new Task("Task 2", "Description"));

        Epic epicWithSubtasks = taskManager.appendEpic(new Epic("Epic with Subtasks", "Description"));
        Subtask subtask1 = taskManager.appendSubtask(new Subtask("Subtask 1", "Description", epicWithSubtasks));
        Subtask subtask2 = taskManager.appendSubtask(new Subtask("Subtask 2", "Description", epicWithSubtasks));
        Subtask subtask3 = taskManager.appendSubtask(new Subtask("Subtask 3", "Description", epicWithSubtasks));

        Epic epicWithoutSubtasks = taskManager.appendEpic(new Epic("Epic without Subtasks", "Description"));

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
        System.out.println("\nУдаляем задачу Task 1:");
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


import app.manager.TaskManager;
import app.tasks.Epic;
import app.tasks.StatusTasks;
import app.tasks.Subtask;
import app.tasks.Task;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = new TaskManager();

        Task taskOne = new Task("Написать этот ToDo", "ох-ох-ох");
        Task taskSecond = new Task("Подарки", "закупить подарки семье");

        manager.appendTask(taskOne);
        manager.appendTask(taskSecond);

        Epic epicOne = new Epic("Epic", "descrription ");
        Subtask subtask1 = new Subtask("subtask 1", "descrription 1", epicOne);
        Subtask subtask2 = new Subtask("subtask 2", "descrription 2", epicOne);
        manager.appendSubtask(subtask1);
        manager.appendSubtask(subtask2);
        manager.appendEpic(epicOne);


        System.out.println("Subtasks in manager -" + manager.getAllSubtasks().size());
        System.out.println("Waiting NEW, result -" + epicOne.getStatus());
        subtask1.setStatus(StatusTasks.IN_PROGRESS);
        System.out.println("Waiting IN PROGRES, result -" + epicOne.getStatus());
        subtask1.setStatus(StatusTasks.DONE);
        System.out.println("Waiting IN PROGRES, result -" + epicOne.getStatus());
        subtask2.setStatus(StatusTasks.DONE);
        System.out.println("Waiting DONE, result -" + epicOne.getStatus());


        System.out.println("Проверка коллизии подзадач" + manager.getAllSubtasks());
        Epic replaceEpic = new Epic("Updated epic", "description");
        replaceEpic.setId(epicOne.getId());
        replaceEpic.addSubtask(subtask1);
        manager.updateEpic(replaceEpic);
        System.out.println("Проверка коллизии подзадач. Должна исчезнуть сабтаск 2, так как изменен эпик" + manager.getAllSubtasks());
        System.out.println("Epic subtasks:" + epicOne.getListSubtasks());

        manager.deleteEpic(epicOne.getId());
        System.out.println("Subtasks in manager -" + manager.getAllSubtasks().size());
        Epic epicSecond = new Epic("Epic ", "descrription ");
        Subtask subtask3 = new Subtask("subtask 1", "descrription 1", epicSecond);
        manager.appendEpic(epicSecond);
        manager.appendSubtask(subtask3);

        System.out.println("Все task:");

        for (Task task : manager.getAllTasks()) {
            System.out.println(task);

        }


        System.out.println("Все epic:");
        for (Epic epic : manager.getAllEpic()) {
            System.out.println("epic:");
            System.out.println(epic);
            System.out.println("subtask: ");

            for (Subtask subtask : epic.getListSubtasks()) {
                System.out.println(subtask);
            }
        }

        taskOne.setStatus(StatusTasks.IN_PROGRESS);
        taskSecond.setStatus(StatusTasks.DONE);
        subtask1.setStatus(StatusTasks.DONE);
        subtask2.setStatus(StatusTasks.IN_PROGRESS);


        System.out.println("Все task:");

        for (Task task : manager.getAllTasks()) {
            System.out.println(task);

        }


        System.out.println("Все epic:");
        for (Epic epic : manager.getAllEpic()) {
            System.out.println("epic:");
            System.out.println(epic);
            System.out.println("subtask: ");

            for (Subtask subtask : epic.getListSubtasks()) {
                System.out.println(subtask);
            }
        }


        System.out.println(manager.deleteTask(1));

    }

}

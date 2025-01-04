import app.manager.TaskManager;
import app.tasks.Epic;
import app.tasks.StatusTasks;
import app.tasks.Subtask;
import app.tasks.Task;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = new TaskManager();

        Task taskOne = new Task("Написать этот ToDo", "ох-ох-ох");
        Task taskSecond = new Task("Подарки" , "закупить подарки семье");

        manager.appendTask(taskOne);
        manager.appendTask(taskSecond);

        Epic epicOne = new Epic("Epic ", "descrription ");
        Subtask subtask1 = new Subtask("subtask 1", "descrription 1");
        Subtask subtask2 = new Subtask("subtask 2", "descrription 2");

        manager.appendEpic(epicOne);
        manager.appendSubtask(subtask1, epicOne);
        manager.appendSubtask(subtask2, epicOne);

        Epic epicSecond = new Epic("Epic ", "descrription ");
        Subtask subtask3 = new Subtask("subtask 1", "descrription 1");
        manager.appendEpic(epicSecond);
        manager.appendSubtask(subtask3, epicSecond);

        System.out.println("Все task:");

        for(Task task : manager.getAllTasks()){
            System.out.println( task);

        }


        System.out.println("Все epic:");
        for(Epic epic : manager.getAllEpic()){
            System.out.println("epic:");
            System.out.println( epic);
            System.out.println("subtask: ");

            for (Subtask subtask : epic.getEpicList()){
                System.out.println(subtask);
            }
        }

        taskOne.setStatus(StatusTasks.IN_PROGRESS);
        taskSecond.setStatus(StatusTasks.DONE);
        subtask1.setStatus(StatusTasks.DONE);
        subtask2.setStatus(StatusTasks.IN_PROGRESS);



        System.out.println("Все task:");

        for(Task task : manager.getAllTasks()){
            System.out.println( task);

        }


        System.out.println("Все epic:");
        for(Epic epic : manager.getAllEpic()){
            System.out.println("epic:");
            System.out.println( epic);
            System.out.println("subtask: ");

            for (Subtask subtask : epic.getEpicList()){
                System.out.println(subtask);
            }
        }


        System.out.println(manager.deleteTask(1));
        System.out.println(manager.deleteEpic(3));

    }

}

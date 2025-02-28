import app.manager.HistoryManager;
import app.manager.InMemoryTaskManager;
import app.manager.Manager;
import app.manager.TaskManager;
import app.tasks.Epic;
import app.tasks.StatusTasks;
import app.tasks.Subtask;
import app.tasks.Task;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager  = Manager.getDefault();
        HistoryManager historyManager = Manager.getDefaultHistory();

    }

}


package app.manager;

import app.tasks.Task;

import java.util.List;

public interface HistoryManager {

    List<Task> getHistory();

    void addToHistory(Task task);

    void removeFromHistory(Task task);

}

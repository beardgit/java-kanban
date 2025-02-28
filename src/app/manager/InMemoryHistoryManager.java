package app.manager;

import app.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    //Создаем для лист для записи истории !TODO
    private ArrayList<Task> history = new ArrayList<>();

    @Override
    public void addToHistory(Task task) {
        if (history.size() == 10) {
            history.remove(0);
        }
        history.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return List.of();
    }
}

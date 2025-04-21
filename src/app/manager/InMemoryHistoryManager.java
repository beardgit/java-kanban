package app.manager;

import app.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private List<Task> history = new ArrayList<>();

    private Map<Integer, Task> historyMap = new HashMap<>();
    private Node head;
    private Node tail;


    @Override
    public void addToHistory(Task task) {
        if (history.size() == 10) {
            history.remove(0);
        }
        history.add(task);
    }

//    TODO
    @Override
    public void removeToHistory(Task task) {
//        !TODO
        System.out.println("Удаляю!");
    }

    @Override
    public List<Task> getHistory() {
        System.out.println(history);
        return new ArrayList<>(history);
    }

}

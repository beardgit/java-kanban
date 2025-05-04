package app.manager;

import app.exception.TaskNitFoundException;
import app.tasks.Epic;
import app.tasks.Subtask;
import app.tasks.Task;

import java.time.Instant;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();

    protected static Integer countId = 0;

    private final HistoryManager historyManager;

    private final Set<Task> prioritizedTasks = new TreeSet<>(
            (task1, task2) -> {
                // Сравнение по startTime с учетом null
                if (task1.getStartTime() == null && task2.getStartTime() == null) {
                    return 0;
                } else if (task1.getStartTime() == null) {
                    return 1; // task1 считается "больше" (null в конце)
                } else if (task2.getStartTime() == null) {
                    return -1; // task2 считается "больше" (null в конце)
                } else {
                    int startTimeComparison = task1.getStartTime().compareTo(task2.getStartTime());
                    if (startTimeComparison != 0) {
                        return startTimeComparison;
                    }
                }

                // Если startTime одинаковые, сравниваем по id
                return Integer.compare(task1.getId(), task2.getId());
            }
    );

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    private int nextId() {
        return ++countId;
    }

    private boolean hasIntersections(Task task) {
        if (task.getStartTime() == null || task.getDuration() == null) return false;
        Instant newStart = task.getStartTime();
        Instant newEnd = task.getEndTime();

        return prioritizedTasks.stream()
                .filter(existing -> !Objects.equals(existing.getId(), task.getId()))
                .filter(existing -> existing.getStartTime() != null && existing.getDuration() != null)
                .anyMatch(existing -> {
                    Instant existStart = existing.getStartTime();
                    Instant existEnd = existing.getEndTime();
                    return existStart.isBefore(newEnd) && newStart.isBefore(existEnd);
                });
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getAllEpic() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public boolean clearTasks() {
        for (Task task : tasks.values()) prioritizedTasks.remove(task);
        tasks.clear();
        return true;
    }

    @Override
    public boolean clearEpics() {
        for (Epic epic : epics.values()) prioritizedTasks.remove(epic);
        epics.clear();
        clearSubtasks();
        return true;
    }

    @Override
    public boolean clearSubtasks() {
        for (Subtask sub : subtasks.values()) prioritizedTasks.remove(sub);
        subtasks.clear();
        for (Epic epic : epics.values()) epic.clearSubtasks();
        return true;
    }

    @Override
    public Task getTaskById(Integer id) {
        Task task = tasks.get(id);
        if (task == null) {
            String errorMessage = String.format("задача с id %d не найдена", id);
            throw new TaskNitFoundException(errorMessage);
        }
        historyManager.addToHistory(task);
        return task;
    }

    @Override
    public Epic getEpicById(Integer id) {
        Epic epic = epics.get(id);
        historyManager.addToHistory(epic);
        return epic;
    }

    @Override
    public Subtask getSubtaskById(Integer id) {
        Subtask subtask = subtasks.get(id);
        historyManager.addToHistory(subtask);
        return subtask;
    }

    @Override
    public Task appendTask(Task newTask) {
        if (hasIntersections(newTask)) throw new IllegalArgumentException("Задача пересекается с другой");
        int id = nextId();
        newTask.setId(id);
        tasks.put(id, newTask);
        prioritizedTasks.add(newTask);
        return newTask;
    }

    @Override
    public Epic appendEpic(Epic newEpic) {
        int id = nextId();
        newEpic.setId(id);
        epics.put(id, newEpic);
        prioritizedTasks.add(newEpic);
        return newEpic;
    }

    @Override
    public Subtask appendSubtask(Subtask newSubtask) {
        if (hasIntersections(newSubtask)) throw new IllegalArgumentException("Пересечение подзадачи");
        int id = nextId();
        newSubtask.setId(id);
        subtasks.put(id, newSubtask);
        Epic epic = epics.get(newSubtask.getEpicId());
        if (epic != null) {
            epic.addSubtask(newSubtask);
        }
        prioritizedTasks.add(newSubtask);
        return newSubtask;
    }

    @Override
    public Task updateTask(Task updatedTask) {
        Task old = tasks.get(updatedTask.getId());
        prioritizedTasks.remove(old);
        if (hasIntersections(updatedTask)) {
            prioritizedTasks.add(old);
            throw new IllegalArgumentException("Обновление создаёт пересечение");
        }
        tasks.put(updatedTask.getId(), updatedTask);
        prioritizedTasks.add(updatedTask);
        return updatedTask;
    }

    @Override
    public Epic updateEpic(Epic updatedEpic) {
        Epic oldEpic = epics.get(updatedEpic.getId());
        prioritizedTasks.remove(oldEpic);
        epics.put(updatedEpic.getId(), updatedEpic);
        prioritizedTasks.add(updatedEpic);
        return updatedEpic;
    }

    @Override
    public Subtask updateSubtask(Subtask updatedSubtask) {
        Subtask old = subtasks.get(updatedSubtask.getId());
        prioritizedTasks.remove(old);
        if (hasIntersections(updatedSubtask)) {
            prioritizedTasks.add(old);
            throw new IllegalArgumentException("Обновление подзадачи вызывает пересечение");
        }
        subtasks.put(updatedSubtask.getId(), updatedSubtask);
        Epic epic = epics.get(updatedSubtask.getEpicId());
        if (epic != null) {
            epic.removeSubtaskById(updatedSubtask.getId());
            epic.addSubtask(updatedSubtask);
        }
        prioritizedTasks.add(updatedSubtask);
        return updatedSubtask;
    }

    @Override
    public Task deleteTask(Integer id) {
        Task task = tasks.remove(id);
        if (task != null) {
            prioritizedTasks.remove(task);
            historyManager.removeFromHistory(task);
        }
        return task;
    }

    @Override
    public Epic deleteEpic(Integer id) {
        Epic epic = epics.remove(id);
        if (epic != null) {
            prioritizedTasks.remove(epic);
            for (Subtask sub : new ArrayList<>(subtasks.values())) {
                if (Objects.equals(sub.getEpicId(), id)) {
                    deleteSubtask(sub.getId());
                }
            }
            historyManager.removeFromHistory(epic);
        }
        return epic;
    }

    @Override
    public void deleteSubtask(Integer id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.removeSubtaskById(id);
            }
            prioritizedTasks.remove(subtask);
            historyManager.removeFromHistory(subtask);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }
}

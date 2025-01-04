package app.manager;

import app.tasks.Epic;
import app.tasks.StatusTasks;
import app.tasks.Subtask;
import app.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {

    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private static Integer countId = 0;

    private int nextId() {
        return ++countId;
    }

    public Task appendTask(Task newTask) {
        int newId = nextId();
        newTask.setId(newId);
        newTask.setStatus(StatusTasks.NEW);
        tasks.put(newTask.getId(), newTask);
        return tasks.get(newTask.getId());

    }

    public Task appendTask(Task newTask, int id) {
        newTask.setId(id);
        tasks.put(id, newTask);
        return tasks.get(id);
    }

    public Task updateTask(Task newTask) {
        Integer taskId = newTask.getId();

        if (taskId != null) {
            Task task = tasks.get(taskId);
            task.setStatus(newTask.getStatus());
            task.setDescription(newTask.getDescription());
            task.setName(newTask.getName());
            return task;
        } else {
            return null;
        }

    }

    public Task deleteTask(Integer id) {
        return tasks.remove(id);
    }

    public boolean clearTasks() {
        if (!tasks.isEmpty()) {
            tasks.clear();
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public Task findTaskById(Integer id) {
        return tasks.get(id);
    }

    public Epic appendEpic(Epic newEpic) {
        int newId = nextId();
        newEpic.setId(newId);
        newEpic.setStatus(StatusTasks.NEW);
        epics.put(newEpic.getId(), newEpic);
        return epics.get(newEpic.getId());
    }

    public Epic updateEpic(Epic epic) {

        if (epic.getEpicList().isEmpty()) {
            epic.setStatus(StatusTasks.NEW);
            return epic;
        }

        StatusTasks status = StatusTasks.NEW;
        boolean statusInProgress = false;
        boolean statusAllDone = true;

        for (Subtask subtask : epic.getEpicList()) {
            if (subtask.getStatus() != StatusTasks.NEW) {
                statusAllDone = false;
            }
            if (subtask.getStatus() == StatusTasks.IN_PROGRESS) {
                statusInProgress = true;
            }
            if (subtask.getStatus() != StatusTasks.DONE) {
                statusAllDone = false;
            }
        }

        if (statusAllDone) {
            status = StatusTasks.DONE;
        } else if (statusInProgress) {
            status = StatusTasks.IN_PROGRESS;
        }

        epic.setStatus(status);
        return epic;
    }

    public Epic deleteEpic(Integer id) {
        return epics.remove(id);
    }

    public ArrayList<Epic> getAllEpic() {
        return new ArrayList<>(epics.values());
    }

    public Epic findEpicById(Integer id) {
        return epics.get(id);
    }


    public Subtask appendSubtask(Subtask subtask, Epic epic) {
        int newId = nextId();
        subtask.setId(newId);
        subtask.setStatus(StatusTasks.NEW);
        subtasks.put(newId, subtask);
        epic.addSubtask(subtask);
        subtask.setEpicId(epic.getId());
        updateEpic(epic);
        return subtasks.get(newId);
    }

    public Subtask updateSubtask(Subtask newSubtask) {
        Integer epicId = newSubtask.getId();
        Subtask subtask = subtasks.get(epicId);
        if (subtask != null) {
            subtask.setStatus(newSubtask.getStatus());
            subtask.setName(newSubtask.getName());
            subtask.setDescription(newSubtask.getDescription());

            updateEpic(epics.get(epicId));
            return subtask;
        }
        return null;
    }

    public Subtask deleteSubtask(Integer id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            int epicId = subtask.getEpicId();
            boolean isRemoveSubtaskToEpic = epics.get(epicId).removeSubtask(id);
            if (isRemoveSubtaskToEpic) {
                updateEpic(epics.get(epicId));
            }
            return subtasks.remove(id);
        }
        return null;
    }

    public ArrayList<Subtask> findAllSubtask() {
        return new ArrayList<>(subtasks.values());
    }

    public Subtask findSubtaskById(Integer id) {
        return subtasks.get(id);
    }

}

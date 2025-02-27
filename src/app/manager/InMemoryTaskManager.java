package app.manager;

import app.tasks.Epic;
import app.tasks.Subtask;
import app.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private static Integer countId = 0;

    private int nextId() {
        return ++countId;
    }

    //  блок получения списка всех задач
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
        return new ArrayList<Subtask>(subtasks.values());
    }


    //    Блок удаления всех задач
    @Override
    public boolean clearTasks() {
        if (!tasks.isEmpty()) {
            tasks.clear();
            return true;
        }
        return false;

    }

    @Override
    public boolean clearEpic() {
        return false;
    }
    @Override
    public boolean clearEpics() {
        if (!epics.isEmpty()) {
            epics.clear();
            clearSubtasks();
            return true;
        }
        return false;

    }
    @Override
    public boolean clearSubtasks() {
        if (!subtasks.isEmpty()) {
            subtasks.clear();
            return true;
        }
        return false;
    }


    // Блок Получения задач по id
@Override
    public Task getTaskById(Integer id) {
        return tasks.get(id);
    }
@Override
    public Subtask getSubtaskById(Integer id) {
        return subtasks.get(id);
    }
@Override
    public Epic getEpicById(Integer id) {
        return epics.get(id);
    }

    // Блок добавления задач
    @Override
    public Task appendTask(Task newTask) {
        int newId = nextId();
        newTask.setId(newId);
        tasks.put(newTask.getId(), newTask);
        return newTask;

    }
@Override
    public Epic appendEpic(Epic newEpic) {
        int newId = nextId();
        newEpic.setId(newId);
        epics.put(newEpic.getId(), newEpic);
        return newEpic;
    }
@Override
    public Subtask appendSubtask(Subtask newSubtask) {
        int newId = nextId();
        newSubtask.setId(newId);
        subtasks.put(newId, newSubtask);
        return newSubtask;
    }


// Блок обновления задач
@Override
    public Task updateTask(Task updatedTask) {
        return tasks.put(updatedTask.getId(), updatedTask);
    }
@Override
    public Epic updateEpic(Epic updatedEpic) {
        //Для избежания коллизий, если новый эпик содержит подзадач старого эпика, мы проверяем соответствия
        Epic existEpic = epics.get(updatedEpic.getId());
        ArrayList<Subtask> tmpSubtasks = new ArrayList<>(existEpic.getListSubtasks());
        tmpSubtasks.removeAll(updatedEpic.getListSubtasks());

        for (Subtask subtask : tmpSubtasks) {
            deleteSubtask(subtask.getId());
        }

        return epics.put(updatedEpic.getId(), updatedEpic);
    }
@Override
    public Subtask updateSubtask(Subtask updatedSubtask) {
        return subtasks.put(updatedSubtask.getId(), updatedSubtask);
    }


//    Блок удаления по id
@Override
    public Task deleteTask(Integer id) {
        return tasks.remove(id);
    }
@Override
    public Epic deleteEpic(Integer id) {
        Epic epic = epics.get(id);
        for (Subtask subtask : epic.getListSubtasks()) {
            subtasks.remove(subtask.getId());
        }
        epic.clearSubtasks();
        return epics.remove(id);
    }
@Override
    public void deleteSubtask(Integer id) {
        Subtask subtask = subtasks.get(id);
        subtask.getEpic().removeSubtask(subtask);
        subtasks.remove(id);
    }

}

package app.manager;

import app.tasks.Epic;
import app.tasks.Subtask;
import app.tasks.Task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class TaskManager {

    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private static Integer countId = 0;

    private int nextId() {
        return ++countId;
    }

    //  блок получения списка всех задач

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> getAllEpic() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<Subtask>(subtasks.values());
    }


    //    Блок удаления всех задач

    public boolean clearTasks() {
        if (!tasks.isEmpty()) {
            tasks.clear();
            return true;
        }
        return false;

    }

    public boolean clearEpics() {
        if (!epics.isEmpty()) {
            epics.clear();
            clearSubtasks();
            return true;
        }
        return false;

    }

    public boolean clearSubtasks() {
        if (!subtasks.isEmpty()) {
            subtasks.clear();
            return true;
        }
        return false;
    }


    // Блок Получения задач по id

    public Task getTaskById(Integer id) {
        return tasks.get(id);
    }

    public Subtask getSubtaskById(Integer id) {
        return subtasks.get(id);
    }

    public Epic getEpicById(Integer id) {
        return epics.get(id);
    }

    // Блок добавления задач
    public Task appendTask(Task newTask) {
        int newId = nextId();
        newTask.setId(newId);
        tasks.put(newTask.getId(), newTask);
        return newTask;

    }

    public Epic appendEpic(Epic newEpic) {
        int newId = nextId();
        newEpic.setId(newId);
        epics.put(newEpic.getId(), newEpic);
        return newEpic;
    }

    public Subtask appendSubtask(Subtask newSubtask) {
        int newId = nextId();
        newSubtask.setId(newId);
        subtasks.put(newId, newSubtask);
        return newSubtask;
    }


// Блок обновления задач

    public Task updateTask(Task updatedTask) {
        return tasks.put(updatedTask.getId(), updatedTask);
    }

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

    public Subtask updateSubtask(Subtask updatedSubtask) {
        return subtasks.put(updatedSubtask.getId(), updatedSubtask);
    }


//    Блок удаления по id

    public Task deleteTask(Integer id) {
        return tasks.remove(id);
    }

    public Epic deleteEpic(Integer id) {
        Epic epic = epics.get(id);
        for (Subtask subtask : epic.getListSubtasks()) {
            subtasks.remove(subtask.getId());
        }
        epic.clearSubtasks();
        return epics.remove(id);
    }

    public void deleteSubtask(Integer id) {
        Subtask subtask = subtasks.get(id);
        subtask.getEpic().removeSubtask(subtask);
        subtasks.remove(id);
    }

}

package app.manager;

import app.tasks.Epic;
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
        epics.put(newEpic.getId(), newEpic);
        return epics.get(newEpic.getId());
    }

    public Epic updateEpic(Epic epic, Integer index){
        return epics.put(index, epic);
    }



    public Epic deleteEpic(Integer id) {
        deleteSubtask(epics.get(id));
        return epics.remove(id);
//        Удалить все подзадаи из Мар
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
        subtasks.put(newId, subtask);
        epic.addSubtask(subtask);
        subtask.setEpic(epic);
        return subtasks.get(newId);
    }

    public Subtask updateSubtask(Subtask newSubtask) {
        Integer epicId = newSubtask.getId();
        Subtask subtask = subtasks.get(epicId);
        if (subtask != null) {
            subtask.setStatus(newSubtask.getStatus());
            subtask.setName(newSubtask.getName());
            subtask.setDescription(newSubtask.getDescription());

            return subtask;
        }
        return null;
    }

    public Subtask deleteSubtask(Epic epic) {
        ArrayList<Subtask> epicSubtasks = epic.getSubtasks();
        for(Subtask subtask: epicSubtasks)
        {
            subtasks.remove(subtask.getId());
        }
        return null;
    }


    public void clearSubtasks() {
        subtasks.clear();
    }

    public void clearEpics()
    {
        for(Epic epic:epics.values())
        {
            this.deleteEpic(epic.getId());
        }
    }

    public ArrayList<Subtask> getSubtasksByEpicID(Integer id)
    {
        return epics.get(id).getSubtasks();
    }

    public ArrayList<Subtask> findAllSubtask() {
        return new ArrayList<>(subtasks.values());
    }

    public Subtask findSubtaskById(Integer id) {
        return subtasks.get(id);
    }

//    Метод удаления всех сабтасок
//    Метод удаления всех эпиков
//    Получение всех сабтасок эпика по ид эпика

}

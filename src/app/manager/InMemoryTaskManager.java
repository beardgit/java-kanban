package app.manager;

import app.tasks.Epic;
import app.tasks.Subtask;
import app.tasks.Task;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics = new HashMap<>();
    protected final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    private final HistoryManager historyManager;

    InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    protected static Integer countId = 0;

    //  Создание обобщенного ID классу
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
            for (Epic epic : epics.values()) {
                epic.clearSubtasks(); // Очищаем список подзадач в каждом эпике
            }
            return true;
        }
        return false;
    }


    // Блок Получения задач по id
    @Override
    public Task getTaskById(Integer id) {
        Task task = tasks.get(id);
        historyManager.addToHistory(task);
        return tasks.get(id);
    }

    @Override
    public Subtask getSubtaskById(Integer id) {
        Subtask subtask = subtasks.get(id);
        historyManager.addToHistory(subtask);
        return subtask;
    }


    public  List<Subtask> getSubtasksByEpicId(Integer epicId){
        List<Subtask> result  = new ArrayList<>();
        for(Subtask subtask : subtasks.values()){
            if(subtask.getEpicId().equals(epicId)){
                result.add(subtask);
            }
        }
        return result;
    }

    @Override
    public Epic getEpicById(Integer id) {
        Epic epic = epics.get(id);
        historyManager.addToHistory(epic);
        return epic;
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

    public Subtask appendSubtask(Subtask newSubtask) {
        int newId = nextId();
        newSubtask.setId(newId);
        subtasks.put(newId, newSubtask);
        Epic epic = epics.get(newSubtask.getEpicId());
        if(epic != null){
            epic.addSubtask(newSubtask);
        }
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
        Subtask subtaskInSubtasks =  subtasks.put(updatedSubtask.getId(), updatedSubtask);
        if (subtaskInSubtasks != null){
            Epic epic = epics.get(updatedSubtask.getEpicId());
            if(epic != null){
                epic.getStatus();
            }
        }
        return subtaskInSubtasks;
    }


    //    Блок удаления по id
    @Override
    public Task deleteTask(Integer id) {
        Task remove = tasks.remove(id);
        if (remove != null) {
            historyManager.removeFromHistory(remove);
        }
        return remove;
    }

    @Override
    public Epic deleteEpic(Integer id) {

        Epic epic = epics.remove(id);
        historyManager.removeFromHistory(epic);

        if(epic != null) {
            for(Subtask subtask : getSubtasksByEpicId(id)){
                deleteSubtask(subtask.getId());
            }
        }
        return epic;
    }

    @Override
    public void deleteSubtask(Integer id) {
        Subtask subtask = subtasks.get(id);

        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());

            if (epic != null) {
                // Удаляем подзадачу из списка подзадач эпика
                epic.removeSubtaskById(id);
            }

            subtasks.remove(id);
            historyManager.removeFromHistory(subtask);
        }
    }

    //Получение истории
    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(historyManager.getHistory());
    }

}

package app.manager;


import app.tasks.Epic;
import app.tasks.Subtask;
import app.tasks.Task;

import java.util.List;

public interface TaskManager {
    // Блок получения всех задач
    List<Task> getAllTasks();

    List<Epic> getAllEpic();

    List<Subtask> getAllSubtasks();

    // блок удаления всех задач
    boolean clearTasks();

    boolean clearEpics();

    boolean clearEpic();

    boolean clearSubtasks();

    // Блок получения задач по id
    Task getTaskById(Integer id);

    Epic getEpicById(Integer id);

    Subtask getSubtaskById(Integer id);

    // Блок добавления задач
    Task appendTask(Task newTask);

    Epic appendEpic(Epic newEpic);

    Subtask appendSubtask(Subtask newSubtask);

    // Блок обновления задач
    Task updateTask(Task updatedTask);

    Epic updateEpic(Epic updatedEpic);

    Subtask updateSubtask(Subtask updatedSubtask);

    //  Блок удаления по id
    Task deleteTask(Integer id);

    Epic deleteEpic(Integer id);

    void deleteSubtask(Integer id);

}

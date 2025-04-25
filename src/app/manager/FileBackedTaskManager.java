package app.manager;

import app.enumeration.TypeTask;
import app.tasks.Epic;
import app.tasks.Subtask;
import app.tasks.Task;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final Path filePath;

    FileBackedTaskManager(HistoryManager historyManager, Path data) {
        super(historyManager);
        this.filePath = data;
    }


    //   Блок получения данных
    private Map<TypeTask, List<? extends Task>> getAll() {
        System.out.println("get all List Task , Epic , Subtask");

        Map<TypeTask, List<? extends Task>> tasksAll = new HashMap<>();

        tasksAll.put(TypeTask.TASK, getAllTasks());
        tasksAll.put(TypeTask.EPIC, getAllEpic());
        tasksAll.put(TypeTask.SUBTASK, getAllSubtasks());

        return tasksAll;
    }

    private void save() {
//        Получить данные
        Map<TypeTask, List<? extends Task>> taskData =  getAll();


//        Обработать данные

// Сформировать файл

// записать в файл

    }


    // блок обработки данных
//public <T> String stringifyToListTask(List<T> listTasks){
//
//}

public <T extends Task> String stringify(T task, TypeTask typeTask){
     Integer id = task.getId() ;
     String type = String.valueOf(typeTask);
     String name = task.getName();
     String status = String.valueOf(task.getStatus());
     String description = task.getDescription();
     Integer epicId = task.getClass() == Subtask.class ? ((Subtask) task).getEpic().getId() : null;

     StringBuilder str = new StringBuilder();
     str.append(id == null ? "" : id).append(",")
             .append(type)
             .append()

}





    // блок записи данных




    // Блок переопределения методов
    @Override
    public Task deleteTask(Integer id) {
//        Вначале проводим удаление и сохраняем то что удаляем
        Task task = super.deleteTask(id);
//        Проводим полное сохранение
        save();
//        Возвращаем то что удалили
        return task;
    }

    @Override
    public Epic deleteEpic(Integer id) {
        Epic epic = super.deleteEpic(id);
        save();
        return epic;
    }

    @Override
    public void deleteSubtask(Integer id) {
        super.deleteSubtask(id);
        save();
    }


    @Override
    public Subtask updateSubtask(Subtask updatedSubtask) {
        Subtask newSubtask = super.updateSubtask(updatedSubtask);
        save();
        return newSubtask;
    }

    @Override
    public Epic updateEpic(Epic updatedEpic) {
        Epic newEpic = super.updateEpic(updatedEpic);
        save();
        return newEpic;
    }

    @Override
    public Task updateTask(Task updatedTask) {
        Task newTask = super.updateTask(updatedTask);
        save();
        return newTask;
    }

    @Override
    public Subtask appendSubtask(Subtask newSubtask) {
        Subtask subtask = super.appendSubtask(newSubtask);
        save();
        return subtask;
    }

    @Override
    public Epic appendEpic(Epic newEpic) {
        Epic epic = super.appendEpic(newEpic);
        save();
        return epic;
    }

    @Override
    public Task appendTask(Task newTask) {
        Task task = super.appendTask(newTask);
        save();
        return task;
    }
}

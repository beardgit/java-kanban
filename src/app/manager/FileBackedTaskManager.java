package app.manager;

import app.enumeration.StatusTasks;
import app.enumeration.TypeTask;
import app.exception.FileLoadException;
import app.exception.FileSaveException;
import app.tasks.Epic;
import app.tasks.Subtask;
import app.tasks.Task;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final Path filePath;

    FileBackedTaskManager(HistoryManager historyManager, Path data) {
        super(historyManager);
        this.filePath = data;
    }


    //        Блок сохранения и записи данных
    private Map<TypeTask, List<? extends Task>> getAll() {
        System.out.println("get all List Task , Epic , Subtask");

        Map<TypeTask, List<? extends Task>> tasksAll = new HashMap<>();

        tasksAll.put(TypeTask.TASK, getAllTasks());
        tasksAll.put(TypeTask.EPIC, getAllEpic());
        tasksAll.put(TypeTask.SUBTASK, getAllSubtasks());

        return tasksAll;
    }

    //     Основной блок реализации класса
    private void save() {

        Map<TypeTask, List<? extends Task>> taskData = getAll();

        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(filePath)) {

            bufferedWriter.write("id,type,name,status,description,epic");
            bufferedWriter.newLine();

            for (Task task : taskData.get(TypeTask.TASK)) {
                bufferedWriter.write(stringify(task));
                bufferedWriter.newLine();
            }

            for (Task task : taskData.get(TypeTask.EPIC)) {
                bufferedWriter.write(stringify(task));
                bufferedWriter.newLine();
            }

            for (Task task : taskData.get(TypeTask.SUBTASK)) {
                bufferedWriter.write(stringify(task));
                bufferedWriter.newLine();
            }

        } catch (IOException e) {
            throw new FileSaveException(e.getMessage());
        }

    }

    public static FileBackedTaskManager loadFromFile(File file) {

        Path pathFile = file.toPath();
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(new InMemoryHistoryManager(), pathFile);

        try {
            List<String> listAllLines = Files.readAllLines(pathFile);

            for (String str : listAllLines) {
//             Получаем готовую сущность из строки
                Task task = fromString(str);
//            Наполнение у taskManager внутренних коллекций
                if (task instanceof Subtask) {
                    fileBackedTaskManager.subtasks.put(task.getId(), (Subtask) task);
                } else if (task instanceof Epic) {
                    fileBackedTaskManager.epics.put(task.getId(), (Epic) task);
                } else {
                    fileBackedTaskManager.tasks.put(task.getId(), task);
                }
//        Связь эпика и подзадачи
                for (Subtask subtask : fileBackedTaskManager.getAllSubtasks()) {
//             У текущей сабтаски берем epicId для поика сущности epic в коллекции
                    Epic epic = fileBackedTaskManager.epics.get(subtask.getEpicId());
//             Сохраняем во внутренний список сущности epic сабтаску которая содержит id данного эпика

                }

            }

            return fileBackedTaskManager;

        } catch (IOException e) {
            throw new FileLoadException(e.getMessage());
        }
    }

//    Блок преобразования
    private <T extends Task> String stringify(T task) {
        Integer id = task.getId();
        String type = String.valueOf(task.getType());
        String name = task.getName();
        String status = String.valueOf(task.getStatus());
        String description = task.getDescription() != null ? task.getDescription() : "";
        Integer epic = task instanceof Subtask ? ((Subtask) task).getEpicId() : null;

        return String.format("%d,%s,%s,%s,%s,%s",
                id,
                type,
                name,
                status,
                description,
                epic != null ? epic : "");
    }

    private static Task fromString(String str) {
//    Ожидаемое считывание из файла  id,type,name,status,description,epic

        String[] attributes = str.split(",");

        Integer id = Integer.valueOf(attributes[0]);
        TypeTask type = TypeTask.valueOf(attributes[1]);
        String name = attributes[2];
        StatusTasks status = StatusTasks.valueOf(attributes[3]);
        String description = attributes[4];
        Integer epicId = Integer.valueOf(attributes[5]);

        Task task;
        switch (type) {
            case TASK:
                task = new Task(name, description);
                task.setId(id);
                task.setStatus(status);
                break;
            case EPIC:
                task = new Epic(name, description);
                task.setId(id);
                task.setStatus(status);
                break;
            case SUBTASK:
                task = new Subtask(name, description, epicId);
                task.setStatus(status);
                break;
            default:
                task = null;
        }

        return task;
    }


    // Блок переопределения методов
    @Override
    public Task deleteTask(Integer id) {
//        Вначале проводим удаление и возвращаем удаляем удаляемую таску
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

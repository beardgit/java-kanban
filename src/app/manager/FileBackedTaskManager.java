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
import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final Path filePath;

    private static final String TITLE = "id,type,name,status,description,epic,startTime,duration,endTime";
    private static final String FORMAT_TITLE = "%d,%s,%s,%s,%s,%s,%s,%s,%s";

    public FileBackedTaskManager(HistoryManager historyManager, Path filePath) {
        super(historyManager);
        this.filePath = filePath;
    }

    public void save() {

        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            writer.write(TITLE);
            writer.newLine();

            for (Task task : tasks.values()) writer.write(stringify(task) + "\n");
            for (Epic epic : epics.values()) writer.write(stringify(epic) + "\n");
            for (Subtask subtask : subtasks.values()) writer.write(stringify(subtask) + "\n");

        } catch (IOException e) {
            throw new FileSaveException("Ошибка при сохранении: " + e.getMessage());
        }
    }

    private <T extends Task> String stringify(T task) {

        return String.format(FORMAT_TITLE,
                task.getId(),
                task.getType(),
                task.getName(),
                task.getStatus(),
                task.getDescription(),
                task.getType().equals(TypeTask.SUBTASK) ? String.valueOf(((Subtask) task).getEpicId()) : "",
                task.getStartTime() != null ? task.getStartTime() : "",
                task.getDuration() != null ? task.getDuration() : "",
                task.getEndTime() != null ? task.getEndTime() : ""
        );
    }

    public static FileBackedTaskManager loadFromFile(File file) {

        Path path = file.toPath();
        FileBackedTaskManager manager = new FileBackedTaskManager(Managers.getDefaultHistory(), path);

        int maxId = 0;
        try {
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {

                if (line.trim().isBlank() || line.equals(TITLE)) {
                    continue;
                }

                Task task = fromString(line);
                maxId = Math.max(maxId, task.getId());

                switch (task.getType()) {

                    case TypeTask.TASK:
                        manager.tasks.put(task.getId(), task);
                        break;
                    case TypeTask.EPIC:
                        manager.epics.put(task.getId(), (Epic) task);
                        break;
                    case TypeTask.SUBTASK:
                        manager.subtasks.put(task.getId(), (Subtask) task);
                        break;

                }
            }

            for (Subtask sub : manager.subtasks.values()) {
                Epic epic = manager.epics.get(sub.getEpicId());
                if (epic != null) epic.addSubtask(sub);
            }

            countId = maxId + 1;
            return manager;

        } catch (IOException e) {
            throw new FileLoadException("Ошибка при загрузке: ", e);
        }
    }

    private static Task fromString(String line) {
//        "id,type,name,status,description,epic,startTime,duration,endTime"
        String[] parts = line.split(",", -1);
        int id = Integer.parseInt(parts[0]);
        TypeTask type = TypeTask.valueOf(parts[1]);
        String name = parts[2];
        StatusTasks status = StatusTasks.valueOf(parts[3]);
        String description = parts[4];
        Integer epicId = parts[5].isEmpty() ? null : Integer.parseInt(parts[5]);
        Instant startTime = parts[6].isEmpty() ? null : Instant.parse(parts[6]);
        Duration duration = parts[7].isEmpty() ? null : Duration.parse(parts[7]);


        Task task;
        switch (type) {
            case TypeTask.SUBTASK:
                task = new Subtask(name, description, epicId, startTime, duration);
                break;
            case TypeTask.EPIC:
                task = new Epic(name, description);
                break;
            case TypeTask.TASK:
                task = new Task(name, description, startTime, duration);
                break;
            default:
                task = null;
        }

        if (task != null) {
            task.setId(id);
            task.setStatus(status);
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


    @Override
    public boolean clearTasks() {
        boolean isEmpty = super.clearTasks();
        save();
        return isEmpty;
    }

    @Override
    public boolean clearEpics() {
        boolean isEmpty = super.clearEpics();
        save();
        return isEmpty;
    }

    @Override
    public boolean clearSubtasks() {
        boolean isEmpty = super.clearSubtasks();
        save();
        return isEmpty;
    }

}

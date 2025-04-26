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


    //    Блок сохранения и записи данных
    private Map<TypeTask, List<? extends Task>> getAll() {
        System.out.println("get all List Task , Epic , Subtask");

        Map<TypeTask, List<? extends Task>> tasksAll = new HashMap<>();

        tasksAll.put(TypeTask.TASK, getAllTasks());
        tasksAll.put(TypeTask.EPIC, getAllEpic());
        tasksAll.put(TypeTask.SUBTASK, getAllSubtasks());

        return tasksAll;
    }

    private void ss() {

    }

//    public static FileBackedTaskManager loadFromFile(Path filePath, HistoryManager historyManager) {
//        FileBackedTaskManager manager = new FileBackedTaskManager(historyManager, filePath);
//
//        try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
//
//            String line;
//
//            Map<Integer, Epic> epicMap = new HashMap<>();
//            Map<Integer, Subtask> subtaskMap = new HashMap<>();
//            Map<Integer, Integer> subtaskToEpicMap = new HashMap<>();
//
//            reader.readLine();
//
//            while ((line = reader.readLine()) != null) {
//
//                if (line.isEmpty()) continue;
//
//                String[] fields = line.split(",");
//
//                if (fields.length < 5) {
//                    System.err.println("Некорректная строка в файле: " + line);
//                    continue;
//                }
//
//                String type = fields[0];
//                int id = Integer.parseInt(fields[1]);
//                String name = fields[2];
//                String description = fields[3];
//                StatusTasks status = StatusTasks.valueOf(fields[4].toUpperCase());
//
//                switch (type) {
//                    case "TASK":
//                        Task task = new Task(name, description);
//                        task.setId(id);
//                        task.setStatus(status);
//                        manager.tasks.put(id, task);
//                        break;
//
//                    case "EPIC":
//                        Epic epic = new Epic(name, description);
//                        epic.setId(id);
//                        epicMap.put(id, epic);
//                        manager.epics.put(id, epic);
//                        break;
//
//                    case "SUBTASK":
//                        int epicId = Integer.parseInt(fields[5]);
//
//                        Subtask subtask = new Subtask(name, description, null); // Temporarily set epic to null
//                        subtask.setId(id);
//                        subtask.setStatus(status);
//                        subtaskMap.put(id, subtask);
//                        subtaskToEpicMap.put(id, epicId); // Save the epic_id for later
//                        manager.epics.put(id, subtask.getEpic());
//                        break;
//
//                    default:
//                        System.err.println("Неизвестный тип задачи: " + type);
//                        break;
//                }
//            }
//
//            // Установка связей между Subtask и Epic
//            for (Map.Entry<Integer, Integer> entry : subtaskToEpicMap.entrySet()) {
//                int subtaskId = entry.getKey();
//                int epicId = entry.getValue();
//
//                Subtask subtask = subtaskMap.get(subtaskId);
//                Epic epic = epicMap.get(epicId);
//
//                if (subtask != null && epic != null) {
//                    subtask.setEpic(epic);
//                    epic.addSubtask(subtask);
//                } else {
//                    System.err.println("Не удалось установить связь для Subtask ID=" + subtaskId + " и Epic ID=" + epicId);
//                }
//            }
//
//
//            manager.updateCountId();
//
//        } catch (IOException e) {
//            System.err.println("Ошибка при загрузке данных: " + e.getMessage());
//        }
//
//        return manager;
//    }
//
//    private void updateCountId() {
//        int maxId = Math.max(
//                tasks.keySet().stream().max(Integer::compareTo).orElse(0),
//                Math.max(
//                        epics.keySet().stream().max(Integer::compareTo).orElse(0),
//                        subtasks.keySet().stream().max(Integer::compareTo).orElse(0)
//                )
//        );
//        countId = maxId;
//    }


    //        Получить данные

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

    public Task fromString(String str) {
//        id,type,name,status,description,epic

        String[] attributes = str.split(",");

        Integer id = Integer.valueOf(attributes[0]);
        TypeTask type = TypeTask.valueOf(attributes[1]);
        String name = attributes[2];
        StatusTasks status = StatusTasks.valueOf(attributes[3]);
        String description = attributes[4];
        Integer epic = Integer.valueOf(attributes[5]);

        switch (type){
            case SUBTASK:
                Subtask subtask = new Subtask(name, description, id);
                return null ;
        }

        return null;
    }


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

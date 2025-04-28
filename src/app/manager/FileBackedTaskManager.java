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
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final Path filePath;
    //    TITLE для добавления шапки файла и пропуска данной строки в файле
    private static final String TITLE = "id,type,name,status,description,epic";

    public FileBackedTaskManager(HistoryManager historyManager, Path data) {
        super(historyManager);
        this.filePath = data;
    }


    //     Основной блок реализации класса (Сохранение в файл / Восстановление из файла)
    public void save() {

        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(filePath)) {

            bufferedWriter.write(TITLE);
            bufferedWriter.newLine();

//            Итерируемся по всем получаемым задачам из Map / итерация по получаемой коллекции
            for (Task task : tasks.values()) {
                bufferedWriter.write(stringify(task));
                bufferedWriter.newLine();
            }

            for (Epic epic : epics.values()) {
                bufferedWriter.write(stringify(epic));
                bufferedWriter.newLine();
            }

            for (Subtask subtask : subtasks.values()) {
                bufferedWriter.write(stringify(subtask));
                bufferedWriter.newLine();
            }

        } catch (IOException e) {
            throw new FileSaveException(e.getMessage());
        }

    }


    public static FileBackedTaskManager loadFromFile(File file) {

        Path pathFile = file.toPath();
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(new InMemoryHistoryManager(), pathFile);
        int maxCountId = 0;

        try {
            List<String> listAllLines = Files.readAllLines(pathFile);

            for (String str : listAllLines) {
                if (str.isEmpty() || str.equals(TITLE)) {
                    continue;
                }
//                Получаем сущность из строки для наполнения менеджера
                Task task = fromString(str);
                maxCountId = task.getId() > maxCountId ? task.getId() : maxCountId;
//            Наполнение у taskManager внутренних коллекций
                if (task.getType().equals(TypeTask.SUBTASK)) {
                    fileBackedTaskManager.subtasks.put(task.getId(), (Subtask) task);
                } else if (task.getType().equals(TypeTask.EPIC)) {
                    fileBackedTaskManager.epics.put(task.getId(), (Epic) task);
                } else {
                    fileBackedTaskManager.tasks.put(task.getId(), task);
                }

            }
//                   Связь эпика и подзадачи
            for (Subtask subtask : fileBackedTaskManager.getAllSubtasks()) {
//             У текущей сабтаски берем epicId для поиска сущности epic в коллекции
                Epic epic = fileBackedTaskManager.epics.get(subtask.getEpicId());

//             Сохраняем во внутренний список сущности epic сабтаску которая содержит id данного эпика
                if (epic != null) {
                    epic.addSubtask(subtask);
                }

            }
//           Обращение к countId InMemoryTaskManager для установки обновленного счетчика
            countId = maxCountId + 1;

            return fileBackedTaskManager;

        } catch (IOException e) {
            throw new FileLoadException("Ошибка метода loadFromFile : ", e);
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
        TypeTask type = TypeTask.valueOf(attributes[1]); // Определяем тип задачи
        String name = attributes[2];
        StatusTasks status = StatusTasks.valueOf(attributes[3]);
        String description = attributes[4];
        Integer epicId = attributes.length > 5 && !attributes[5].isEmpty() ? Integer.valueOf(attributes[5]) : null;

        switch (type) {
            case TASK:
                Task task = new Task(name, description);
                task.setId(id);
                task.setStatus(status);
                return task;
            case EPIC:
                Epic epic = new Epic(name, description);
                epic.setId(id);
                epic.setStatus(status);
                return epic;
            case SUBTASK:
                Subtask subtask = new Subtask(name, description, epicId);
                subtask.setStatus(status);
                subtask.setId(id);
                return subtask;
            default:
                return null;
        }
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

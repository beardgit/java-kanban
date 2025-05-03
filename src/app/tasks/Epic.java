package app.tasks;

import app.enumeration.StatusTasks;
import app.enumeration.TypeTask;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {

    private final TypeTask type = TypeTask.EPIC;
    private final List<Subtask> listSubtasks = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public List<Subtask> getListSubtasks() {
        return listSubtasks;
    }

    public boolean addSubtask(Subtask subtask) {
        return listSubtasks.add(subtask);
    }

    public boolean removeSubtaskById(int id) {
        return listSubtasks.removeIf(subtask -> Objects.equals(subtask.getId(), id));
    }

    public boolean clearSubtasks() {
        if (listSubtasks.isEmpty()) return false;
        listSubtasks.clear();
        return true;
    }

    @Override
    public StatusTasks getStatus() {
        if (listSubtasks.isEmpty()) return StatusTasks.NEW;

        boolean allNew = true;
        boolean allDone = true;

        for (Subtask sub : listSubtasks) {
            if (sub.getStatus() != StatusTasks.NEW) allNew = false;
            if (sub.getStatus() != StatusTasks.DONE) allDone = false;
        }

        if (allDone) return StatusTasks.DONE;
        if (allNew) return StatusTasks.NEW;
        return StatusTasks.IN_PROGRESS;
    }

    @Override
    public Duration getDuration() {
        return listSubtasks.stream()
                .filter(s -> s.getDuration() != null)
                .map(Subtask::getDuration)
                .reduce(Duration.ZERO, Duration::plus);
    }

    @Override
    public Instant getStartTime() {
        return listSubtasks.stream()
                .map(Subtask::getStartTime)
                .filter(Objects::nonNull)
                .min(Instant::compareTo)
                .orElse(null);
    }

    @Override
    public Instant getEndTime() {
        return listSubtasks.stream()
                .map(Subtask::getEndTime)
                .filter(Objects::nonNull)
                .max(Instant::compareTo)
                .orElse(null);
    }

    @Override
    public TypeTask getType() {
        return this.type;
    }
}
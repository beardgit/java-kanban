package app.tasks;

import app.enumeration.StatusTasks;
import app.enumeration.TypeTask;
import com.google.gson.annotations.SerializedName;

import java.time.Duration;
import java.time.Instant;

public class Subtask extends Task {

    private Integer epicId;


    public Subtask(String name, String description, Integer epicId) {
        super(name, description);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, Integer epicId, Instant startTime, Duration duration) {
        super(name, description, startTime, duration);
        this.epicId = epicId;
    }

    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(Integer epicId) {
        this.epicId = epicId;
    }

    @Override
    public void setStatus(StatusTasks status) {
        super.setStatus(status);
    }

    @Override
    public TypeTask getType() {
        return TypeTask.SUBTASK;
    }
}
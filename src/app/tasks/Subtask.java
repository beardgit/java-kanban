package app.tasks;

import app.enumeration.StatusTasks;
import app.enumeration.TypeTask;

public class Subtask extends Task {
    private Integer epicId;
    private final TypeTask type = TypeTask.SUBTASK;

    public Subtask(String name, String description, Integer epicId) {
        super(name, description);
        this.epicId = epicId;
//        epic.addSubtask(this);

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

}

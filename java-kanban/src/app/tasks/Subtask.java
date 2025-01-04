package app.tasks;

public class Subtask extends Task {
    Integer epicId;

    public Subtask(Integer id, String name, String description) {
        super(id, name, description);

    }

    public Subtask(String name, String description){
        super(name, description);
    }

    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(Integer epicId) {
        this.epicId = epicId;
    }

}

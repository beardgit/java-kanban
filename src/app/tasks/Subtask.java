package app.tasks;

public class Subtask extends Task {
    private Epic epic;

    public Subtask(Integer id, String name, String description) {
        super(id, name, description);

    }

    public Subtask(String name, String description){
        super(name, description);
    }

    public Epic getEpic() {
        return epic;
    }

    public void setEpic(Epic epic) {
        this.epic = epic;
        //this.epic.setStatus();
    }

    @Override
    public void setStatus(StatusTasks status)
    {
        super.setStatus(status);
        //this.getEpic().setStatus();
    }


}

package app.tasks;

import app.enumeration.StatusTasks;
import app.enumeration.TypeTask;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private final TypeTask type = TypeTask.EPIC;

    private List<Subtask> listSubtasks = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public List<Subtask> getListSubtasks() {
        return listSubtasks;
    }

    public boolean addSubtask(Subtask subtask) {
        return listSubtasks.add(subtask);
    }

    @Override
    public StatusTasks getStatus() {
        if (this.getListSubtasks().isEmpty()) {
            return StatusTasks.NEW;
        }

        StatusTasks statusOne = StatusTasks.NEW;
        List<Subtask> listSubtasks = this.getListSubtasks();

        for (int i = 0; i < listSubtasks.size(); i++) {
            if (i == 0) {
                statusOne = listSubtasks.get(i).getStatus();
                if (statusOne == StatusTasks.IN_PROGRESS) {
                    break;
                }
            } else {
                if (statusOne != listSubtasks.get(i).getStatus()) {
                    statusOne = StatusTasks.IN_PROGRESS;
                    break;
                }
            }
        }

        return statusOne;
    }

    public boolean removeSubtask(Integer subtaskId) {
        return listSubtasks.remove(subtaskId);
    }

    public boolean removeSubtaskById(int id) {
        Subtask subtaskToDelete = null;
        if (!listSubtasks.isEmpty()) {
            for (Subtask subtask : listSubtasks) {
                if (subtask.getId() == id) {
                    subtaskToDelete = subtask;
                    break;
                }
            }
            if (subtaskToDelete != null) {
                return  listSubtasks.remove(subtaskToDelete);
            }
        }
        return false;
    }

    public boolean clearSubtasks() {
        if (listSubtasks.isEmpty()) {
            return false;
        } else {
            listSubtasks.clear();
            return true;
        }
    }


    @Override
    public void setStatus(StatusTasks status) {
        //Do nothing
    }

}
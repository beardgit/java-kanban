package app.tasks;

import java.util.ArrayList;

public class Epic extends Task {
    ArrayList<Subtask> epicList = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public ArrayList<Subtask> getEpicList() {
        return epicList;
    }

    public boolean addSubtask(Subtask subtask) {
        return epicList.add(subtask);
    }

    public boolean removeSubtask(int id) {
        if (!epicList.isEmpty()) {
            for (Subtask subtask : epicList) {
                int idSubtask = subtask.getId();
                if (idSubtask == id) {
                    epicList.remove(subtask);
                    return true;
                }
            }
            return false;
        }
        return false;
    }
}

package app.tasks;

import java.util.ArrayList;

public class Epic extends Task {
   private ArrayList<Subtask> subtasks = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    public boolean addSubtask(Subtask subtask) {
        return subtasks.add(subtask);
    }

    public boolean removeSubtask(int id) {
        if (!subtasks.isEmpty()) {
            for (Subtask subtask : subtasks) {
                int idSubtask = subtask.getId();
                if (idSubtask == id) {
                    subtasks.remove(subtask);
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public boolean clearSubtask (){
       subtasks.clear();
       if(!subtasks.isEmpty()){
           return false;
       }else {
           return true;
       }
    }

    @Override
    public StatusTasks getStatus()
    {
        if (this.getSubtasks().isEmpty()) {
            //this.setStatus(StatusTasks.NEW);
            return StatusTasks.NEW;
        }

        StatusTasks statusOne = StatusTasks.NEW;
        ArrayList<Subtask> listSubtasks = this.getSubtasks();

        for (int i = 0; i < listSubtasks.size(); i++) {
            if(i == 0 ){
                statusOne = listSubtasks.get(i).getStatus();
                if(statusOne==StatusTasks.IN_PROGRESS)
                {
                    break;
                }
            }
            else {
                if(statusOne!=listSubtasks.get(i).getStatus())
                {
                    statusOne=StatusTasks.IN_PROGRESS;
                    break;
                }
            }
        }
        //this.setStatus(statusOne);

        return statusOne;
    }


    @Override
    public void setStatus(StatusTasks status) {
        //Do nothing
    }
}

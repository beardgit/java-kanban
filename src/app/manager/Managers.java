package app.manager;

import org.w3c.dom.Node;

public class Managers {

    public static TaskManager getDefault() {
        HistoryManager historyManager = getDefaultHistory();
        return new InMemoryTaskManager(historyManager);

    }



    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}

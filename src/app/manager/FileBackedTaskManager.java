package app.manager;

import java.io.File;
import java.nio.file.Path;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private File data;

    FileBackedTaskManager(HistoryManager historyManager, File data) {
        super(historyManager);
        this.data = data;
    }

    //    Метод для сохранения
    private void save() {
        System.out.println("save");
    }

//    При каждом изменении InMemoryTaskManager нужно проводить сохранения
// коогда происходят изменения в taskmanager ?

}

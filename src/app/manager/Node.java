package app.manager;

import app.tasks.Task;

public class Node {
    private Task task;
    private Task prev;
    private Task next;

    public Node(Task task, Task prev, Task next) {
        this.task = task;
        this.prev = prev;
        this.next = next;
    }

    public Task getTask() {
        return task;
    }

    public Task getPrev() {
        return prev;
    }

    public Task getNext() {
        return next;
    }


}

package app.manager;

import app.tasks.Task;

import java.util.Objects;

class Node {

    Node prev;
    Node next;
    Task task;

    Node(Task task) {
        this.task = task;
    }

    public Node getPrev() {
        return this.prev;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }

    public Node getNext() {
        return this.next;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    //Переопределяем equals и hashCode;
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;
        return Objects.equals(task.getId(), node.getTask().getId()) && Objects.equals(task, node.getTask());
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(task.getId());
        result = 31 * result + Objects.hashCode(task);
        return result;
    }
}

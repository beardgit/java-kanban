package app.manager;

import app.tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    //     Хранение головы и хвоста
    private Node head;

    private Node tail;


    //  Хранение истории
    private final Map<Integer, Node> historyMap = new HashMap<>();

    //   Блок обработки Node
    private void linkLast(Task task) {
        Node nodeTask = new Node(task);
        if (head == null && tail == null) {
            this.head = nodeTask;
            nodeTask.setNext(nodeTask);
            this.tail = nodeTask;
        }else {
            tail.setNext(nodeTask);
            nodeTask.setPrev(this.tail);
            this.tail = nodeTask;
        }
        historyMap.put(task.getId(), nodeTask);
    }

    private void removeNode(Node node) {
       if(node.getPrev() != null){
           node.getPrev().setNext(node.getNext());
       }else {
           this.head = node.getNext();
       }
       if(node.getNext() != null){
           node.getNext().setPrev(node.getPrev());
       }else {
           this.tail = node.getPrev();
       }
       historyMap.remove(node.getTask().getId());
    }

    // блок обработки истории


    @Override
    public void addToHistory(Task task) {
        if (task != null) {
            Node nodeTask = historyMap.get(task.getId());
            if (nodeTask != null) {
                removeNode(nodeTask); // Удаляем существующий узел
            }
            linkLast(task); // Добавляем новый узел для задачи
        }
    }

    @Override
    public void removeToHistory(Task task) {
        if(task != null){
            Integer id = task.getId();
            Node nodeTask = id != null ? historyMap.get(id) :  null;
            removeNode(nodeTask);
        }

    }

    @Override
    public List<Task> getHistory() {
        List<Task> taskList = new ArrayList<>();
        Node node = this.head;

        while (node != null){
            Task task = node.getTask();
            taskList.add(task);
            node = node.getNext();
        }
        return taskList;
    }

}

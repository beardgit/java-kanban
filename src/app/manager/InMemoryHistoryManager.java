package app.manager;

import app.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    //     Хранение головы и хвоста
    private Node head;
    private Node tail;

    //  Хранение истории
    private final Map<Integer, Node> historyMap = new HashMap<>();

    //   Блок обработки Node
    private void linkLast(Task task) {
        Node nodeTask = new Node(task);
        if (head == null && tail == null || historyMap.isEmpty()) {
            this.head = nodeTask;
            this.tail = nodeTask;
        }else {
            tail.setNext(nodeTask);
            nodeTask.setPrev(this.tail);
            tail = nodeTask;
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
        if(task != null){
            Node nodeTask = historyMap.get(task.getId());
           if(nodeTask != null){
               removeNode(nodeTask);
           }
            linkLast(nodeTask.getTask());
        }
    }

    @Override
    public void removeToHistory(Task task) {

    }

    @Override
    public List<Task> getHistory() {

    }

}

package app.manager;

import app.tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    //     Хранение головы и хвоста
    private Node head;
    private Node tail;


    //  Хранение истории в Map
    private final Map<Integer, Node> historyMap = new HashMap<>();

    // Блок связки и удаления Node
    private void linkLast(Node newNode) {

        if (head == null) {
            head = newNode;
            tail = newNode;
            tail.setNext(null);
            head.setPrev(null);
        } else {
            tail.setNext(newNode);
            newNode.setPrev(tail);
            tail = newNode;
        }
    }

    private void removeNode(Node node) {
        if (node == null) {
            return;
        }
        Node prev = node.getPrev();
        Node next = node.getNext();

        if (prev != null) {
            prev.setNext(next);
        } else {
            head = next;
        }

        if (next != null) {
            next.setPrev(prev);
        } else {
            tail = prev;
        }
    }

    //   Блок  обработки добавления и удаления в историю
    @Override
    public void addToHistory(Task task) {
        if (task == null) {
            return;
        }
        Integer keyTask = task.getId();

        if (historyMap.containsKey(keyTask)) {
            Node existingNode = historyMap.get(keyTask);
            removeNode(existingNode); // Удаляем старый узел из связи node
            historyMap.remove(keyTask); // Удаляем задачу из списка истории
        }

        Node newNode = new Node(task);
        linkLast(newNode);
        historyMap.put(keyTask, newNode);
    }


    @Override
    public void removeToHistory(Task task) {
        if (task == null) {
            return;
        }

        Integer id = task.getId();
        Node nodeTask = historyMap.get(id);
        if (nodeTask != null) {
            removeNode(nodeTask);
            historyMap.remove(id);
        }
    }


    @Override
    public List<Task> getHistory() {
        List<Task> taskList = new ArrayList<>();
        Node node = this.head;
        while (node != null) {
            taskList.add(node.getTask());
            node = node.getNext();
        }
        return taskList;
    }
}

package app.manager;

import app.tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    //     Хранение головы и хвоста
    private Node head;
    private Node tail;


    //  Хранение истории
    private final Map<Integer, Node> historyMap = new HashMap<>();


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

    @Override
    public void addToHistory(Task task) {
        if (task == null) {
            return; // Просто выходим, если задача равна null
        }
        Integer keyTask = task.getId();
        // Если задача уже есть в истории, удаляем её из текущего места
        if (historyMap.containsKey(keyTask)) {
            Node existingNode = historyMap.get(keyTask);
            removeNode(existingNode); // Удаляем старый узел из списка
            historyMap.remove(keyTask); // Удаляем задачу из карты
        }
        // Создаем новый узел для задачи
        Node newNode = new Node(task);
        // Добавляем задачу в конец списка
        linkLast(newNode);
        // Обновляем карту
        historyMap.put(keyTask, newNode);
    }

    private void removeNode(Node node) {
        if (node == null) {
            return; // Нечего удалять
        }

        Node prev = node.getPrev();
        Node next = node.getNext();

        if (prev != null) {
            prev.setNext(next); // Перенаправляем ссылку prev->next
        } else {
            head = next; // Если узел был головой, обновляем голову
        }

        if (next != null) {
            next.setPrev(prev); // Перенаправляем ссылку next->prev
        } else {
            tail = prev; // Если узел был хвостом, обновляем хвост
        }
    }

    @Override
    public void removeToHistory(Task task) {
        if (task == null) {
            return; // Просто выходим, если задача равна null
        }

        Integer id = task.getId();
        Node nodeTask = historyMap.get(id);
        if (nodeTask != null) {
            removeNode(nodeTask); // Удаляем узел из списка
            historyMap.remove(id); // Удаляем задачу из карты
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

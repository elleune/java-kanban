package ru.yandex.practicum.service;

import ru.yandex.practicum.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


    public class InMemoryHistoryManager implements HistoryManager {
        private final Map<Integer, ru.yandex.practicum.service.InMemoryHistoryManager.Node> nodes = new HashMap<>();
        private ru.yandex.practicum.service.InMemoryHistoryManager.Node head;
        private ru.yandex.practicum.service.InMemoryHistoryManager.Node tail;
        @Override
        public void add(Task task) {
            if (task == null) {
                return;
            }
            if (nodes.containsKey(task.getId())) {
                remove(task.getId());
            }
            linkLast(task);
        }

        @Override
        public void remove(int id) {
            if (nodes.containsKey(id)) {
                removeNode(nodes.get(id));
                nodes.remove(id);
            }
        }

        @Override
        public List<Task> getHistory() {
            List<Task> history = new ArrayList<>();
            ru.yandex.practicum.service.InMemoryHistoryManager.Node current = head;
            while (current != null) {
                history.add(current.task);
                current = current.next;
            }
            return history;
        }

        private void linkLast(Task task) {
            ru.yandex.practicum.service.InMemoryHistoryManager.Node newNode = new ru.yandex.practicum.service.InMemoryHistoryManager.Node(task);
            if (tail == null) {
                head = newNode;
                tail = newNode;
            } else {
                tail.next = newNode;
                newNode.prev = tail;
                tail = newNode;
            }
            nodes.put(task.getId(), newNode);
        }

        private void removeNode(ru.yandex.practicum.service.InMemoryHistoryManager.Node node) {
            if (node.prev != null) {
                node.prev.next = node.next;
            } else {
                head = node.next;
            }
            if (node.next != null) {
                node.next.prev = node.prev;
            } else {
                tail = node.prev;
            }
        }

        private static class Node {
            Task task;
            ru.yandex.practicum.service.InMemoryHistoryManager.Node prev;
            ru.yandex.practicum.service.InMemoryHistoryManager.Node next;

            Node(Task task) {
                this.task = task;
            }
        }
}
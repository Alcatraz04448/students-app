package org.example.list;

import java.util.AbstractSequentialList;
import java.util.ConcurrentModificationException;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/*
 * Двусвязный список - аналог LinkedList.
 * Каждый элемент хранится в узле Node, узлы связаны ссылками prev/next.
 * Наследуется от AbstractSequentialList, поэтому является обычным List
 * и работает с for-each и stream().
 */
public class CustomLinkedList<E> extends AbstractSequentialList<E> {

    private static class Node<E> {
        E value;
        Node<E> prev;
        Node<E> next;

        Node(E value) {
            this.value = value;
        }
    }

    private Node<E> first;
    private Node<E> last;
    private int size;

    @Override
    public boolean add(E value) {
        linkBefore(value, null);
        return true;
    }

    @Override
    public E get(int index) {
        return node(index).value;
    }

    @Override
    public E remove(int index) {
        Node<E> node = node(index);
        unlink(node);
        return node.value;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Индекс: " + index + ", размер: " + size);
        }
        return new NodeIterator(index);
    }

    private Node<E> node(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Индекс: " + index + ", размер: " + size);
        }
        // идём с того конца, который ближе к нужному индексу
        Node<E> current;
        if (index < size / 2) {
            current = first;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
        } else {
            current = last;
            for (int i = size - 1; i > index; i--) {
                current = current.prev;
            }
        }
        return current;
    }

    // вставляет новый узел перед next; если next == null - в конец списка
    private void linkBefore(E value, Node<E> next) {
        Node<E> node = new Node<>(value);
        Node<E> prev = (next == null) ? last : next.prev;
        node.prev = prev;
        node.next = next;

        if (prev == null) {
            first = node;
        } else {
            prev.next = node;
        }
        if (next == null) {
            last = node;
        } else {
            next.prev = node;
        }
        size++;
        modCount++;
    }

    private void unlink(Node<E> node) {
        if (node.prev == null) {
            first = node.next;
        } else {
            node.prev.next = node.next;
        }
        if (node.next == null) {
            last = node.prev;
        } else {
            node.next.prev = node.prev;
        }
        size--;
        modCount++;
    }

    private class NodeIterator implements ListIterator<E> {

        private Node<E> next;
        private Node<E> lastReturned;
        private int nextIndex;
        private int expectedModCount = modCount;

        NodeIterator(int index) {
            next = (index == size) ? null : node(index);
            nextIndex = index;
        }

        @Override
        public boolean hasNext() {
            return nextIndex < size;
        }

        @Override
        public E next() {
            checkModification();
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            lastReturned = next;
            next = next.next;
            nextIndex++;
            return lastReturned.value;
        }

        @Override
        public boolean hasPrevious() {
            return nextIndex > 0;
        }

        @Override
        public E previous() {
            checkModification();
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }
            next = (next == null) ? last : next.prev;
            lastReturned = next;
            nextIndex--;
            return lastReturned.value;
        }

        @Override
        public int nextIndex() {
            return nextIndex;
        }

        @Override
        public int previousIndex() {
            return nextIndex - 1;
        }

        @Override
        public void remove() {
            checkModification();
            if (lastReturned == null) {
                throw new IllegalStateException();
            }
            Node<E> afterRemoved = lastReturned.next;
            unlink(lastReturned);
            if (next == lastReturned) {
                next = afterRemoved; // перед этим был вызван previous()
            } else {
                nextIndex--;
            }
            lastReturned = null;
            expectedModCount = modCount;
        }

        @Override
        public void set(E value) {
            if (lastReturned == null) {
                throw new IllegalStateException();
            }
            checkModification();
            lastReturned.value = value;
        }

        @Override
        public void add(E value) {
            checkModification();
            lastReturned = null;
            linkBefore(value, next);
            nextIndex++;
            expectedModCount = modCount;
        }

        private void checkModification() {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }
    }
}

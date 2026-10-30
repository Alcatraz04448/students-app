package org.example.sorting;

import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

/*
 * Общая часть для алгоритмов: элементы копируются в массив, сортируются там
 * и записываются обратно в список. В связном списке get(i) идёт по узлам,
 * поэтому сортировать прямо в нём было бы медленно.
 */
public abstract class ArraySortAlgorithm<T> implements SortAlgorithm<T> {

    @Override
    public void sort(List<T> list, Comparator<T> comparator, boolean ascending) {
        Comparator<T> order = ascending ? comparator : comparator.reversed();

        @SuppressWarnings("unchecked")
        T[] array = (T[]) list.toArray();
        sortArray(array, order);

        ListIterator<T> iterator = list.listIterator();
        for (T element : array) {
            iterator.next();
            iterator.set(element);
        }
    }

    protected abstract void sortArray(T[] array, Comparator<T> comparator);

    protected void swap(T[] array, int i, int j) {
        T temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}

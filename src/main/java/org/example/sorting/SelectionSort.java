package org.example.sorting;

import java.util.Comparator;

public class SelectionSort<T> extends ArraySortAlgorithm<T> {

    @Override
    public String getTitle() {
        return "Сортировка выбором";
    }

    @Override
    protected void sortArray(T[] array, Comparator<T> comparator) {
        for (int i = 0; i < array.length - 1; i++) {
            // ищем минимальный элемент в неотсортированной части и ставим его на место i
            int minIndex = i;
            for (int j = i + 1; j < array.length; j++) {
                if (comparator.compare(array[j], array[minIndex]) < 0) {
                    minIndex = j;
                }
            }
            if (minIndex != i) {
                swap(array, i, minIndex);
            }
        }
    }
}

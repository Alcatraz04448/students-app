package org.example.sorting;

import java.util.Comparator;

public class QuickSort<T> extends ArraySortAlgorithm<T> {

    @Override
    public String getTitle() {
        return "Быстрая сортировка";
    }

    @Override
    protected void sortArray(T[] array, Comparator<T> comparator) {
        quickSort(array, 0, array.length - 1, comparator);
    }

    private void quickSort(T[] array, int low, int high, Comparator<T> comparator) {
        if (low >= high) {
            return;
        }
        // опорный элемент берём из середины, слева собираем меньшие, справа большие
        T pivot = array[(low + high) / 2];
        int i = low;
        int j = high;
        while (i <= j) {
            while (comparator.compare(array[i], pivot) < 0) {
                i++;
            }
            while (comparator.compare(array[j], pivot) > 0) {
                j--;
            }
            if (i <= j) {
                swap(array, i, j);
                i++;
                j--;
            }
        }
        quickSort(array, low, j, comparator);
        quickSort(array, i, high, comparator);
    }
}

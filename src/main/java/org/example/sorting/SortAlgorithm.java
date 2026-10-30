package org.example.sorting;

import java.util.Comparator;
import java.util.List;

public interface SortAlgorithm<T> {

    String getTitle();

    void sort(List<T> list, Comparator<T> comparator, boolean ascending);
}

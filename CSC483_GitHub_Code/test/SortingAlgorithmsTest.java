package com.csc483.sorting;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 tests for SortingAlgorithms.
 * Tests correctness on empty, single, sorted, reverse, random, and duplicate arrays.
 *
 * @author Student
 * @version 1.0
 */
@DisplayName("SortingAlgorithms Tests")
class SortingAlgorithmsTest {

    private SortingAlgorithms sorter;

    @BeforeEach
    void setUp() {
        sorter = new SortingAlgorithms();
    }

    // ====================================================================
    // HELPER: verifies an array is sorted ascending
    // ====================================================================
    private void assertSorted(int[] arr, String algorithm) {
        for (int i = 0; i < arr.length - 1; i++) {
            assertTrue(arr[i] <= arr[i + 1],
                    algorithm + " failed: arr[" + i + "]=" + arr[i] +
                    " > arr[" + (i+1) + "]=" + arr[i+1]);
        }
    }

    // ====================================================================
    // EMPTY & SINGLE ELEMENT
    // ====================================================================

    @Test @DisplayName("Insertion Sort: empty array")
    void insertionSortEmpty() {
        int[] arr = {};
        sorter.insertionSort(arr);
        assertEquals(0, arr.length);
    }

    @Test @DisplayName("Merge Sort: single element")
    void mergeSortSingle() {
        int[] arr = {42};
        sorter.mergeSort(arr);
        assertArrayEquals(new int[]{42}, arr);
    }

    @Test @DisplayName("Quick Sort: empty array")
    void quickSortEmpty() {
        sorter.quickSort(new int[]{});
    }

    @Test @DisplayName("Heap Sort: single element")
    void heapSortSingle() {
        int[] arr = {7};
        sorter.heapSort(arr);
        assertArrayEquals(new int[]{7}, arr);
    }

    // ====================================================================
    // RANDOM DATA
    // ====================================================================

    @Test @DisplayName("Insertion Sort: random array")
    void insertionSortRandom() {
        int[] arr = randomArray(500);
        int[] expected = arr.clone();
        Arrays.sort(expected);
        sorter.insertionSort(arr);
        assertArrayEquals(expected, arr);
        assertSorted(arr, "InsertionSort");
    }

    @Test @DisplayName("Merge Sort: random array")
    void mergeSortRandom() {
        int[] arr = randomArray(1000);
        int[] expected = arr.clone();
        Arrays.sort(expected);
        sorter.mergeSort(arr);
        assertArrayEquals(expected, arr);
    }

    @Test @DisplayName("Quick Sort: random array")
    void quickSortRandom() {
        int[] arr = randomArray(1000);
        int[] expected = arr.clone();
        Arrays.sort(expected);
        sorter.quickSort(arr);
        assertArrayEquals(expected, arr);
    }

    @Test @DisplayName("Heap Sort: random array")
    void heapSortRandom() {
        int[] arr = randomArray(1000);
        int[] expected = arr.clone();
        Arrays.sort(expected);
        sorter.heapSort(arr);
        assertArrayEquals(expected, arr);
    }

    // ====================================================================
    // ALREADY SORTED
    // ====================================================================

    @Test @DisplayName("Insertion Sort: already sorted (best case)")
    void insertionSortAlreadySorted() {
        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        sorter.insertionSort(arr);
        assertSorted(arr, "InsertionSort");
        // Best case: comparisons should be minimal (n-1)
        assertTrue(sorter.getComparisons() <= arr.length,
                "Too many comparisons on sorted data");
    }

    @Test @DisplayName("Merge Sort: already sorted")
    void mergeSortAlreadySorted() {
        int[] arr = {10, 20, 30, 40, 50};
        sorter.mergeSort(arr);
        assertSorted(arr, "MergeSort");
    }

    // ====================================================================
    // REVERSE SORTED
    // ====================================================================

    @Test @DisplayName("Heap Sort: reverse sorted")
    void heapSortReverse() {
        int[] arr = {100, 90, 80, 70, 60, 50, 40, 30, 20, 10};
        int[] expected = arr.clone();
        Arrays.sort(expected);
        sorter.heapSort(arr);
        assertArrayEquals(expected, arr);
    }

    @Test @DisplayName("Quick Sort: reverse sorted")
    void quickSortReverse() {
        int[] arr = {10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
        sorter.quickSort(arr);
        assertSorted(arr, "QuickSort");
    }

    // ====================================================================
    // DUPLICATES
    // ====================================================================

    @Test @DisplayName("All algorithms handle all-same values")
    void allAlgorithmsDuplicates() {
        int[] base = {5, 5, 5, 5, 5, 5, 5, 5};

        int[] a1 = base.clone(); sorter.insertionSort(a1);
        int[] a2 = base.clone(); sorter.mergeSort(a2);
        int[] a3 = base.clone(); sorter.quickSort(a3);
        int[] a4 = base.clone(); sorter.heapSort(a4);

        assertArrayEquals(base, a1, "InsertionSort");
        assertArrayEquals(base, a2, "MergeSort");
        assertArrayEquals(base, a3, "QuickSort");
        assertArrayEquals(base, a4, "HeapSort");
    }

    // ====================================================================
    // NEGATIVE NUMBERS
    // ====================================================================

    @Test @DisplayName("All algorithms handle negative numbers")
    void allAlgorithmsNegatives() {
        int[] arr = {-5, -1, -100, 0, 3, -2, 7};
        int[] expected = arr.clone();
        Arrays.sort(expected);

        int[] a1 = arr.clone(); sorter.insertionSort(a1); assertArrayEquals(expected, a1);
        int[] a2 = arr.clone(); sorter.mergeSort(a2);     assertArrayEquals(expected, a2);
        int[] a3 = arr.clone(); sorter.quickSort(a3);     assertArrayEquals(expected, a3);
        int[] a4 = arr.clone(); sorter.heapSort(a4);      assertArrayEquals(expected, a4);
    }

    // ====================================================================
    // METRICS SANITY CHECKS
    // ====================================================================

    @Test @DisplayName("Comparisons are positive after sort")
    void metricsPositive() {
        int[] arr = randomArray(100);
        sorter.mergeSort(arr);
        assertTrue(sorter.getComparisons() > 0);
    }

    @Test @DisplayName("Metrics reset between sorts")
    void metricsReset() {
        sorter.mergeSort(randomArray(100));
        long firstCount = sorter.getComparisons();
        sorter.mergeSort(new int[]{1}); // Tiny sort
        assertTrue(sorter.getComparisons() < firstCount,
                "Metrics were not reset between sorts");
    }

    // ====================================================================
    // UTILITY
    // ====================================================================

    private int[] randomArray(int n) {
        Random rng = new Random(77777);
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) arr[i] = rng.nextInt(10000) - 5000;
        return arr;
    }
}

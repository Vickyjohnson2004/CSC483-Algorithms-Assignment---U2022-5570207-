package com.csc483.sorting;

/**
 * Collection of sorting algorithm implementations for empirical analysis.
 * All algorithms sort integer arrays in ascending order and track
 * comparison and swap counts.
 *
 * <p>Algorithms included:
 * <ul>
 *   <li>Insertion Sort - O(n^2) average, O(n) best (nearly sorted)</li>
 *   <li>Merge Sort     - O(n log n) all cases, stable, not in-place</li>
 *   <li>Quick Sort     - O(n log n) average, O(n^2) worst, in-place</li>
 *   <li>Heap Sort      - O(n log n) all cases, in-place, not stable</li>
 * </ul>
 *
 * @author Student
 * @version 1.0
 */
public class SortingAlgorithms {

    /** Number of comparisons made during the last sort */
    private long comparisons = 0;

    /** Number of swaps (or assignments) made during the last sort */
    private long swaps = 0;

    /** Resets metrics for a new sort run */
    public void resetMetrics() {
        comparisons = 0;
        swaps = 0;
    }

    /** @return comparisons made during the last sort */
    public long getComparisons() { return comparisons; }

    /** @return swaps/assignments made during the last sort */
    public long getSwaps() { return swaps; }

    // ====================================================================
    // INSERTION SORT
    // ====================================================================

    /**
     * Sorts the array using Insertion Sort.
     *
     * <p>Best Case:   O(n)   - already sorted, only n-1 comparisons needed</p>
     * <p>Average:     O(n^2) - roughly n^2/4 comparisons and swaps</p>
     * <p>Worst Case:  O(n^2) - reverse sorted, n*(n-1)/2 comparisons</p>
     * <p>Space:       O(1)   - in-place</p>
     * <p>Stable:      Yes</p>
     *
     * @param arr  the integer array to sort
     */
    public void insertionSort(int[] arr) {
        resetMetrics();
        if (arr == null || arr.length <= 1) return;

        for (int i = 1; i < arr.length; i++) {
            int key = arr[i];
            int j = i - 1;

            // Shift elements greater than key one position ahead
            while (j >= 0 && arr[j] > key) {
                comparisons++;
                arr[j + 1] = arr[j];
                swaps++; // Count each shift as an assignment
                j--;
            }
            // Count the final comparison that broke the loop (or j < 0)
            if (j >= 0) comparisons++;

            arr[j + 1] = key;
            if (j + 1 != i) swaps++; // Actual placement
        }
    }

    // ====================================================================
    // MERGE SORT
    // ====================================================================

    /**
     * Sorts the array using Merge Sort (top-down recursive).
     *
     * <p>Best Case:   O(n log n)</p>
     * <p>Average:     O(n log n)</p>
     * <p>Worst Case:  O(n log n)</p>
     * <p>Space:       O(n)   - auxiliary array for merging</p>
     * <p>Stable:      Yes</p>
     *
     * @param arr  the integer array to sort
     */
    public void mergeSort(int[] arr) {
        resetMetrics();
        if (arr == null || arr.length <= 1) return;
        int[] temp = new int[arr.length];
        mergeSortHelper(arr, temp, 0, arr.length - 1);
    }

    /**
     * Recursive helper that divides and conquers.
     */
    private void mergeSortHelper(int[] arr, int[] temp, int left, int right) {
        if (left >= right) return;
        int mid = left + (right - left) / 2;
        mergeSortHelper(arr, temp, left, mid);
        mergeSortHelper(arr, temp, mid + 1, right);
        merge(arr, temp, left, mid, right);
    }

    /**
     * Merges two sorted sub-arrays [left..mid] and [mid+1..right].
     */
    private void merge(int[] arr, int[] temp, int left, int mid, int right) {
        // Copy both halves to temp
        for (int k = left; k <= right; k++) {
            temp[k] = arr[k];
        }

        int i = left;
        int j = mid + 1;
        int k = left;

        while (i <= mid && j <= right) {
            comparisons++;
            if (temp[i] <= temp[j]) {
                arr[k++] = temp[i++];
            } else {
                arr[k++] = temp[j++];
                swaps++; // Counts inversions corrected (no literal swap, but meaningful)
            }
        }
        // Copy remaining left half
        while (i <= mid) {
            arr[k++] = temp[i++];
        }
    }

    // ====================================================================
    // QUICK SORT
    // ====================================================================

    /**
     * Sorts the array using Quick Sort with median-of-three pivot selection.
     *
     * <p>Best Case:   O(n log n) - balanced partitions</p>
     * <p>Average:     O(n log n)</p>
     * <p>Worst Case:  O(n^2)     - already sorted with bad pivot (mitigated by median-of-3)</p>
     * <p>Space:       O(log n)   - recursive call stack</p>
     * <p>Stable:      No</p>
     *
     * @param arr  the integer array to sort
     */
    public void quickSort(int[] arr) {
        resetMetrics();
        if (arr == null || arr.length <= 1) return;
        quickSortHelper(arr, 0, arr.length - 1);
    }

    /**
     * Recursive Quick Sort helper.
     */
    private void quickSortHelper(int[] arr, int low, int high) {
        if (low < high) {
            // Use insertion sort for small subarrays (optimization)
            if (high - low < 10) {
                insertionSortRange(arr, low, high);
                return;
            }
            int pivotIndex = partition(arr, low, high);
            quickSortHelper(arr, low, pivotIndex - 1);
            quickSortHelper(arr, pivotIndex + 1, high);
        }
    }

    /**
     * Partitions the array around the median-of-three pivot.
     *
     * @return the final pivot index
     */
    private int partition(int[] arr, int low, int high) {
        // Median-of-three pivot selection
        int mid = low + (high - low) / 2;
        comparisons += 2;
        if (arr[low] > arr[mid]) { swap(arr, low, mid); }
        if (arr[low] > arr[high]) { swap(arr, low, high); }
        if (arr[mid] > arr[high]) { swap(arr, mid, high); }
        // Place pivot at high-1
        swap(arr, mid, high);
        int pivot = arr[high];

        int i = low - 1;
        for (int j = low; j < high; j++) {
            comparisons++;
            if (arr[j] <= pivot) {
                i++;
                swap(arr, i, j);
            }
        }
        swap(arr, i + 1, high); // Place pivot in correct position
        return i + 1;
    }

    /**
     * Insertion sort over a sub-range [low..high].
     */
    private void insertionSortRange(int[] arr, int low, int high) {
        for (int i = low + 1; i <= high; i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= low) {
                comparisons++;
                if (arr[j] > key) {
                    arr[j + 1] = arr[j];
                    swaps++;
                    j--;
                } else break;
            }
            arr[j + 1] = key;
        }
    }

    // ====================================================================
    // HEAP SORT
    // ====================================================================

    /**
     * Sorts the array using Heap Sort.
     *
     * <p>Best Case:   O(n log n)</p>
     * <p>Average:     O(n log n)</p>
     * <p>Worst Case:  O(n log n)</p>
     * <p>Space:       O(1)   - in-place</p>
     * <p>Stable:      No</p>
     *
     * @param arr  the integer array to sort
     */
    public void heapSort(int[] arr) {
        resetMetrics();
        if (arr == null || arr.length <= 1) return;
        int n = arr.length;

        // Build max-heap (heapify from last non-leaf down to root)
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(arr, n, i);
        }

        // Extract elements from heap one by one
        for (int i = n - 1; i > 0; i--) {
            swap(arr, 0, i);      // Move current max to end
            heapify(arr, i, 0);   // Restore max-heap on reduced array
        }
    }

    /**
     * Maintains the max-heap property for the subtree rooted at index i.
     *
     * @param arr   the array
     * @param n     size of the heap portion
     * @param i     root index of the subtree
     */
    private void heapify(int[] arr, int n, int i) {
        int largest = i;
        int left  = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < n) {
            comparisons++;
            if (arr[left] > arr[largest]) largest = left;
        }
        if (right < n) {
            comparisons++;
            if (arr[right] > arr[largest]) largest = right;
        }

        if (largest != i) {
            swap(arr, i, largest);
            heapify(arr, n, largest); // Recursively heapify the affected subtree
        }
    }

    // ====================================================================
    // UTILITY
    // ====================================================================

    /**
     * Swaps two elements in the array and increments the swap counter.
     */
    private void swap(int[] arr, int i, int j) {
        if (i == j) return;
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
        swaps++;
    }
}

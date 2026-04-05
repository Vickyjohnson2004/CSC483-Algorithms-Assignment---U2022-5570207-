package com.csc483.sorting;

import java.util.Arrays;
import java.util.Random;

/**
 * Empirical benchmark comparing Insertion Sort, Merge Sort, Quick Sort, and Heap Sort.
 * Tests on: Random, Sorted, Reverse-sorted, Nearly-sorted, and Duplicate-heavy data.
 *
 * @author Student
 * @version 1.0
 */
public class SortingBenchmark {

    /** Number of timing runs per test (average is reported) */
    private static final int RUNS = 5;

    /** Input sizes to test */
    private static final int[] SIZES = {100, 1_000, 10_000, 100_000};

    /** Distinct values count for duplicate-heavy dataset */
    private static final int DISTINCT_VALUES = 10;

    public static void main(String[] args) {
        SortingAlgorithms sorter = new SortingAlgorithms();

        System.out.println("================================================================");
        System.out.println("SORTING ALGORITHMS COMPARISON");
        System.out.println("================================================================");
        System.out.printf("%-12s %-15s %-12s %-15s %-12s%n",
                "Size", "Algorithm", "Time (ms)", "Comparisons", "Swaps");
        System.out.println("-".repeat(66));

        for (int n : SIZES) {
            runDatasetBenchmark(sorter, n, "RANDOM",       generateRandom(n));
            runDatasetBenchmark(sorter, n, "SORTED",       generateSorted(n));
            runDatasetBenchmark(sorter, n, "REVERSE",      generateReverse(n));
            runDatasetBenchmark(sorter, n, "NEARLY SORTED",generateNearlySorted(n));
            runDatasetBenchmark(sorter, n, "DUPLICATES",   generateDuplicates(n));
            System.out.println("-".repeat(66));
        }

        System.out.println("\nCONCLUSIONS:");
        System.out.println("- Quick Sort is fastest on average for random data");
        System.out.println("- Insertion Sort is competitive only for n < 1000");
        System.out.println("- Merge Sort provides consistent O(n log n) regardless of data order");
        System.out.println("- Heap Sort is reliable O(n log n) but slower than Quick Sort in practice");
        System.out.println("- Nearly-sorted data is ideal for Insertion Sort (approaches O(n))");
        System.out.println("================================================================");
    }

    /**
     * Runs all four algorithms on a given dataset and prints results.
     */
    private static void runDatasetBenchmark(SortingAlgorithms sorter, int n,
                                             String dataType, int[] baseData) {
        System.out.printf("\n  [%s data, n=%,d]%n", dataType, n);
        benchmarkAlgorithm(sorter, "Insertion", baseData, "insertion");
        benchmarkAlgorithm(sorter, "Merge    ", baseData, "merge");
        benchmarkAlgorithm(sorter, "Quick    ", baseData, "quick");
        benchmarkAlgorithm(sorter, "Heap     ", baseData, "heap");
    }

    /**
     * Runs one algorithm RUNS times, averages the metrics, and prints results.
     */
    private static void benchmarkAlgorithm(SortingAlgorithms sorter, String name,
                                            int[] baseData, String algorithm) {
        long totalTime = 0;
        long totalComparisons = 0;
        long totalSwaps = 0;

        for (int run = 0; run < RUNS; run++) {
            int[] arr = Arrays.copyOf(baseData, baseData.length);
            long start = System.nanoTime();
            switch (algorithm) {
                case "insertion": sorter.insertionSort(arr); break;
                case "merge":     sorter.mergeSort(arr);     break;
                case "quick":     sorter.quickSort(arr);     break;
                case "heap":      sorter.heapSort(arr);      break;
            }
            totalTime        += System.nanoTime() - start;
            totalComparisons += sorter.getComparisons();
            totalSwaps       += sorter.getSwaps();
        }

        double avgMs   = (totalTime / RUNS) / 1_000_000.0;
        long avgCmp    = totalComparisons / RUNS;
        long avgSwps   = totalSwaps / RUNS;

        System.out.printf("    %-12s  %8.3f ms   %,14d   %,14d%n",
                name, avgMs, avgCmp, avgSwps);
    }

    // ====================================================================
    // DATA GENERATORS
    // ====================================================================

    /** Random order data */
    private static int[] generateRandom(int n) {
        Random rng = new Random(12345);
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) arr[i] = rng.nextInt(n * 10);
        return arr;
    }

    /** Already sorted ascending */
    private static int[] generateSorted(int n) {
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) arr[i] = i;
        return arr;
    }

    /** Reverse sorted descending */
    private static int[] generateReverse(int n) {
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) arr[i] = n - i;
        return arr;
    }

    /** 90% sorted, 10% randomly swapped */
    private static int[] generateNearlySorted(int n) {
        int[] arr = generateSorted(n);
        Random rng = new Random(99999);
        int swaps = Math.max(1, n / 10);
        for (int i = 0; i < swaps; i++) {
            int a = rng.nextInt(n);
            int b = rng.nextInt(n);
            int tmp = arr[a]; arr[a] = arr[b]; arr[b] = tmp;
        }
        return arr;
    }

    /** Only DISTINCT_VALUES unique values */
    private static int[] generateDuplicates(int n) {
        Random rng = new Random(54321);
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) arr[i] = rng.nextInt(DISTINCT_VALUES);
        return arr;
    }
}

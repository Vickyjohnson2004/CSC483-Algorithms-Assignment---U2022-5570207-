package com.csc483.search;

import java.util.Map;

/**
 * TechMart Search Performance Benchmark.
 * Generates 100,000 products and compares sequential vs binary search performance.
 *
 * @author Student
 * @version 1.0
 */
public class TechMartBenchmark {

    /** Number of products in the benchmark */
    private static final int N = 100_000;

    /** Number of timing runs per test case */
    private static final int RUNS = 5;

    public static void main(String[] args) {
        System.out.println("================================================================");
        System.out.println("TECHMART SEARCH PERFORMANCE ANALYSIS (n = " + N + " products)");
        System.out.println("================================================================");

        // Generate sorted dataset
        System.out.println("\nGenerating " + N + " products...");
        Product[] products = DataGenerator.generateSortedProducts(N);
        System.out.println("Dataset generated and sorted by productId.\n");

        ProductSearch searcher = new ProductSearch();

        // ----------------------------------------------------------------
        // SEQUENTIAL SEARCH BENCHMARKS
        // ----------------------------------------------------------------
        System.out.println("SEQUENTIAL SEARCH:");
        System.out.println("-".repeat(50));

        // Best case: ID at position 0
        int bestId = DataGenerator.getBestCaseId(products);
        double seqBest = measureSequential(searcher, products, bestId);
        System.out.printf("  Best Case (ID at position 0):     %.3f ms%n", seqBest);

        // Average case: random existing ID
        int avgId = DataGenerator.getRandomExistingId(products);
        double seqAvg = measureSequential(searcher, products, avgId);
        System.out.printf("  Average Case (random existing ID): %.3f ms%n", seqAvg);

        // Worst case: ID not in array
        int worstId = DataGenerator.getWorstCaseId(products);
        double seqWorst = measureSequential(searcher, products, worstId);
        System.out.printf("  Worst Case (ID not found):         %.3f ms%n", seqWorst);

        // ----------------------------------------------------------------
        // BINARY SEARCH BENCHMARKS
        // ----------------------------------------------------------------
        System.out.println("\nBINARY SEARCH:");
        System.out.println("-".repeat(50));

        // Best case: ID at midpoint
        int midId = DataGenerator.getMidId(products);
        double binBest = measureBinary(searcher, products, midId);
        System.out.printf("  Best Case (ID at midpoint):        %.3f ms%n", binBest);

        // Average case: random existing ID
        double binAvg = measureBinary(searcher, products, avgId);
        System.out.printf("  Average Case (random existing ID): %.3f ms%n", binAvg);

        // Worst case: ID not in array
        double binWorst = measureBinary(searcher, products, worstId);
        System.out.printf("  Worst Case (ID not found):         %.3f ms%n", binWorst);

        // ----------------------------------------------------------------
        // PERFORMANCE COMPARISON
        // ----------------------------------------------------------------
        double speedup = (seqAvg > 0 && binAvg > 0) ? (seqAvg / binAvg) : 0;
        System.out.printf("%nPERFORMANCE IMPROVEMENT: Binary search is ~%.0fx faster on average%n", speedup);

        // ----------------------------------------------------------------
        // HYBRID NAME SEARCH BENCHMARK
        // ----------------------------------------------------------------
        System.out.println("\nHYBRID NAME SEARCH:");
        System.out.println("-".repeat(50));

        // Build index
        long buildStart = System.nanoTime();
        Map<String, Product> nameIndex = searcher.buildNameIndex(products);
        long buildEnd = System.nanoTime();
        System.out.printf("  Index build time:      %.3f ms%n",
                (buildEnd - buildStart) / 1_000_000.0);

        // Search time
        String targetName = products[N / 3].getProductName();
        double hybridSearch = measureHybridSearch(searcher, nameIndex, targetName);
        System.out.printf("  Average search time:   %.3f ms%n", hybridSearch);

        // Insert time (add product maintaining sorted order)
        Product[] extendedProducts = java.util.Arrays.copyOf(products, N + 1);
        Product newProduct = new Product(999999, "NewTech Gadget", "Accessory", 99.99, 100);
        long insertStart = System.nanoTime();
        searcher.addProduct(extendedProducts, newProduct);
        long insertEnd = System.nanoTime();
        System.out.printf("  Insert time (sorted):  %.3f ms%n",
                (insertEnd - insertStart) / 1_000_000.0);

        // ----------------------------------------------------------------
        // COMPARISON TABLE
        // ----------------------------------------------------------------
        System.out.println("\n================================================================");
        System.out.println("SUMMARY TABLE");
        System.out.println("================================================================");
        System.out.printf("%-35s %-15s %-15s%n", "Test Case", "Sequential (ms)", "Binary (ms)");
        System.out.println("-".repeat(65));
        System.out.printf("%-35s %-15.3f %-15.3f%n", "Best Case",  seqBest,  binBest);
        System.out.printf("%-35s %-15.3f %-15.3f%n", "Average Case", seqAvg, binAvg);
        System.out.printf("%-35s %-15.3f %-15.3f%n", "Worst Case", seqWorst, binWorst);
        System.out.println("================================================================");
    }

    /**
     * Measures average sequential search time over RUNS iterations.
     */
    private static double measureSequential(ProductSearch s, Product[] products, int id) {
        long total = 0;
        for (int i = 0; i < RUNS; i++) {
            long start = System.nanoTime();
            s.sequentialSearchById(products, id);
            total += System.nanoTime() - start;
        }
        return (total / RUNS) / 1_000_000.0;
    }

    /**
     * Measures average binary search time over RUNS iterations.
     */
    private static double measureBinary(ProductSearch s, Product[] products, int id) {
        long total = 0;
        for (int i = 0; i < RUNS; i++) {
            long start = System.nanoTime();
            s.binarySearchById(products, id);
            total += System.nanoTime() - start;
        }
        return (total / RUNS) / 1_000_000.0;
    }

    /**
     * Measures average hybrid name search time over RUNS iterations.
     */
    private static double measureHybridSearch(ProductSearch s, Map<String, Product> index,
                                               String name) {
        long total = 0;
        for (int i = 0; i < RUNS; i++) {
            long start = System.nanoTime();
            s.hybridSearchByName(index, name);
            total += System.nanoTime() - start;
        }
        return (total / RUNS) / 1_000_000.0;
    }
}

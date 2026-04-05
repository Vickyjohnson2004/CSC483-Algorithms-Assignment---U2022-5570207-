package com.csc483.search;

import java.util.Random;
import java.util.HashSet;
import java.util.Set;

/**
 * Utility class to generate synthetic product datasets for TechMart benchmarks.
 *
 * @author Student
 * @version 1.0
 */
public class DataGenerator {

    private static final String[] CATEGORIES = {
        "Laptop", "Smartphone", "Tablet", "Monitor", "Keyboard",
        "Mouse", "Headphones", "Camera", "Printer", "Router"
    };

    private static final String[] BRANDS = {
        "TechPro", "EliteGear", "NovaTech", "ApexElec", "CoreSystems",
        "ZenithTech", "PrimeTech", "StarElec", "OmegaTech", "AlphaDev"
    };

    /**
     * Generates an array of n products with unique random IDs in range [1, 200000].
     * The array is sorted by productId to enable binary search.
     *
     * @param n  number of products to generate
     * @return   sorted Product array
     */
    public static Product[] generateSortedProducts(int n) {
        if (n <= 0) throw new IllegalArgumentException("n must be positive");

        Random random = new Random(42); // Fixed seed for reproducibility
        Set<Integer> usedIds = new HashSet<>(n * 2);
        Product[] products = new Product[n];

        int generated = 0;
        while (generated < n) {
            int id = random.nextInt(200000) + 1;
            if (usedIds.add(id)) {
                String brand = BRANDS[random.nextInt(BRANDS.length)];
                String category = CATEGORIES[random.nextInt(CATEGORIES.length)];
                String name = brand + " " + category + " " + (generated + 1);
                double price = 50.0 + (random.nextDouble() * 2950.0);
                int stock = random.nextInt(500);
                products[generated++] = new Product(id, name, category,
                        Math.round(price * 100.0) / 100.0, stock);
            }
        }

        // Sort by productId (insertion sort for clarity; could use Arrays.sort)
        java.util.Arrays.sort(products, java.util.Comparator.comparingInt(Product::getProductId));
        return products;
    }

    /**
     * Returns the product at index 0 (best case for sequential search).
     *
     * @param products  sorted product array
     * @return          the product at position 0
     */
    public static int getBestCaseId(Product[] products) {
        return products[0].getProductId();
    }

    /**
     * Returns the product ID at the midpoint (best case for binary search).
     *
     * @param products  sorted product array
     * @return          the product ID at the midpoint
     */
    public static int getMidId(Product[] products) {
        return products[products.length / 2].getProductId();
    }

    /**
     * Returns a valid product ID roughly in the middle of the dataset (average case).
     *
     * @param products  sorted product array
     * @return          a random existing product ID
     */
    public static int getRandomExistingId(Product[] products) {
        Random rng = new Random();
        return products[rng.nextInt(products.length)].getProductId();
    }

    /**
     * Returns an ID guaranteed NOT to be in the array (worst case).
     *
     * @param products  sorted product array
     * @return          an ID not present in the array
     */
    public static int getWorstCaseId(Product[] products) {
        // Use a value larger than the largest ID
        return products[products.length - 1].getProductId() + 1;
    }
}

package com.csc483.search;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides search methods for the TechMart product catalog.
 * Includes sequential search, binary search, and a hybrid name search.
 *
 * <p>Time Complexities:
 * <ul>
 *   <li>Sequential Search: O(n) worst/average, O(1) best</li>
 *   <li>Binary Search: O(log n) worst/average, O(1) best</li>
 *   <li>Name Search (HashMap): O(1) average</li>
 * </ul>
 *
 * @author Student
 * @version 1.0
 */
public class ProductSearch {

    /** Comparison counter for analysis purposes */
    private long comparisonCount = 0;

    /**
     * Resets the comparison counter to zero.
     */
    public void resetCounter() {
        comparisonCount = 0;
    }

    /**
     * Returns the number of comparisons made in the last operation.
     *
     * @return comparison count
     */
    public long getComparisonCount() {
        return comparisonCount;
    }

    // ====================================================================
    // SEQUENTIAL SEARCH BY ID
    // ====================================================================

    /**
     * Performs a linear (sequential) search for a product by its ID.
     *
     * <p>Best Case:  O(1)  - product found at index 0</p>
     * <p>Average:   O(n/2) - product found at middle on average</p>
     * <p>Worst Case: O(n)  - product not found, entire array scanned</p>
     *
     * @param products  array of Product objects
     * @param targetId  the product ID to search for
     * @return the matching Product, or null if not found
     */
    public Product sequentialSearchById(Product[] products, int targetId) {
        resetCounter();
        if (products == null) return null;

        for (int i = 0; i < products.length; i++) {
            comparisonCount++;
            if (products[i] != null && products[i].getProductId() == targetId) {
                return products[i];
            }
        }
        return null; // Not found
    }

    // ====================================================================
    // BINARY SEARCH BY ID
    // ====================================================================

    /**
     * Performs a binary search for a product by its ID.
     *
     * <p><b>Precondition:</b> The array must be sorted in ascending order by productId.</p>
     *
     * <p>Best Case:  O(1)     - product found at midpoint</p>
     * <p>Average:   O(log n)  - approximately log2(n) comparisons</p>
     * <p>Worst Case: O(log n) - product not found</p>
     *
     * @param products  array of Product objects sorted by ID
     * @param targetId  the product ID to search for
     * @return the matching Product, or null if not found
     */
    public Product binarySearchById(Product[] products, int targetId) {
        resetCounter();
        if (products == null || products.length == 0) return null;

        int low = 0;
        int high = products.length - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2; // Avoids integer overflow
            comparisonCount++;

            if (products[mid] == null) {
                high = mid - 1;
                continue;
            }

            int midId = products[mid].getProductId();

            if (midId == targetId) {
                return products[mid]; // Found
            } else if (midId < targetId) {
                low = mid + 1;         // Search right half
            } else {
                high = mid - 1;        // Search left half
            }
        }
        return null; // Not found
    }

    // ====================================================================
    // SEQUENTIAL SEARCH BY NAME
    // ====================================================================

    /**
     * Performs a sequential (linear) search by product name (case-insensitive).
     * Binary search cannot be used here because names are not sorted.
     *
     * <p>Time Complexity: O(n) in all cases</p>
     *
     * @param products    array of Product objects
     * @param targetName  the product name to search for
     * @return the matching Product, or null if not found
     */
    public Product searchByName(Product[] products, String targetName) {
        resetCounter();
        if (products == null || targetName == null) return null;

        for (Product product : products) {
            comparisonCount++;
            if (product != null && product.getProductName().equalsIgnoreCase(targetName)) {
                return product;
            }
        }
        return null; // Not found
    }

    // ====================================================================
    // HYBRID APPROACH: HashMap-Based Name Index
    // ====================================================================

    /**
     * Builds a HashMap index from product name to Product for O(1) name lookups.
     *
     * <p>Time Complexity to Build: O(n)</p>
     * <p>Space Complexity: O(n)</p>
     *
     * @param products  array of Product objects
     * @return HashMap mapping lowercase name to Product
     */
    public Map<String, Product> buildNameIndex(Product[] products) {
        if (products == null) return new HashMap<>();
        Map<String, Product> index = new HashMap<>(products.length * 2);
        for (Product p : products) {
            if (p != null) {
                index.put(p.getProductName().toLowerCase(), p);
            }
        }
        return index;
    }

    /**
     * Searches for a product by name using the pre-built HashMap index.
     * Average time complexity: O(1).
     *
     * @param nameIndex   the HashMap built by buildNameIndex()
     * @param targetName  the product name to search for
     * @return the matching Product, or null if not found
     */
    public Product hybridSearchByName(Map<String, Product> nameIndex, String targetName) {
        if (nameIndex == null || targetName == null) return null;
        return nameIndex.get(targetName.toLowerCase());
    }

    // ====================================================================
    // ADD PRODUCT (maintaining sorted order for binary search)
    // ====================================================================

    /**
     * Inserts a new product into a sorted array, maintaining ascending order by ID.
     * Uses insertion-sort logic to shift elements and place the new product.
     *
     * <p>Time Complexity: O(n) - shifting elements in worst case</p>
     * <p>Precondition: products array must be sorted and have at least one null slot at end</p>
     *
     * @param products    sorted Product array (last element must be null for space)
     * @param newProduct  the Product to insert
     * @return true if insertion was successful, false if array is full
     */
    public boolean addProduct(Product[] products, Product newProduct) {
        if (products == null || newProduct == null) return false;

        // Find last null slot (the capacity slot)
        int lastNull = -1;
        for (int i = products.length - 1; i >= 0; i--) {
            if (products[i] == null) {
                lastNull = i;
                break;
            }
        }
        if (lastNull == -1) return false; // Array is full

        // Find correct insertion position using binary search
        int insertPos = findInsertPosition(products, newProduct.getProductId(), lastNull);

        // Shift elements right from insertPos to lastNull
        for (int i = lastNull; i > insertPos; i--) {
            products[i] = products[i - 1];
        }
        products[insertPos] = newProduct;
        return true;
    }

    /**
     * Finds the correct insertion position using binary search logic.
     *
     * @param products   sorted array
     * @param targetId   ID to insert
     * @param limit      the exclusive upper bound (null index)
     * @return index where the new product should be inserted
     */
    private int findInsertPosition(Product[] products, int targetId, int limit) {
        int low = 0;
        int high = limit - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (products[mid] == null || products[mid].getProductId() > targetId) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return low;
    }
}

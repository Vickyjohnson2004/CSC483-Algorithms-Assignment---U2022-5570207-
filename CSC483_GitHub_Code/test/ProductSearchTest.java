package com.csc483.search;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 tests for ProductSearch correctness.
 * Covers: empty arrays, null inputs, best/average/worst cases, edge values.
 *
 * @author Student
 * @version 1.0
 */
@DisplayName("ProductSearch Tests")
class ProductSearchTest {

    private ProductSearch searcher;
    private Product[] sortedProducts;

    @BeforeEach
    void setUp() {
        searcher = new ProductSearch();
        // Build a small sorted array for tests
        sortedProducts = new Product[]{
            new Product(100, "Alpha Laptop",    "Laptop",      999.99, 50),
            new Product(200, "Beta Phone",      "Smartphone",  499.99, 100),
            new Product(300, "Gamma Tablet",    "Tablet",      699.99, 30),
            new Product(400, "Delta Monitor",   "Monitor",     399.99, 20),
            new Product(500, "Epsilon Keyboard","Keyboard",     79.99, 200)
        };
    }

    // ====================================================================
    // SEQUENTIAL SEARCH TESTS
    // ====================================================================

    @Test
    @DisplayName("Sequential: find first element (best case)")
    void testSequentialBestCase() {
        Product result = searcher.sequentialSearchById(sortedProducts, 100);
        assertNotNull(result);
        assertEquals(100, result.getProductId());
        assertEquals(1, searcher.getComparisonCount());
    }

    @Test
    @DisplayName("Sequential: find last element (worst found case)")
    void testSequentialLastElement() {
        Product result = searcher.sequentialSearchById(sortedProducts, 500);
        assertNotNull(result);
        assertEquals(500, result.getProductId());
        assertEquals(5, searcher.getComparisonCount());
    }

    @Test
    @DisplayName("Sequential: element not found (true worst case)")
    void testSequentialNotFound() {
        Product result = searcher.sequentialSearchById(sortedProducts, 999);
        assertNull(result);
        assertEquals(5, searcher.getComparisonCount()); // Full scan
    }

    @Test
    @DisplayName("Sequential: null array returns null")
    void testSequentialNullArray() {
        assertNull(searcher.sequentialSearchById(null, 100));
    }

    @Test
    @DisplayName("Sequential: empty array returns null")
    void testSequentialEmptyArray() {
        assertNull(searcher.sequentialSearchById(new Product[0], 100));
    }

    // ====================================================================
    // BINARY SEARCH TESTS
    // ====================================================================

    @Test
    @DisplayName("Binary: find middle element (best case)")
    void testBinaryBestCase() {
        Product result = searcher.binarySearchById(sortedProducts, 300);
        assertNotNull(result);
        assertEquals(300, result.getProductId());
        assertEquals(1, searcher.getComparisonCount()); // Found on first probe
    }

    @Test
    @DisplayName("Binary: find first element")
    void testBinaryFirstElement() {
        assertNotNull(searcher.binarySearchById(sortedProducts, 100));
    }

    @Test
    @DisplayName("Binary: find last element")
    void testBinaryLastElement() {
        assertNotNull(searcher.binarySearchById(sortedProducts, 500));
    }

    @Test
    @DisplayName("Binary: element not found returns null")
    void testBinaryNotFound() {
        assertNull(searcher.binarySearchById(sortedProducts, 999));
    }

    @Test
    @DisplayName("Binary: null array returns null")
    void testBinaryNullArray() {
        assertNull(searcher.binarySearchById(null, 100));
    }

    @Test
    @DisplayName("Binary: empty array returns null")
    void testBinaryEmptyArray() {
        assertNull(searcher.binarySearchById(new Product[0], 100));
    }

    @ParameterizedTest
    @ValueSource(ints = {100, 200, 300, 400, 500})
    @DisplayName("Binary: finds all products in array")
    void testBinaryFindsAll(int id) {
        assertNotNull(searcher.binarySearchById(sortedProducts, id));
    }

    // ====================================================================
    // NAME SEARCH TESTS
    // ====================================================================

    @Test
    @DisplayName("Name search: finds existing product case-insensitively")
    void testNameSearchFound() {
        Product result = searcher.searchByName(sortedProducts, "beta phone");
        assertNotNull(result);
        assertEquals(200, result.getProductId());
    }

    @Test
    @DisplayName("Name search: returns null for unknown name")
    void testNameSearchNotFound() {
        assertNull(searcher.searchByName(sortedProducts, "NonExistent"));
    }

    @Test
    @DisplayName("Name search: null name returns null")
    void testNameSearchNullName() {
        assertNull(searcher.searchByName(sortedProducts, null));
    }

    // ====================================================================
    // HYBRID SEARCH TESTS
    // ====================================================================

    @Test
    @DisplayName("Hybrid: index build and lookup O(1)")
    void testHybridSearch() {
        Map<String, Product> index = searcher.buildNameIndex(sortedProducts);
        Product result = searcher.hybridSearchByName(index, "Alpha Laptop");
        assertNotNull(result);
        assertEquals(100, result.getProductId());
    }

    @Test
    @DisplayName("Hybrid: case-insensitive lookup")
    void testHybridCaseInsensitive() {
        Map<String, Product> index = searcher.buildNameIndex(sortedProducts);
        assertNotNull(searcher.hybridSearchByName(index, "ALPHA LAPTOP"));
    }

    @Test
    @DisplayName("Hybrid: returns null for missing name")
    void testHybridNotFound() {
        Map<String, Product> index = searcher.buildNameIndex(sortedProducts);
        assertNull(searcher.hybridSearchByName(index, "Nonexistent Product"));
    }

    // ====================================================================
    // ADD PRODUCT TESTS
    // ====================================================================

    @Test
    @DisplayName("addProduct: maintains sorted order")
    void testAddProductMaintainsOrder() {
        // Add extra null slot for the new product
        Product[] extended = new Product[sortedProducts.length + 1];
        System.arraycopy(sortedProducts, 0, extended, 0, sortedProducts.length);

        Product newProduct = new Product(250, "New Gadget", "Tablet", 149.99, 10);
        boolean success = searcher.addProduct(extended, newProduct);
        assertTrue(success);

        // Verify sorted order
        for (int i = 0; i < extended.length - 1; i++) {
            if (extended[i] != null && extended[i + 1] != null) {
                assertTrue(extended[i].getProductId() <= extended[i + 1].getProductId(),
                        "Array is not sorted at index " + i);
            }
        }
    }

    @Test
    @DisplayName("addProduct: rejects null product")
    void testAddNullProduct() {
        Product[] extended = new Product[sortedProducts.length + 1];
        System.arraycopy(sortedProducts, 0, extended, 0, sortedProducts.length);
        assertFalse(searcher.addProduct(extended, null));
    }

    @Test
    @DisplayName("addProduct: rejects full array")
    void testAddToFullArray() {
        // No null slot available
        assertFalse(searcher.addProduct(sortedProducts, 
                new Product(999, "Test", "X", 1.0, 1)));
    }

    // ====================================================================
    // CONSISTENCY TESTS
    // ====================================================================

    @Test
    @DisplayName("Consistency: sequential and binary agree on all IDs")
    void testSearchConsistency() {
        int[] ids = {100, 200, 300, 400, 500};
        for (int id : ids) {
            Product seq = searcher.sequentialSearchById(sortedProducts, id);
            Product bin = searcher.binarySearchById(sortedProducts, id);
            assertNotNull(seq, "Sequential missed id=" + id);
            assertNotNull(bin, "Binary missed id=" + id);
            assertEquals(seq.getProductId(), bin.getProductId(),
                    "Mismatch for id=" + id);
        }
    }
}

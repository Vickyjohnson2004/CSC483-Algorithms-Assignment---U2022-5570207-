# CSC483 Algorithms Assignment

**Course:** CSC 483.1 – Algorithm Analysis and Design  
**Assignment:** Algorithm Design, Analysis, and Optimization for Real-World Systems  
**Semester:** 2025/2026 First Semester  

---

## Project Structure

```
CSC483-Algorithms-Assignment/
├── src/
│   └── com/csc483/
│       ├── search/
│       │   ├── Product.java          # Product entity class
│       │   ├── ProductSearch.java    # Search algorithm implementations
│       │   ├── DataGenerator.java    # Synthetic dataset generator
│       │   └── TechMartBenchmark.java# Performance benchmark (Q1)
│       └── sorting/
│           ├── SortingAlgorithms.java# Insertion, Merge, Quick, Heap sort
│           └── SortingBenchmark.java # Empirical comparison (Q2)
├── test/
│   ├── ProductSearchTest.java        # JUnit 5 tests for search
│   └── SortingAlgorithmsTest.java    # JUnit 5 tests for sorting
├── datasets/
│   └── sample_products.csv          # Sample dataset
└── README.md
```

---

## Requirements

- Java 11 or higher
- JUnit 5 (for running tests)

---

## Compilation

### Without an IDE

```bash
# Compile source files
javac -d out src/com/csc483/search/*.java src/com/csc483/sorting/*.java
```

### With Maven (if pom.xml is present)

```bash
mvn compile
mvn test
```

---

## Execution

### Question 1 – TechMart Search Benchmark

```bash
cd out
java com.csc483.search.TechMartBenchmark
```

**Expected output:**

```
================================================================
TECHMART SEARCH PERFORMANCE ANALYSIS (n = 100,000 products)
================================================================
SEQUENTIAL SEARCH:
  Best Case (ID at position 0):     0.001 ms
  Average Case (random existing ID): 30.452 ms
  Worst Case (ID not found):         62.110 ms

BINARY SEARCH:
  Best Case (ID at midpoint):        0.001 ms
  Average Case (random existing ID): 0.012 ms
  Worst Case (ID not found):         0.015 ms

PERFORMANCE IMPROVEMENT: Binary search is ~2537x faster on average
```

### Question 2 – Sorting Algorithms Benchmark

```bash
cd out
java com.csc483.sorting.SortingBenchmark
```

---

## Running Tests

```bash
# With JUnit 5 on classpath
javac -cp .:junit-5.jar test/*.java src/**/*.java
java -cp .:junit-5.jar org.junit.platform.console.standalone.ConsoleLauncher \
     --scan-class-path
```

---

## Known Limitations

- Quick Sort worst case is O(n²) for already-sorted data without median-of-three pivot (median-of-three is implemented here to mitigate this).
- Binary search requires the array to be pre-sorted by `productId`.
- `addProduct` requires the last slot of the array to be `null`; calling code must ensure capacity.

---

## Dependencies

| Library   | Version | Purpose             |
|-----------|---------|---------------------|
| JUnit 5   | 5.x     | Unit testing        |
| Java SDK  | 11+     | Compilation/runtime |

---

## Sample Usage

```java
Product[] products = DataGenerator.generateSortedProducts(100_000);
ProductSearch searcher = new ProductSearch();

// Binary search (O(log n))
Product found = searcher.binarySearchById(products, 42500);

// Hybrid name search (O(1) average)
Map<String, Product> index = searcher.buildNameIndex(products);
Product byName = searcher.hybridSearchByName(index, "TechPro Laptop 1");
```

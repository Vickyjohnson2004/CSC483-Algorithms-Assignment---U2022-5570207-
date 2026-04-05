package com.csc483.search;

/**
 * Represents a product in the TechMart online store.
 * Implements Comparable to support sorting by productId.
 *
 * @author Student
 * @version 1.0
 */
public class Product implements Comparable<Product> {

    /** Unique product identifier */
    private int productId;

    /** Name of the product */
    private String productName;

    /** Product category (e.g., Laptop, Phone, Tablet) */
    private String category;

    /** Price of the product in dollars */
    private double price;

    /** Number of items currently in stock */
    private int stockQuantity;

    /**
     * Constructs a Product with all required attributes.
     *
     * @param productId     unique identifier for the product
     * @param productName   name of the product
     * @param category      category of the product
     * @param price         price of the product
     * @param stockQuantity quantity in stock
     */
    public Product(int productId, String productName, String category,
                   double price, int stockQuantity) {
        this.productId = productId;
        this.productName = productName;
        this.category = category;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    // ===================== Getters =====================

    /** @return the product ID */
    public int getProductId() { return productId; }

    /** @return the product name */
    public String getProductName() { return productName; }

    /** @return the product category */
    public String getCategory() { return category; }

    /** @return the product price */
    public double getPrice() { return price; }

    /** @return the stock quantity */
    public int getStockQuantity() { return stockQuantity; }

    // ===================== Setters =====================

    /** @param productId the product ID to set */
    public void setProductId(int productId) { this.productId = productId; }

    /** @param productName the product name to set */
    public void setProductName(String productName) { this.productName = productName; }

    /** @param category the category to set */
    public void setCategory(String category) { this.category = category; }

    /** @param price the price to set */
    public void setPrice(double price) { this.price = price; }

    /** @param stockQuantity the stock quantity to set */
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }

    /**
     * Compares this product to another by productId.
     * Required for binary search and sorting.
     *
     * @param other the other product
     * @return negative, zero, or positive integer
     */
    @Override
    public int compareTo(Product other) {
        return Integer.compare(this.productId, other.productId);
    }

    /**
     * Returns a string representation of the product.
     *
     * @return formatted product string
     */
    @Override
    public String toString() {
        return String.format("Product{id=%d, name='%s', category='%s', price=%.2f, stock=%d}",
                productId, productName, category, price, stockQuantity);
    }

    /**
     * Checks equality based on productId.
     *
     * @param obj the object to compare
     * @return true if equal
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Product)) return false;
        Product other = (Product) obj;
        return this.productId == other.productId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(productId);
    }
}

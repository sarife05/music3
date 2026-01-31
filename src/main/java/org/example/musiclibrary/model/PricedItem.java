package org.example.musiclibrary.model;
/**
 * PricedItem interface for entities that have a price.
 * Demonstrates ISP: only items that have pricing implement this interface.
 */
public interface PricedItem {

    /**
     * Get the price of the item
     */
    double getPrice();

    /**
     * Set the price of the item
     */
    void setPrice(double price);

    /**
     * Calculate total price for multiple quantities
     */
    double calculateTotalPrice(int quantity);

    /**
     * Default method: Apply discount
     */
    default double applyDiscount(double discountPercent) {
        if (discountPercent < 0 || discountPercent > 100) {
            throw new IllegalArgumentException("Discount must be between 0 and 100");
        }
        return getPrice() * (1 - discountPercent / 100.0);
    }

    /**
     * Static method: Compare prices
     */
    static int comparePrice(PricedItem item1, PricedItem item2) {
        return Double.compare(item1.getPrice(), item2.getPrice());
    }
}
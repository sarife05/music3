package org.example.musiclibrary.model;
/**
 * Printable interface for entities that can display formatted information.
 * Demonstrates ISP: only entities that need printing implement this.
 */
public interface Printable {

    /**
     * Print formatted information about the entity
     */
    void print();

    /**
     * Default method: Print with border
     */
    default void printWithBorder() {
        System.out.println("═══════════════════════════════════════════════════════");
        print();
        System.out.println("═══════════════════════════════════════════════════════");
    }
}
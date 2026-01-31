package org.example.musiclibrary.model;

import org.example.musiclibrary.exception.InvalidInputException;

/**
 * Generic Validatable interface for entities that require validation.
 * Demonstrates Generics and ISP (Interface Segregation Principle).
 * Type parameter T allows this interface to be used with any entity type.
 */
public interface Validatable<T> {

    /**
     * Validate the entity's state
     * @throws InvalidInputException if validation fails
     */
    void validate() throws InvalidInputException;

    /**
     * Default method: Check if entity is valid without throwing exception
     * @return true if valid, false otherwise
     */
    default boolean isValid() {
        try {
            validate();
            return true;
        } catch (InvalidInputException e) {
            return false;
        }
    }

    /**
     * Static method: Validate multiple entities at once
     * Demonstrates static methods in interfaces
     */
    @SafeVarargs
    static <T extends Validatable<T>> boolean validateAll(T... entities) throws InvalidInputException {
        for (T entity : entities) {
            entity.validate();
        }
        return true;
    }
}
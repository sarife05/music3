package org.example.musiclibrary.repository;

import org.example.musiclibrary.exception.DatabaseOperationException;
import org.example.musiclibrary.exception.ResourceNotFoundException;
import java.util.List;

/**
 * Generic CRUD Repository interface.
 * Demonstrates Generics and DIP (Dependency Inversion Principle).
 * Type parameter T represents the entity type.
 * Follows ISP: defines only essential CRUD operations.
 */
public interface CrudRepository<T> {

    /**
     * Create a new entity in the database
     * @param entity The entity to create
     * @return The created entity with generated ID
     * @throws DatabaseOperationException if creation fails
     */
    T create(T entity) throws DatabaseOperationException;

    /**
     * Retrieve all entities from the database
     * @return List of all entities
     * @throws DatabaseOperationException if retrieval fails
     */
    List<T> getAll() throws DatabaseOperationException;

    /**
     * Retrieve an entity by its ID
     * @param id The entity ID
     * @return The entity if found
     * @throws ResourceNotFoundException if entity not found
     * @throws DatabaseOperationException if retrieval fails
     */
    T getById(int id) throws ResourceNotFoundException, DatabaseOperationException;

    /**
     * Update an existing entity
     * @param id The entity ID
     * @param entity The updated entity data
     * @return The updated entity
     * @throws ResourceNotFoundException if entity not found
     * @throws DatabaseOperationException if update fails
     */
    T update(int id, T entity) throws ResourceNotFoundException, DatabaseOperationException;

    /**
     * Delete an entity by its ID
     * @param id The entity ID
     * @return true if deleted successfully
     * @throws ResourceNotFoundException if entity not found
     * @throws DatabaseOperationException if deletion fails
     */
    boolean delete(int id) throws ResourceNotFoundException, DatabaseOperationException;

    /**
     * Check if an entity exists by ID
     * @param id The entity ID
     * @return true if exists
     * @throws DatabaseOperationException if check fails
     */
    boolean exists(int id) throws DatabaseOperationException;
}
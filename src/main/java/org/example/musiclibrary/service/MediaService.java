package org.example.musiclibrary.service;

import org.example.musiclibrary.exception.*;
import org.example.musiclibrary.model.Media;
import java.util.List;

/**
 * MediaService interface defining business operations for Media.
 * Demonstrates DIP (Dependency Inversion Principle):
 * Controller depends on this interface, not the implementation.
 * Follows ISP: Defines only methods relevant to media business logic.
 */
public interface MediaService {

    /**
     * Create a new media item with validation
     */
    Media createMedia(Media media) throws InvalidInputException, DuplicateResourceException, DatabaseOperationException;

    /**
     * Get all media items
     */
    List<Media> getAllMedia() throws DatabaseOperationException;

    /**
     * Get media by ID
     */
    Media getMediaById(int id) throws ResourceNotFoundException, DatabaseOperationException;

    /**
     * Update existing media with validation
     */
    Media updateMedia(int id, Media media) throws ResourceNotFoundException, InvalidInputException, DatabaseOperationException;

    /**
     * Delete media
     */
    void deleteMedia(int id) throws ResourceNotFoundException, DatabaseOperationException;

    /**
     * Get media by type
     */
    List<Media> getMediaByType(Media.MediaType type) throws DatabaseOperationException;

    /**
     * Get media by creator
     */
    List<Media> getMediaByCreator(String creator) throws DatabaseOperationException;

    /**
     * Search media by name
     */
    List<Media> searchMediaByName(String keyword) throws DatabaseOperationException;
}
package org.example.musiclibrary.repository;

import org.example.musiclibrary.exception.DatabaseOperationException;
import org.example.musiclibrary.model.Media;
import java.util.List;

/**
 * MediaRepository interface extending generic CrudRepository.
 * Demonstrates DIP and ISP: defines contract for media-specific operations.
 */
public interface MediaRepository extends CrudRepository<Media> {

    /**
     * Find media by type
     */
    List<Media> findByType(Media.MediaType type) throws DatabaseOperationException;

    /**
     * Find media by creator
     */
    List<Media> findByCreator(String creator) throws DatabaseOperationException;

    /**
     * Search media by name
     */
    List<Media> searchByName(String keyword) throws DatabaseOperationException;

    /**
     * Check if media exists by name, type, and creator
     */
    boolean existsByNameAndTypeAndCreator(String name, Media.MediaType type, String creator)
            throws DatabaseOperationException;
}
package org.example.musiclibrary.service;

import org.example.musiclibrary.exception.*;
import org.example.musiclibrary.model.Media;
import org.example.musiclibrary.repository.MediaRepository;

import java.util.List;

/**
 * Implementation of MediaService with business logic and validation.
 * Follows SRP: Single responsibility is managing media business logic.
 * Follows DIP: Depends on MediaRepository interface, not implementation.
 * Follows OCP: Open for extension through inheritance, closed for modification.
 */
public class MediaServiceImpl implements MediaService {

    private final MediaRepository mediaRepository;

    /**
     * Constructor injection demonstrating DIP
     */
    public MediaServiceImpl(MediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
    }

    @Override
    public Media createMedia(Media media) throws InvalidInputException, DuplicateResourceException, DatabaseOperationException {
        // Validation
        media.validate();

        // Business rule: Check for duplicates
        if (mediaRepository.existsByNameAndTypeAndCreator(media.getName(), media.getType(), media.getCreator())) {
            throw new DuplicateResourceException("Media",
                    String.format("%s '%s' by %s", media.getType(), media.getName(), media.getCreator()));
        }

        // Additional business rules
        if (media.getDuration() > 86400) { // Max 24 hours
            throw new InvalidInputException("Media duration cannot exceed 24 hours");
        }

        return mediaRepository.create(media);
    }

    @Override
    public List<Media> getAllMedia() throws DatabaseOperationException {
        return mediaRepository.getAll();
    }

    @Override
    public Media getMediaById(int id) throws ResourceNotFoundException, DatabaseOperationException {
        if (id <= 0) {
            throw new ResourceNotFoundException("Media with invalid ID: " + id);
        }
        return mediaRepository.getById(id);
    }

    @Override
    public Media updateMedia(int id, Media media) throws ResourceNotFoundException, InvalidInputException, DatabaseOperationException {
        // Validation
        media.validate();

        // Business rule: Ensure media exists
        if (!mediaRepository.exists(id)) {
            throw new ResourceNotFoundException("Media", id);
        }

        // Additional business rules
        if (media.getDuration() > 86400) {
            throw new InvalidInputException("Media duration cannot exceed 24 hours");
        }

        return mediaRepository.update(id, media);
    }

    @Override
    public void deleteMedia(int id) throws ResourceNotFoundException, DatabaseOperationException {
        if (!mediaRepository.exists(id)) {
            throw new ResourceNotFoundException("Media", id);
        }

        mediaRepository.delete(id);
    }

    @Override
    public List<Media> getMediaByType(Media.MediaType type) throws DatabaseOperationException {
        if (type == null) {
            throw new IllegalArgumentException("Media type cannot be null");
        }
        return mediaRepository.findByType(type);
    }

    @Override
    public List<Media> getMediaByCreator(String creator) throws DatabaseOperationException {
        if (creator == null || creator.trim().isEmpty()) {
            throw new IllegalArgumentException("Creator name cannot be empty");
        }
        return mediaRepository.findByCreator(creator);
    }

    @Override
    public List<Media> searchMediaByName(String keyword) throws DatabaseOperationException {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("Search keyword cannot be empty");
        }
        return mediaRepository.searchByName(keyword);
    }
}
package org.example.musiclibrary.service;

import org.example.musiclibrary.model.Media;
import org.example.musiclibrary.repository.MediaRepository;
import org.example.musiclibrary.exception.*;

import java.sql.SQLException;
import java.util.List;

public class MediaService {

    private final MediaRepository repository = new MediaRepository();


    public void create(Media media) {
        try {
            if (media == null) {
                throw new InvalidInputException("Media cannot be null");
            }

            media.validate();

            if (repository.getById(media.getId()) != null) {
                throw new DuplicateResourceException(
                        "Media with id " + media.getId() + " already exists"
                );
            }

            repository.create(media);

        } catch (SQLException e) {
            throw new DatabaseOperationException("Error creating media", e);
        }
    }


    public List<Media> getAll() {
        try {
            return repository.getAll();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error fetching media list", e);
        }
    }


    public Media getById(int id) {
        try {
            Media media = repository.getById(id);
            if (media == null) {
                throw new ResourceNotFoundException(
                        "Media not found with id: " + id
                );
            }
            return media;

        } catch (SQLException e) {
            throw new DatabaseOperationException("Error fetching media by id", e);
        }
    }


    public void update(int id, Media media) {
        try {
            if (repository.getById(id) == null) {
                throw new ResourceNotFoundException(
                        "Cannot update. Media not found with id: " + id
                );
            }

            media.validate();
            repository.update(id, media);

        } catch (SQLException e) {
            throw new DatabaseOperationException("Error updating media", e);
        }
    }


    public void delete(int id) {
        try {
            if (repository.getById(id) == null) {
                throw new ResourceNotFoundException(
                        "Cannot delete. Media not found with id: " + id
                );
            }

            repository.delete(id);

        } catch (SQLException e) {
            throw new DatabaseOperationException("Error deleting media", e);
        }
    }
}


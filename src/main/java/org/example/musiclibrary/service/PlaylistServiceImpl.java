package org.example.musiclibrary.service;

import org.example.musiclibrary.exception.*;
import org.example.musiclibrary.model.Playlist;
import org.example.musiclibrary.repository.MediaRepository;
import org.example.musiclibrary.repository.PlaylistRepository;

import java.util.List;

/**
 * Implementation of PlaylistService with business logic and validation.
 * Follows SRP: Single responsibility is managing playlist business logic.
 * Follows DIP: Depends on repository interfaces.
 */
public class PlaylistServiceImpl implements PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final MediaRepository mediaRepository;

    /**
     * Constructor injection demonstrating DIP
     */
    public PlaylistServiceImpl(PlaylistRepository playlistRepository, MediaRepository mediaRepository) {
        this.playlistRepository = playlistRepository;
        this.mediaRepository = mediaRepository;
    }

    @Override
    public Playlist createPlaylist(Playlist playlist) throws InvalidInputException, DuplicateResourceException, DatabaseOperationException {
        // Validation
        playlist.validate();

        // Business rule: Check for duplicate playlist names
        if (playlistRepository.existsByName(playlist.getName())) {
            throw new DuplicateResourceException("Playlist", playlist.getName());
        }

        return playlistRepository.create(playlist);
    }

    @Override
    public List<Playlist> getAllPlaylists() throws DatabaseOperationException {
        return playlistRepository.getAll();
    }

    @Override
    public Playlist getPlaylistById(int id) throws ResourceNotFoundException, DatabaseOperationException {
        if (id <= 0) {
            throw new ResourceNotFoundException("Playlist with invalid ID: " + id);
        }
        return playlistRepository.getById(id);
    }

    @Override
    public Playlist updatePlaylist(int id, Playlist playlist) throws ResourceNotFoundException, InvalidInputException, DatabaseOperationException {
        // Validation
        playlist.validate();

        // Business rule: Ensure playlist exists
        if (!playlistRepository.exists(id)) {
            throw new ResourceNotFoundException("Playlist", id);
        }

        // Business rule: If name is changing, check for duplicates
        Playlist existing = playlistRepository.getById(id);
        if (!existing.getName().equalsIgnoreCase(playlist.getName())) {
            if (playlistRepository.existsByName(playlist.getName())) {
                throw new DuplicateResourceException("Playlist", playlist.getName());
            }
        }

        return playlistRepository.update(id, playlist);
    }

    @Override
    public void deletePlaylist(int id) throws ResourceNotFoundException, DatabaseOperationException {
        if (!playlistRepository.exists(id)) {
            throw new ResourceNotFoundException("Playlist", id);
        }

        playlistRepository.delete(id);
    }

    @Override
    public void addMediaToPlaylist(int playlistId, int mediaId) throws ResourceNotFoundException, DatabaseOperationException {
        // Business rule: Validate both playlist and media exist
        if (!playlistRepository.exists(playlistId)) {
            throw new ResourceNotFoundException("Playlist", playlistId);
        }

        if (!mediaRepository.exists(mediaId)) {
            throw new ResourceNotFoundException("Media", mediaId);
        }

        playlistRepository.addMediaToPlaylist(playlistId, mediaId);
    }

    @Override
    public void removeMediaFromPlaylist(int playlistId, int mediaId) throws DatabaseOperationException {
        playlistRepository.removeMediaFromPlaylist(playlistId, mediaId);
    }

    @Override
    public Playlist getPlaylistByName(String name) throws ResourceNotFoundException, DatabaseOperationException {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Playlist name cannot be empty");
        }

        Playlist playlist = playlistRepository.findByName(name);
        if (playlist == null) {
            throw new ResourceNotFoundException("Playlist", name);
        }

        return playlist;
    }
}
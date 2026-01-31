package org.example.musiclibrary.service;

import org.example.musiclibrary.exception.*;
import org.example.musiclibrary.model.Playlist;
import java.util.List;

/**
 * PlaylistService interface defining business operations for Playlist.
 * Demonstrates DIP and ISP.
 */
public interface PlaylistService {

    /**
     * Create a new playlist with validation
     */
    Playlist createPlaylist(Playlist playlist) throws InvalidInputException, DuplicateResourceException, DatabaseOperationException;

    /**
     * Get all playlists
     */
    List<Playlist> getAllPlaylists() throws DatabaseOperationException;

    /**
     * Get playlist by ID
     */
    Playlist getPlaylistById(int id) throws ResourceNotFoundException, DatabaseOperationException;

    /**
     * Update existing playlist with validation
     */
    Playlist updatePlaylist(int id, Playlist playlist) throws ResourceNotFoundException, InvalidInputException, DatabaseOperationException;

    /**
     * Delete playlist
     */
    void deletePlaylist(int id) throws ResourceNotFoundException, DatabaseOperationException;

    /**
     * Add media to playlist with validation
     */
    void addMediaToPlaylist(int playlistId, int mediaId) throws ResourceNotFoundException, DatabaseOperationException;

    /**
     * Remove media from playlist
     */
    void removeMediaFromPlaylist(int playlistId, int mediaId) throws DatabaseOperationException;

    /**
     * Get playlist by name
     */
    Playlist getPlaylistByName(String name) throws ResourceNotFoundException, DatabaseOperationException;
}